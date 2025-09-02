package com.example.inrussian.data.client.infrastructure

import co.touchlab.kermit.Logger
import com.example.inrussian.data.client.auth.ApiKeyAuth
import com.example.inrussian.data.client.auth.Authentication
import com.example.inrussian.data.client.auth.HttpBasicAuth
import com.example.inrussian.data.client.auth.HttpBearerAuth
import com.example.inrussian.data.client.auth.OAuth
import com.example.inrussian.data.client.models.RefreshResponseDto
import com.example.inrussian.platformInterfaces.UserConfigurationStorage
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.expectSuccess
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.Parameters
import io.ktor.http.URLBuilder
import io.ktor.http.content.PartData
import io.ktor.http.contentType
import io.ktor.http.encodeURLQueryComponent
import io.ktor.http.encodedPath
import io.ktor.http.takeFrom
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.openapitools.client.models.RefreshTokenRequest
import kotlin.concurrent.Volatile

open class ApiClient(
    private val baseUrl: String,
    private val userConfigurationStorage: UserConfigurationStorage,
) {
    @Volatile
    private var bearerToken: String? = null
    private lateinit var client: HttpClient

    constructor(
        baseUrl: String,
        httpClientEngine: HttpClientEngine?,
        userConfigurationStorage: UserConfigurationStorage,
        httpClientConfig: ((HttpClientConfig<*>) -> Unit)? = null,
        jsonBlock: Json,
    ) : this(baseUrl = baseUrl, userConfigurationStorage) {
        val clientConfig: (HttpClientConfig<*>) -> Unit by lazy {
            {
                it.install(ContentNegotiation) { json(jsonBlock) }
                httpClientConfig?.invoke(it)
            }
        }

        client = httpClientEngine?.let { HttpClient(it, clientConfig) } ?: HttpClient(clientConfig)
    }

    constructor(
        baseUrl: String,
        httpClient: HttpClient,
        userConfigurationStorage: UserConfigurationStorage,
    ) : this(baseUrl = baseUrl, userConfigurationStorage) {
        this.client = httpClient
    }

    private val authentications: Map<String, Authentication>? = null

    companion object {
        const val BASE_URL: String = "https://hits-playground.ru/api"
        val JSON_DEFAULT: Json = Json {
            ignoreUnknownKeys = true
            prettyPrint = true
            isLenient = true
        }
        protected val UNSAFE_HEADERS: List<String> = listOf(HttpHeaders.ContentType)
    }


    fun setUsername(username: String) {
        val auth = authentications?.values?.firstOrNull { it is HttpBasicAuth } as HttpBasicAuth?
            ?: throw Exception("No HTTP basic authentication configured")
        auth.username = username
    }

    /**
     * Set the password for the first HTTP basic authentication.
     *
     * @param password Password
     */
    fun setPassword(password: String) {
        val auth = authentications?.values?.firstOrNull { it is HttpBasicAuth } as HttpBasicAuth?
            ?: throw Exception("No HTTP basic authentication configured")
        auth.password = password
    }

    /**
     * Set the API key value for the first API key authentication.
     *
     * @param apiKey API key
     * @param paramName The name of the API key parameter, or null or set the first key.
     */
    fun setApiKey(apiKey: String, paramName: String? = null) {
        val auth =
            authentications?.values?.firstOrNull { it is ApiKeyAuth && (paramName == null || paramName == it.paramName) } as ApiKeyAuth?
                ?: throw Exception("No API key authentication configured")
        auth.apiKey = apiKey
    }

    /**
     * Set the API key prefix for the first API key authentication.
     *
     * @param apiKeyPrefix API key prefix
     * @param paramName The name of the API key parameter, or null or set the first key.
     */
    fun setApiKeyPrefix(apiKeyPrefix: String, paramName: String? = null) {
        val auth =
            authentications?.values?.firstOrNull { it is ApiKeyAuth && (paramName == null || paramName == it.paramName) } as ApiKeyAuth?
                ?: throw Exception("No API key authentication configured")
        auth.apiKeyPrefix = apiKeyPrefix
    }

    /**
     * Set the access token for the first OAuth2 authentication.
     *
     * @param accessToken Access token
     */
    fun setAccessToken(accessToken: String) {
        val auth = authentications?.values?.firstOrNull { it is OAuth } as OAuth?
            ?: throw Exception("No OAuth2 authentication configured")
        auth.accessToken = accessToken
    }

    /**
     * Set the access token for the first Bearer authentication.
     *
     * @param bearerToken The bearer token.
     */
    fun setBearerToken(bearerToken: String) {
        // если в authentications есть HttpBearerAuth — пользуемся им (обычное поведение)
        val auth = authentications?.values?.firstOrNull { it is HttpBearerAuth } as HttpBearerAuth?
        if (auth != null) {
            auth.bearerToken = bearerToken
        } else {
            this.bearerToken = bearerToken
        }
    }

    protected suspend fun <T : Any?> multipartFormRequest(
        requestConfig: RequestConfig<T>,
        body: List<PartData>?,
        authNames: List<String>
    ): HttpResponse {
        return request(requestConfig, MultiPartFormDataContent(body ?: listOf()), authNames)
    }

    protected suspend fun <T : Any?> urlEncodedFormRequest(
        requestConfig: RequestConfig<T>,
        body: Parameters?,
        authNames: List<String>
    ): HttpResponse {
        return request(requestConfig, FormDataContent(body ?: Parameters.Empty), authNames)
    }

    protected suspend fun <T : Any?> jsonRequest(
        requestConfig: RequestConfig<T>,
        body: Any? = null,
        authNames: List<String>
    ): HttpResponse = request(requestConfig, body, authNames)

    protected suspend fun <T : Any?> request(
        requestConfig: RequestConfig<T>,
        body: Any? = null,
        authNames: List<String>,
        allowRefresh: Boolean = true
    ): HttpResponse {
        requestConfig.updateForAuth<T>(authNames)

        suspend fun doRequest(): HttpResponse {
            // собираем актуальные заголовки прямо перед каждым запросом
            val headers = requestConfig.headers.toMutableMap()

            // fallback: если Authorization не задан явно, добавим локальный bearerToken
            val currentBearer = bearerToken
            if (headers[HttpHeaders.Authorization].isNullOrBlank() && !currentBearer.isNullOrBlank()) {
                headers[HttpHeaders.Authorization] = "Bearer $currentBearer"
            }

            // логируем для отладки (удаляй в проде или маскируй токен)
            Logger.d { "HTTP ${requestConfig.method} ${requestConfig.path} headers=${headers.keys}" }

            return client.request {
                expectSuccess = false
                url {
                    takeFrom(URLBuilder(baseUrl))
                    appendPath(requestConfig.path.trimStart('/').split('/'))
                    requestConfig.query.forEach { (k, vList) -> vList.forEach { parameter(k, it) } }
                }
                method = requestConfig.method.httpMethod

                // применяем headers, не включая unsafe
                headers.filter { (k, _) -> !UNSAFE_HEADERS.contains(k) }
                    .forEach { (k, v) -> header(k, v) }

                if (requestConfig.method in listOf(
                        RequestMethod.PUT,
                        RequestMethod.POST,
                        RequestMethod.PATCH
                    )
                ) {
                    val contentType =
                        (requestConfig.headers[HttpHeaders.ContentType]?.let { ContentType.parse(it) }
                            ?: ContentType.Application.Json)
                    contentType(contentType)
                    setBody(body)
                }
            }
        }

        var response = doRequest()

        // TRY REFRESH ONCE (only if allowed and status 401/403)
        if (allowRefresh && response.status.value in 401..403) {
            val raw = runCatching { response.bodyAsText() }.getOrNull()
            Logger.i { "Request failed with ${response.status}. Body: $raw" }

            val refreshToken =
                runCatching { userConfigurationStorage.getRefreshToken() }.getOrNull()
            if (!refreshToken.isNullOrBlank()) {
                try {
                    // вызываем refresh напрямую, без повторного allowRefresh внутри
                    val refreshResp =
                        authRefresh(RefreshTokenRequest(refreshToken), allowRefresh = false)

                    val refreshBody = runCatching { refreshResp.bodyAsText() }.getOrNull()
                    Logger.i { "Refresh response status=${refreshResp.status} bodyPreview=${refreshBody?.take(200)}" }

                    if (refreshResp.status.value in 200..299 && !refreshBody.isNullOrBlank()) {
                        // Парсим JSON с полем accessToken
                        val refreshDto = runCatching {
                            JSON_DEFAULT.decodeFromString(RefreshResponseDto.serializer(), refreshBody)
                        }.getOrNull()

                        val newAccessToken = refreshDto?.accessToken?.trim()

                        if (!newAccessToken.isNullOrBlank()) {
                            // Сохраняем и применяем чистый токен (только сам JWT)
                            userConfigurationStorage.saveToken(newAccessToken)           // защищённое хранилище
                            setBearerToken(newAccessToken)                              // локальная подстановка в ApiClient

                            // Повторяем исходный запрос один раз — doRequest() соберёт заголовки заново и подставит новый токен
                            response = doRequest()
                        } else {
                            // на случай, если ответ неожиданного формата
                            Logger.e { "Refresh returned no accessToken field. body=$refreshBody" }
                            throw IllegalStateException("Refresh returned invalid body")
                        }
                    } else {
                        // refresh неуспешен - очистка и проброс или обработка
                        Logger.w { "Refresh failed: status=${refreshResp.status}; body=$refreshBody" }
                        throw IllegalStateException(
                            refreshBody ?: "Refresh failed with ${refreshResp.status}"
                        )
                    }
                } catch (e: Throwable) {
                    // refresh закончился ошибкой — очистим auth и пробросим для внешней логики
                    throw e
                }
            }
        }

        // финальная обработка ответа
        if (response.status.value in 200..299) {
            return response
        } else {
            val raw = runCatching { response.bodyAsText() }.getOrNull()
            Logger.e { "HTTP ${response.status.value} -> $raw" }
            return response
        }
    }

    open suspend fun authRefresh(
        refreshTokenRequest: RefreshTokenRequest,
        allowRefresh: Boolean = false
    ): HttpResponse {

        return client.request {
            expectSuccess = false
            url {
                takeFrom(URLBuilder(baseUrl))
                appendPath(listOf("auth", "refresh"))
            }
            method = HttpMethod.Post
            contentType(ContentType.Application.Json)
            setBody(refreshTokenRequest)
        }
    }
    /*protected suspend fun <T: Any?> request(requestConfig: RequestConfig<T>, body: Any? = null, authNames: List<String>): HttpResponse {
        requestConfig.updateForAuth<T>(authNames)
        val headers = requestConfig.headers.toMutableMap()

        // fallback: if Authorization header not explicitly set and local bearerToken present -> add it
        if (headers[HttpHeaders.Authorization].isNullOrBlank() && bearerToken != null) {
            headers[HttpHeaders.Authorization] = "Bearer ${bearerToken!!}"
        }

        return client.request {
            this.url {
                this.takeFrom(URLBuilder(baseUrl))
                appendPath(requestConfig.path.trimStart('/').split('/'))
                requestConfig.query.forEach { query -> query.value.forEach { value -> parameter(query.key, value) } }
            }
            this.method = requestConfig.method.httpMethod

            headers.filter { header -> !UNSAFE_HEADERS.contains(header.key) }.forEach { header ->
                this.header(header.key, header.value)
            }

            if (requestConfig.method in listOf(RequestMethod.PUT, RequestMethod.POST, RequestMethod.PATCH)) {
                val contentType = (requestConfig.headers[HttpHeaders.ContentType]?.let { ContentType.parse(it) }
                    ?: ContentType.Application.Json)
                this.contentType(contentType)
                this.setBody(body)
            }
        }
    }*/
    // ...

    /*protected suspend fun <T: Any?> request(requestConfig: RequestConfig<T>, body: Any? = null, authNames: List<String>): HttpResponse {
        requestConfig.updateForAuth<T>(authNames)
        val headers = requestConfig.headers

        return client.request {
            this.url {
                this.takeFrom(URLBuilder(baseUrl))
                appendPath(requestConfig.path.trimStart('/').split('/'))
                requestConfig.query.forEach { query ->
                    query.value.forEach { value ->
                        parameter(query.key, value)
                    }
                }
            }
            this.method = requestConfig.method.httpMethod
            headers.filter { header -> !UNSAFE_HEADERS.contains(header.key) }.forEach { header -> this.header(header.key, header.value) }
            if (requestConfig.method in listOf(RequestMethod.PUT, RequestMethod.POST, RequestMethod.PATCH)) {
                val contentType = (requestConfig.headers[HttpHeaders.ContentType]?.let { ContentType.parse(it) }
                    ?: ContentType.Application.Json)
                this.contentType(contentType)
                this.setBody(body)
            }
        }
    }*/

    private fun <T : Any?> RequestConfig<T>.updateForAuth(authNames: List<String>) {
        for (authName in authNames) {
            val auth = authentications?.get(authName)
                ?: throw Exception("Authentication undefined: $authName")
            auth.apply(query, headers)
        }
    }

    private fun URLBuilder.appendPath(components: List<String>): URLBuilder = apply {
        encodedPath = encodedPath.trimEnd('/') + components.joinToString(
            "/",
            prefix = "/"
        ) { it.encodeURLQueryComponent() }
    }

    private val RequestMethod.httpMethod: HttpMethod
        get() = when (this) {
            RequestMethod.DELETE -> HttpMethod.Delete
            RequestMethod.GET -> HttpMethod.Get
            RequestMethod.HEAD -> HttpMethod.Head
            RequestMethod.PATCH -> HttpMethod.Patch
            RequestMethod.PUT -> HttpMethod.Put
            RequestMethod.POST -> HttpMethod.Post
            RequestMethod.OPTIONS -> HttpMethod.Options
        }
}

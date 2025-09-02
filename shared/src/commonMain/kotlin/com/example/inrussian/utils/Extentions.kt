package com.example.inrussian.utils

import co.touchlab.kermit.Logger
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.example.inrussian.data.client.infrastructure.HttpResponse
import com.example.inrussian.models.ErrorType.InvalidRefreshToken
import com.example.inrussian.platformInterfaces.UserConfigurationStorage
import com.example.inrussian.repository.auth.AuthRepository
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

fun ComponentContext.componentCoroutineScope(): CoroutineScope {
    val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    if (lifecycle.state != Lifecycle.State.DESTROYED) {
        lifecycle.doOnDestroy {
            scope.cancel()
        }
    } else {
        scope.cancel()
    }

    return scope
}

suspend fun <T : Any> HttpResponse<T>.errorHandle(
    userConfigurationStorage: UserConfigurationStorage? = null,
    authRepository: AuthRepository? = null,
): T {
    Logger.i { "code: $status" }
    when (status) {
        in 200..299 -> return this.body()
        in (401..403) -> {
            try {
                val refreshToken = userConfigurationStorage?.getRefreshToken()
                Logger.i { refreshToken.toString() }
                if (refreshToken != null) {
                    val result = try {
                        authRepository?.refreshToken(refreshToken)!!
                    } catch (e: Throwable) {
                        throw InvalidRefreshToken
                    }
                    authRepository.setToken(result)
                }

            } catch (e: Throwable) {

                Logger.i { e.message.toString() }
            }
            return this.body()
        }

        else -> ErrorDecoder.toErrorType(this.response.bodyAsText())
    }
}


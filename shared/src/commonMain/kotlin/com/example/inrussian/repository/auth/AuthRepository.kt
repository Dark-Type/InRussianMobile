package com.example.inrussian.repository.auth

import co.touchlab.kermit.Logger
import com.example.inrussian.data.client.apis.DefaultApi
import com.example.inrussian.data.client.models.PasswordResetRequest
import com.example.inrussian.data.client.models.PasswordResetResponse
import com.example.inrussian.data.client.models.RecoveryCheckRequest
import com.example.inrussian.models.models.AuthResponseModel
import com.example.inrussian.models.models.LoginModel
import com.example.inrussian.models.models.RegisterModel
import com.example.inrussian.platformInterfaces.UserConfigurationStorage
import com.example.inrussian.repository.toAuthResponseModel
import com.example.inrussian.repository.toLoginRequest
import com.example.inrussian.repository.toRegisterModel
import com.example.inrussian.utils.errorHandle
import io.ktor.utils.io.InternalAPI
import org.openapitools.client.models.RefreshTokenRequest


interface AuthRepository {
    suspend fun login(login: LoginModel): AuthResponseModel
    suspend fun register(registerModel: RegisterModel): AuthResponseModel
    suspend fun refreshToken(token: String): String
    suspend fun sendMail(email: String)
    suspend fun saveRefreshToken(token: String)
    suspend fun sendCode(code: String, mail: String)
    suspend fun updatePassword(email: String, code: String, password: String)
    suspend fun setToken(token: String)
}

class AuthRepositoryImpl(
    private val api: DefaultApi,
    private val userConfigurationStorage: UserConfigurationStorage
) :
    AuthRepository {

    @OptIn(InternalAPI::class)
    override suspend fun login(login: LoginModel): AuthResponseModel =
        api.authLoginPost(login.toLoginRequest()).errorHandle(userConfigurationStorage, this)
            .toAuthResponseModel()

    override suspend fun register(registerModel: RegisterModel): AuthResponseModel =
        api.authStudentRegisterPost(registerModel.toRegisterModel()).errorHandle()
            .toAuthResponseModel()

    override suspend fun refreshToken(token: String): String =
        api.authRefreshPost(refreshTokenRequest = RefreshTokenRequest(token)).body()

    override suspend fun sendMail(email: String) {
        api.recoveryRequest(email)
    }

    override suspend fun saveRefreshToken(token: String) {
        userConfigurationStorage.saveRefreshToken(token)
    }

    override suspend fun sendCode(code: String, mail: String) {
        api.recoveryCheck(RecoveryCheckRequest(mail, code))

    }

    override suspend fun updatePassword(token: String, code: String, password: String) {
        api.recoveryReset(PasswordResetRequest(token,code,password))
    }

    override suspend fun setToken(token: String) {
        Logger.d { "start" }
        userConfigurationStorage.saveToken(token)
        Logger.d { "saveToken" }
        try {
            api.setBearerToken(token)

        } catch (e: Throwable) {
            Logger.e { e.message.toString() }
        }
        Logger.d { "setBearerToken" }
    }


}

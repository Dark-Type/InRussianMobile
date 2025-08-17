package com.example.inrussian.repository.auth

import com.example.inrussian.models.models.AuthResponseModel
import com.example.inrussian.models.models.LoginModel

interface AuthRepository {
    suspend fun login(login: LoginModel): AuthResponseModel
    suspend fun register()
    suspend fun sendMail(email: String)
    suspend fun saveRefreshToken(token: String)
    suspend fun sendCode(code: String, mail: String)
    suspend fun updatePassword(email: String, password: String)
}

class AuthRepositoryImpl : AuthRepository {

    override suspend fun login(login: LoginModel): AuthResponseModel {
        TODO("Not yet implemented")
    }

    override suspend fun register() {
    }

    override suspend fun sendMail(email: String) {
    }

    override suspend fun saveRefreshToken(token: String) {
    }

    override suspend fun sendCode(code: String, mail: String) {
        TODO("Not yet implemented")
    }

    override suspend fun updatePassword(token: String, password: String) {
        TODO("Not yet implemented")
    }

}
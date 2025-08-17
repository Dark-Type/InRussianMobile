package com.example.inrussian.repository.auth

import com.example.inrussian.models.models.AuthResponseModel
import com.example.inrussian.models.models.LoginModel

interface AuthRepository {
    suspend fun login(login: LoginModel): AuthResponseModel
    suspend fun register()

    suspend fun saveRefreshToken(token: String)
}

class AuthRepositoryImpl : AuthRepository {

    override suspend fun login(login: LoginModel): AuthResponseModel {
        TODO("Not yet implemented")
    }

    override suspend fun register() {
        TODO("Not yet implemented")
    }

    override suspend fun saveRefreshToken(token: String) {
        TODO("Not yet implemented")
    }

}
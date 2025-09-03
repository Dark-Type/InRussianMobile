package com.example.inrussian.platformInterfaces

import com.example.inrussian.navigation.configurations.Configuration

sealed class Creds {
    data class Email(val email: String) : Creds()
    data class Password(val password: String) : Creds()
}
interface UserConfigurationStorage {
    fun save(configuration: Configuration)
    fun get(): Configuration?

    fun saveToken(token: String)
    fun getToken(): String?
    fun deleteToken()
    fun saveRefreshToken(token: String)
    fun getRefreshToken(): String?
    fun deleteRefreshToken()
    fun saveCreds(creds: Creds)
    fun getCreds(): Creds?
    fun deleteCreds()
}
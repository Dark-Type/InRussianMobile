package com.example.inrussian.platformInterfaces

import com.example.inrussian.navigation.configurations.Configuration

interface UserConfigurationStorage {
    fun save(configuration: Configuration)
    fun get(): Configuration?

    fun saveToken(token: String)
    fun getToken(): String?
    fun deleteToken()
    fun saveRefreshToken(token: String)
    fun getRefreshToken(): String?
    fun deleteRefreshToken()
}
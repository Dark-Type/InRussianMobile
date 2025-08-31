package com.example.inrussian.platformInterfaces

import com.example.inrussian.navigation.configurations.Configuration
import platform.Foundation.NSUserDefaults

class UserConfigurationStorageImpl(
    private val defaults: NSUserDefaults
) : UserConfigurationStorage {

    private val key = "last_configuration"
    private val tokenKey = "user_token"
    private val refreshTokenKey = "refresh_token"

    override fun save(configuration: Configuration) {
        val value = when (configuration) {
            Configuration.Auth -> "Auth"
            Configuration.Onboarding -> "Onboarding"
            Configuration.Main -> "Main"
        }
        defaults.setObject(value, forKey = key)
    }

    override fun get(): Configuration? {
        val stored = defaults.stringForKey(key)
        return when (stored) {
            "Auth" -> Configuration.Auth
            "Onboarding" -> Configuration.Onboarding
            "Main" -> Configuration.Main
            else -> Configuration.Auth
        }
    }

    override fun saveToken(token: String) {
        defaults.setObject(token, forKey = tokenKey)
    }

    override fun getToken(): String? {
        return defaults.stringForKey(tokenKey)
    }

    override fun deleteToken() {
        defaults.removeObjectForKey(tokenKey)
    }

    override fun saveRefreshToken(token: String) {
        defaults.setObject(token, forKey = refreshTokenKey)
    }

    override fun getRefreshToken(): String? {
        return defaults.stringForKey(refreshTokenKey)
    }

    override fun deleteRefreshToken() {
        defaults.removeObjectForKey(refreshTokenKey)
    }

}
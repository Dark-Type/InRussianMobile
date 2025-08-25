package com.example.inrussian.platformInterfaces
import android.content.Context
import com.example.inrussian.navigation.configurations.Configuration

class UserConfigurationStorageImpl(private val context: Context) : UserConfigurationStorage {
    private val prefs = context.getSharedPreferences("config_storage", Context.MODE_PRIVATE)
    private val key = "last_configuration"
    private val tokenKey = "user_token"

    override fun save(configuration: Configuration) {
        prefs.edit().putString(key, configuration::class.simpleName).apply()
    }

    override fun get(): Configuration? {
        return when (prefs.getString(key, null)) {
            "Auth" -> Configuration.Auth
            "Onboarding" -> Configuration.Onboarding
            "Main" -> Configuration.Main
            else -> null
        }
    }

    override fun saveToken(token: String) {
        prefs.edit().putString(tokenKey, token).apply()
    }

    override fun getToken(): String? {
        return prefs.getString(tokenKey, null)
    }

    override fun deleteToken() {
        prefs.edit().remove(tokenKey).apply()
    }
}


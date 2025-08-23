package com.example.inrussian.platformInterfaces
import android.content.Context
import com.example.inrussian.navigation.configurations.Configuration

class UserConfigurationStorageImpl(private val context: Context) : UserConfigurationStorage {
    private val prefs = context.getSharedPreferences("config_storage", Context.MODE_PRIVATE)
    private val key = "last_configuration"

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
}


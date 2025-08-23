package com.example.inrussian.platformInterfaces

import com.example.inrussian.navigation.configurations.Configuration
import platform.Foundation.NSUserDefaults

class UserConfigurationStorageImpl(
    private val defaults: NSUserDefaults
) : UserConfigurationStorage {

    private val key = "last_configuration"

    override fun save(configuration: Configuration) {
        val value = when (configuration) {
            Configuration.Auth -> "Auth"
            Configuration.Onboarding -> "Onboarding"
            Configuration.Main -> "Main"
        }
        println("UserConfigurationStorageImpl.save: writing value=$value")
        defaults.setObject(value, forKey = key)
    }

    override fun get(): Configuration? {
        val stored = defaults.stringForKey(key)
        println("UserConfigurationStorageImpl.get: read value=${stored ?: "null"}")
        return when (stored) {
            "Auth" -> Configuration.Auth
            "Onboarding" -> Configuration.Onboarding
            "Main" -> Configuration.Main
            else -> Configuration.Auth
        }
    }
}
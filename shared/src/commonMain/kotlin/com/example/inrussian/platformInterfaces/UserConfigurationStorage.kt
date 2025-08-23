package com.example.inrussian.platformInterfaces

import com.example.inrussian.navigation.configurations.Configuration

interface UserConfigurationStorage {
    fun save(configuration: Configuration)
    fun get(): Configuration?
}
package com.example.inrussian.navigation.configurations

import kotlinx.serialization.Serializable

@Serializable
sealed class Configuration {
    @Serializable
    data object Auth : Configuration()

    @Serializable
    data object Onboarding : Configuration()

    @Serializable
    data object Main : Configuration()
}
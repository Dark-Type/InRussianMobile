package com.example.inrussian.navigation.configurations

import kotlinx.serialization.Serializable

@Serializable
sealed class MainConfiguration {
    @Serializable
    data object Home : MainConfiguration()

    @Serializable
    data object Train : MainConfiguration()

    @Serializable
    data object Profile : MainConfiguration()
}
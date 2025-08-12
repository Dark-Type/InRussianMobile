package com.example.inrussian.navigation.configurations
import kotlinx.serialization.Serializable
@Serializable
sealed class ProfileConfiguration {
    @Serializable
    data object Main : ProfileConfiguration()

    @Serializable
    data object EditProfile : ProfileConfiguration()

    @Serializable
    data object About : ProfileConfiguration()

    @Serializable
    data object PrivacyPolicy : ProfileConfiguration()
}
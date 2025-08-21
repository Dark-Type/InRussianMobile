package com.example.inrussian.navigation.configurations

import kotlinx.serialization.Serializable

@Serializable
sealed class AuthConfiguration {
    @Serializable
    data object BaseAuth : AuthConfiguration()

    @Serializable
    data object Login : AuthConfiguration()

    @Serializable
    data object Register : AuthConfiguration()

    @Serializable
    data object VkPopover : AuthConfiguration()

    @Serializable
    data object YandexPopover : AuthConfiguration()

    @Serializable
    data object EnterEmail : AuthConfiguration()

    @Serializable
    data object EnterRecoveryCode : AuthConfiguration()

    @Serializable
    data object UpdatePassword : AuthConfiguration()

    @Serializable
    data object LanguageEmpty : AuthConfiguration()

    @Serializable
    data object LanguageFilled : AuthConfiguration()

    @Serializable
    data object PersonalDataEmpty : AuthConfiguration()

    @Serializable
    data object PersonalDataFilled : AuthConfiguration()

    @Serializable
    data object CitizenshipEmpty : AuthConfiguration()

    @Serializable
    data object CitizenshipFilled : AuthConfiguration()

    @Serializable
    data object EducationEmpty : AuthConfiguration()

    @Serializable
    data object EducationFilled : AuthConfiguration()

    @Serializable
    data object Confirmation : AuthConfiguration()
}
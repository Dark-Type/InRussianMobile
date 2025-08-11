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
    data object SsoPopover : AuthConfiguration()

    @Serializable
    data object EnterEmail : AuthConfiguration()

    @Serializable
    data object EnterRecoveryCode : AuthConfiguration()

    @Serializable
    data object UpdatePassword : AuthConfiguration()

}

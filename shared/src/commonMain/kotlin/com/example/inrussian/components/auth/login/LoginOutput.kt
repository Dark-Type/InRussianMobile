package com.example.inrussian.components.auth.login

sealed class LoginOutput {
    object NavigateToEnterEmail : LoginOutput()
    object AuthenticationSuccess : LoginOutput()
    object NavigateBack : LoginOutput()
}
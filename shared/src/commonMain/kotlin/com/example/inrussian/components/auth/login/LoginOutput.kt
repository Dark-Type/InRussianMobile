package com.example.inrussian.components.auth.login

sealed class LoginOutput {
    object NavigateToEnterEmail : LoginOutput()
    object NavigateToOnboarding : LoginOutput()
    object NavigateBack : LoginOutput()
}
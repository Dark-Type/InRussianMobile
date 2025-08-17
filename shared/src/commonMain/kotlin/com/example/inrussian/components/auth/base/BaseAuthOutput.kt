package com.example.inrussian.components.auth.base

sealed class BaseAuthOutput {
    object NavigateToLogin : BaseAuthOutput()
    object NavigateToRegister : BaseAuthOutput()
    object NavigateToVk : BaseAuthOutput()
    object NavigateToYandex : BaseAuthOutput()
}
package com.example.inrussian.components.auth.base

sealed class BaseAuthOutput {
    object NavigateToLogin : BaseAuthOutput()
    object NavigateToRegister : BaseAuthOutput()
    object NavigateToSso : BaseAuthOutput()
}
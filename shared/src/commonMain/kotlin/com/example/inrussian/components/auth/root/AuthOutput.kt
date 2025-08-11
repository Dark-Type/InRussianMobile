package com.example.inrussian.components.auth.root

sealed class AuthOutput {
    data object NavigateToOnboarding : AuthOutput()
}
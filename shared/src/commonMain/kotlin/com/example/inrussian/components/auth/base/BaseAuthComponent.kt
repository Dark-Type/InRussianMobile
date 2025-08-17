package com.example.inrussian.components.auth.base

import com.arkivanov.decompose.ComponentContext
import com.example.inrussian.repository.auth.AuthRepository

interface BaseAuthComponent {
    fun onLoginClicked()
    fun onRegisterClicked()
    fun onVkClicked()
    fun onYandexClicked()

    data class Routes(
        val onSignInClick: () -> Unit,
        val onSignUpClick: () -> Unit,
        val onSignInWithVkClick: () -> Unit,
        val onSignInWithYandexClick: () -> Unit,
    )
}

class DefaultBaseAuthComponent(
    componentContext: ComponentContext,
    private val onOutput: (BaseAuthOutput) -> Unit,
    private val authRepository: AuthRepository
) : BaseAuthComponent, ComponentContext by componentContext {

    override fun onLoginClicked() {
        onOutput(BaseAuthOutput.NavigateToLogin)
    }

    override fun onRegisterClicked() {
        onOutput(BaseAuthOutput.NavigateToRegister)
    }

    override fun onVkClicked() {
        onOutput(BaseAuthOutput.NavigateToVk)
    }

    override fun onYandexClicked() {
        onOutput(BaseAuthOutput.NavigateToYandex)
    }
}
package com.example.inrussian.components.auth.base

import com.arkivanov.decompose.ComponentContext
import com.example.inrussian.repository.auth.AuthRepository
interface BaseAuthComponent {
    fun onLoginClicked()
    fun onRegisterClicked()
    fun onSsoClicked()
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

    override fun onSsoClicked() {
        onOutput(BaseAuthOutput.NavigateToSso)
    }
}
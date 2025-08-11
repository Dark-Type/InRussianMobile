package com.example.inrussian.components.auth.login

import com.arkivanov.decompose.ComponentContext
import com.example.inrussian.repository.auth.AuthRepository

interface LoginComponent {
    fun onLogin(email: String, password: String)
    fun onForgotPasswordClicked()
    fun onBackClicked()
}

class DefaultLoginComponent(
    componentContext: ComponentContext,
    private val onOutput: (LoginOutput) -> Unit,
    private val authRepository: AuthRepository
) : LoginComponent, ComponentContext by componentContext {

    override fun onLogin(email: String, password: String) {

        onOutput(LoginOutput.AuthenticationSuccess)
    }

    override fun onForgotPasswordClicked() {
        onOutput(LoginOutput.NavigateToEnterEmail)
    }

    override fun onBackClicked() {
        onOutput(LoginOutput.NavigateBack)
    }
}
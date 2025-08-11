package com.example.inrussian.components.auth.register

import com.arkivanov.decompose.ComponentContext
import com.example.inrussian.repository.auth.AuthRepository

interface RegisterComponent {
    fun onRegister(email: String, password: String)
    fun onBackClicked()
}

sealed class RegisterOutput {
    object AuthenticationSuccess : RegisterOutput()
    object NavigateBack : RegisterOutput()
}

class DefaultRegisterComponent(
    componentContext: ComponentContext,
    private val onOutput: (RegisterOutput) -> Unit,
    private val authRepository: AuthRepository
) : RegisterComponent, ComponentContext by componentContext {

    override fun onRegister(email: String, password: String) {
        onOutput(RegisterOutput.AuthenticationSuccess)
    }

    override fun onBackClicked() {
        onOutput(RegisterOutput.NavigateBack)
    }
}
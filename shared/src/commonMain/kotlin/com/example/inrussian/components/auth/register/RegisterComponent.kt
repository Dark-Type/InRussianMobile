package com.example.inrussian.components.auth.register

import com.arkivanov.decompose.ComponentContext
import com.example.inrussian.models.state.RegisterState
import com.example.inrussian.repository.auth.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface RegisterComponent {
    fun onRegister(email: String, password: String)
    fun onBackClicked()
    fun changeEmail(email: String)
    fun changePassword(password: String)
    fun changeConfirmPassword(confirmPassword: String)
    fun onShowPasswordClick()
    fun onShowConfirmPasswordClick()
    fun onEmailDeleteClick()
    val state: StateFlow<RegisterState>
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
    override val state = MutableStateFlow(RegisterState())

    override fun onRegister(email: String, password: String) {
        onOutput(RegisterOutput.AuthenticationSuccess)
    }

    override fun onBackClicked() {
        onOutput(RegisterOutput.NavigateBack)
    }

    override fun changeEmail(email: String) {
        state.value = state.value.copy(email = email)
    }

    override fun changePassword(password: String) {
        state.value = state.value.copy(password = password)
    }

    override fun changeConfirmPassword(confirmPassword: String) {
        state.value = state.value.copy(confirmPassword = confirmPassword)

    }

    override fun onShowPasswordClick() {
        state.value = state.value.copy(showPassword = !state.value.showPassword)

    }

    override fun onShowConfirmPasswordClick() {
        state.value = state.value.copy(showConfirmPassword = !state.value.showConfirmPassword)

    }

    override fun onEmailDeleteClick() {
        state.value = state.value.copy(email = "")

    }

}
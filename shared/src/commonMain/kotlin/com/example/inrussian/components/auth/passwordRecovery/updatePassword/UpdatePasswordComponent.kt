package com.example.inrussian.components.auth.passwordRecovery.updatePassword

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.example.inrussian.models.state.UpdatePasswordState
import com.example.inrussian.repository.auth.AuthRepository

interface UpdatePasswordComponent {
    fun onPasswordUpdated()
    fun onBackClicked()
    fun onPasswordChange(password: String)
    fun onConfirmPasswordChange(confirmPassword: String)
    fun onShowPasswordClick()
    fun onShowConfirmPasswordClick()
    val state: Value<UpdatePasswordState>
}

sealed class UpdatePasswordOutput {
    object PasswordUpdated : UpdatePasswordOutput()
    object NavigateBack : UpdatePasswordOutput()
}

class DefaultUpdatePasswordComponent(
    componentContext: ComponentContext,
    private val onOutput: (UpdatePasswordOutput) -> Unit,
    private val authRepository: AuthRepository
) : UpdatePasswordComponent, ComponentContext by componentContext {

    private val _state = MutableValue(UpdatePasswordState())
    override val state: Value<UpdatePasswordState> get() = _state

    override fun onPasswordUpdated() {
        onOutput(UpdatePasswordOutput.PasswordUpdated)
    }

    override fun onBackClicked() {
        onOutput(UpdatePasswordOutput.NavigateBack)
    }

    override fun onPasswordChange(password: String) {
        val confirmPassword = _state.value.confirmPassword
        val enable = password.length >= 6 && password == confirmPassword && confirmPassword.isNotEmpty()
        _state.value = _state.value.copy(
            password = password,
            updateButtonEnable = enable
        )
    }

    override fun onConfirmPasswordChange(confirmPassword: String) {
        val password = _state.value.password
        val error = if (password.length < 6) "Пароль должен быть не менее 6 символов" else null
        val enable = password.length >= 6 && password == confirmPassword && confirmPassword.isNotEmpty()
        _state.value = _state.value.copy(
            confirmPassword = confirmPassword,
            confirmPasswordError = error,
            updateButtonEnable = enable
        )
        onOutput(UpdatePasswordOutput.PasswordUpdated)
    }
    override fun onShowPasswordClick() {
        _state.value = _state.value.copy(showPassword = !_state.value.showPassword)
    }

    override fun onShowConfirmPasswordClick() {
        _state.value = _state.value.copy(showConfirmPassword = !_state.value.showConfirmPassword)
    }
}
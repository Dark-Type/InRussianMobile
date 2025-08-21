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

    override fun onPasswordUpdated() {
        onOutput(UpdatePasswordOutput.PasswordUpdated)
    }

    override fun onBackClicked() {
        onOutput(UpdatePasswordOutput.NavigateBack)
    }

    override fun onPasswordChange(password: String) {
        TODO("Not yet implemented")
    }

    override fun onConfirmPasswordChange(confirmPassword: String) {
        TODO("Not yet implemented")
    }

    override fun onShowPasswordClick() {
        TODO("Not yet implemented")
    }

    override fun onShowConfirmPasswordClick() {
        TODO("Not yet implemented")
    }

    private val _state = MutableValue(UpdatePasswordState())
    override val state: Value<UpdatePasswordState> get() = _state
}
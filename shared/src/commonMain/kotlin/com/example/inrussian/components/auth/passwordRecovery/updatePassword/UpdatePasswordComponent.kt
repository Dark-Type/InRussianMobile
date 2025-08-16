package com.example.inrussian.components.auth.passwordRecovery.updatePassword

import com.arkivanov.decompose.ComponentContext
import com.example.inrussian.models.UpdatePasswordState
import com.example.inrussian.repository.auth.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface UpdatePasswordComponent {
    fun onPasswordUpdated()
    fun onBackClicked()
    fun onPasswordChange(password: String)
    fun onConfirmPasswordChange(confirmPassword: String)
    fun onShowPasswordClick()
    fun onShowConfirmPasswordClick()
    val state: StateFlow<UpdatePasswordState>
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

    override val state = MutableStateFlow(UpdatePasswordState())
}
package com.example.inrussian.components.auth.passwordRecovery.updatePassword

import com.arkivanov.decompose.ComponentContext
import com.example.inrussian.repository.auth.AuthRepository

interface UpdatePasswordComponent{
    fun onPasswordUpdated(newPassword: String)
    fun onBackClicked()
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

    override fun onPasswordUpdated(newPassword: String) {
        onOutput(UpdatePasswordOutput.PasswordUpdated)
    }

    override fun onBackClicked() {
        onOutput(UpdatePasswordOutput.NavigateBack)
    }
}
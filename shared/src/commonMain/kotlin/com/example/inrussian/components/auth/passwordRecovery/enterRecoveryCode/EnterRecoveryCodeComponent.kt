package com.example.inrussian.components.auth.passwordRecovery.enterRecoveryCode

import com.arkivanov.decompose.ComponentContext
import com.example.inrussian.repository.auth.AuthRepository

interface EnterRecoveryCodeComponent {
    fun onCodeEntered(code: String)
    fun onBackClicked()
}

sealed class EnterRecoveryCodeOutput {
    object NavigateToUpdatePassword : EnterRecoveryCodeOutput()
    object NavigateBack : EnterRecoveryCodeOutput()
}
class DefaultEnterRecoveryCodeComponent(
    componentContext: ComponentContext,
    private val onOutput: (EnterRecoveryCodeOutput) -> Unit,
    private val authRepository: AuthRepository
) : EnterRecoveryCodeComponent, ComponentContext by componentContext {

    override fun onCodeEntered(code: String) {
        onOutput(EnterRecoveryCodeOutput.NavigateToUpdatePassword)
    }

    override fun onBackClicked() {
        onOutput(EnterRecoveryCodeOutput.NavigateBack)
    }
}
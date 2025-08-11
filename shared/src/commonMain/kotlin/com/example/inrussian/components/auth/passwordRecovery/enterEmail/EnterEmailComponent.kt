package com.example.inrussian.components.auth.passwordRecovery.enterEmail

import com.arkivanov.decompose.ComponentContext
import com.example.inrussian.repository.auth.AuthRepository

interface EnterEmailComponent{
    fun onEmailEntered(email: String)
    fun onBackClicked()
}

class DefaultEnterEmailComponent(
    componentContext: ComponentContext,
    private val onOutput: (EnterEmailOutput) -> Unit,
    private val authRepository: AuthRepository
) : EnterEmailComponent, ComponentContext by componentContext {

    override fun onEmailEntered(email: String) {

        onOutput(EnterEmailOutput.NavigateToRecoveryCode)
    }

    override fun onBackClicked() {
        onOutput(EnterEmailOutput.NavigateBack)
    }
}
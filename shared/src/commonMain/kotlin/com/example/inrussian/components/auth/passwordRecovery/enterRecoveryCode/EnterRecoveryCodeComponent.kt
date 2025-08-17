package com.example.inrussian.components.auth.passwordRecovery.enterRecoveryCode

import com.arkivanov.decompose.ComponentContext
import com.example.inrussian.models.state.EnterRecoveryCodeState
import com.example.inrussian.repository.auth.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface EnterRecoveryCodeComponent {
    val state: StateFlow<EnterRecoveryCodeState>

    fun onCodeEntered(code: String)
    fun onBackClicked()

    fun onQuestionClick()

    fun emailChange(email: String)
    fun codeChange(code: String)

    fun onMissClick()

    fun onSupportContactClick()
}

sealed class EnterRecoveryCodeOutput {
    object NavigateToUpdatePassword : EnterRecoveryCodeOutput()
    object NavigateBack : EnterRecoveryCodeOutput()
}

class DefaultEnterRecoveryCodeComponent(
    componentContext: ComponentContext,
    private val onOutput: (EnterRecoveryCodeOutput) -> Unit,
    private val authRepository: AuthRepository,
) : EnterRecoveryCodeComponent, ComponentContext by componentContext {
    override val state = MutableStateFlow<EnterRecoveryCodeState>(
        EnterRecoveryCodeState(
            email = "",
            code = "",
            timer = "",
            sendButtonEnable = false,
            showHint = false
        )
    )

    override fun onCodeEntered(code: String) {
        onOutput(EnterRecoveryCodeOutput.NavigateToUpdatePassword)
    }

    override fun onBackClicked() {
        onOutput(EnterRecoveryCodeOutput.NavigateBack)
    }

    override fun onQuestionClick() {
        TODO("Not yet implemented")
    }

    override fun emailChange(email: String) {
        TODO("Not yet implemented")
    }

    override fun codeChange(code: String) {
        TODO("Not yet implemented")
    }

    override fun onMissClick() {
        TODO("Not yet implemented")
    }

    override fun onSupportContactClick() {
        TODO("Not yet implemented")
    }

}
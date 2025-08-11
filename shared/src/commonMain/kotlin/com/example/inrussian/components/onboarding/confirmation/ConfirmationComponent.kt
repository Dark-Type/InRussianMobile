package com.example.inrussian.components.onboarding.confirmation

interface ConfirmationComponent {
    fun onConfirm()
}

sealed class ConfirmationOutput {
    object Confirmed : ConfirmationOutput()
}

class DefaultConfirmationComponent(
    private val onOutput: (ConfirmationOutput) -> Unit
) : ConfirmationComponent {
    override fun onConfirm() {
        onOutput(ConfirmationOutput.Confirmed)
    }

}
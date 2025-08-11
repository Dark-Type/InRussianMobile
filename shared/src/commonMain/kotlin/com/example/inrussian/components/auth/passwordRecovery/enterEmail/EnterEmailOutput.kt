package com.example.inrussian.components.auth.passwordRecovery.enterEmail

sealed class EnterEmailOutput {
    object NavigateToRecoveryCode : EnterEmailOutput()
    object NavigateBack : EnterEmailOutput()
}
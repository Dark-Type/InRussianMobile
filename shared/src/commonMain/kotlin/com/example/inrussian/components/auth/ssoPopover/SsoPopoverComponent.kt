package com.example.inrussian.components.auth.ssoPopover

import com.arkivanov.decompose.ComponentContext

interface SsoPopoverComponent {
    fun onAuthenticationSuccess()
    fun onBackClicked()
}

sealed class SsoPopoverOutput {
    object AuthenticationSuccess : SsoPopoverOutput()
    object NavigateBack : SsoPopoverOutput()
}

class DefaultSsoPopoverComponent(
    componentContext: ComponentContext,
    private val onOutput: (SsoPopoverOutput) -> Unit
) : SsoPopoverComponent, ComponentContext by componentContext {

    override fun onAuthenticationSuccess() {
        onOutput(SsoPopoverOutput.AuthenticationSuccess)
    }

    override fun onBackClicked() {
        onOutput(SsoPopoverOutput.NavigateBack)
    }
}
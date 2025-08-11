package com.example.inrussian.components.onboarding.language

import com.arkivanov.decompose.ComponentContext

interface LanguageComponent {
    fun onNext()
    val state: State

    data class State(
        val selectedLanguage: String = "",
        val hasGivenPermission: Boolean = false
    )
}

sealed class LanguageOutput {
    object Filled : LanguageOutput()
    object Back : LanguageOutput()
}

class DefaultLanguageComponent(
    componentContext: ComponentContext,
    private val onOutput: (LanguageOutput) -> Unit
) : LanguageComponent, ComponentContext by componentContext {

    override val state = LanguageComponent.State(selectedLanguage = "RUSSIAN", hasGivenPermission = false)

    override fun onNext() {
        onOutput(LanguageOutput.Filled)
    }
}
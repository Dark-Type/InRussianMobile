package com.example.inrussian.components.onboarding.language

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface LanguageComponent {
    fun onNext()
    fun openMenu()
    fun closeMenu()
    fun selectLanguage(language: String)
    fun onBack()
    fun clickOnToggleButton(isSelected: Boolean)
    val state: StateFlow<State>

    data class State(
        val selectedLanguage: String = "",
        val hasGivenPermission: Boolean = false,
        val isOpenLanguage: Boolean = false
    ) {
        val isActiveContinueButton = hasGivenPermission && selectedLanguage.isNotBlank()
    }
}

sealed class LanguageOutput {
    object Filled : LanguageOutput()
    object Back : LanguageOutput()
}

class DefaultLanguageComponent(
    componentContext: ComponentContext,
    private val onOutput: (LanguageOutput) -> Unit
) : LanguageComponent, ComponentContext by componentContext {

    override val state =
        MutableStateFlow(
            LanguageComponent.State(selectedLanguage = "RUSSIAN", hasGivenPermission = false)
        )

    override fun onNext() {
        onOutput(LanguageOutput.Filled)
    }

    override fun onBack() {
        onOutput(LanguageOutput.Back)
    }

    override fun openMenu() {
        state.value = state.value.copy(isOpenLanguage = !state.value.isOpenLanguage)
    }

    override fun closeMenu() {
        TODO("Not yet implemented")
    }

    override fun selectLanguage(language: String) {
        TODO("Not yet implemented")
    }

    override fun clickOnToggleButton(isSelected: Boolean) {
        state.value = state.value.copy(hasGivenPermission = isSelected)
    }

}
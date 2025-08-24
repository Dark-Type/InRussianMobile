package com.example.inrussian.components.onboarding.language

import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.ComponentContext

interface LanguageComponent {
    fun onNext()
    fun openMenu()
    fun closeMenu()
    fun selectLanguage(language: String)
    fun onBack()
    fun clickOnToggleButton(isSelected: Boolean)
    val state: Value<State>

    data class State(
        val selectedLanguage: String = "",
        val hasGivenPermission: Boolean = false,
        val isOpenLanguage: Boolean = false
    ) {
        val isActiveContinueButton: Boolean
            get() = hasGivenPermission && selectedLanguage.isNotBlank()
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

    private val _state = MutableValue(
        LanguageComponent.State(selectedLanguage = "RUSSIAN", hasGivenPermission = false)
    )
    override val state: Value<LanguageComponent.State> get() = _state

    override fun onNext() {
        onOutput(LanguageOutput.Filled)
    }

    override fun onBack() {
        onOutput(LanguageOutput.Back)
    }

    override fun openMenu() {
        _state.value = _state.value.copy(isOpenLanguage = !_state.value.isOpenLanguage)
    }

    override fun closeMenu() {
        _state.value = _state.value.copy(isOpenLanguage = false)
    }

    override fun selectLanguage(language: String) {
        _state.value = _state.value.copy(selectedLanguage = language)
    }

    override fun clickOnToggleButton(isSelected: Boolean) {
        _state.value = _state.value.copy(hasGivenPermission = isSelected)
    }
}
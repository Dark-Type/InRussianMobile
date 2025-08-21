package com.example.inrussian.components.onboarding.education

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value

interface EducationComponent {
    val state: Value<State>

    data class State(
        val languages: List<String> = listOf(),
        val understandsRussian: Boolean = false,
        val speaksRussian: Boolean = false,
        val readsRussian: Boolean = false,
        val writesRussian: Boolean = false,
        val kindOfActivity: String = "",
        val education: String = "",
        val purposeOfRegistration: String = "",
        val isOpenLanguages: Boolean = false
    ) {
        val continueEnable: Boolean
            get() = languages.isNotEmpty() && kindOfActivity.isNotBlank() && education.isNotBlank() && purposeOfRegistration.isNotBlank()
    }

    fun onNext()
    fun onBack()

    fun deleteLanguage(string: String)

    fun onChangeExpanded(boolean: Boolean)

    fun selectLanguage(language: String)

    fun changeActivity(activity: String)
    fun changeEducation(education: String)
    fun changePurpose(purpose: String)
}

sealed class EducationOutput {
    object Filled : EducationOutput()
    object Back : EducationOutput()
}

class DefaultEducationComponent(
    private val onOutput: (EducationOutput) -> Unit
) : EducationComponent {

    private val _state = MutableValue(
        EducationComponent.State(
            languages = listOf(),
            understandsRussian = false,
            speaksRussian = false,
            readsRussian = false,
            writesRussian = false,
            kindOfActivity = "",
            education = "",
            purposeOfRegistration = "",
            isOpenLanguages = false
        )
    )
    override val state: Value<EducationComponent.State> get() = _state

    override fun onNext() {
        onOutput(EducationOutput.Filled)
    }

    override fun onBack() {
        onOutput(EducationOutput.Back)
    }

    override fun deleteLanguage(string: String) {
        _state.value = _state.value.copy(languages = _state.value.languages.filter { it != string })
    }

    override fun onChangeExpanded(boolean: Boolean) {
        _state.value = _state.value.copy(isOpenLanguages = boolean)
    }

    override fun selectLanguage(language: String) {
        _state.value = _state.value.copy(
            languages = _state.value.languages.toMutableList().apply { add(language) }
        )
    }

    override fun changeActivity(activity: String) {
        _state.value = _state.value.copy(kindOfActivity = activity)
    }

    override fun changeEducation(education: String) {
        _state.value = _state.value.copy(education = education)
    }

    override fun changePurpose(purpose: String) {
        _state.value = _state.value.copy(purposeOfRegistration = purpose)
    }
}
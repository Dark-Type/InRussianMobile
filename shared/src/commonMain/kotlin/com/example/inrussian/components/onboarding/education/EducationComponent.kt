package com.example.inrussian.components.onboarding.education

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface EducationComponent {
    val state: StateFlow<State>

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

    override val state = MutableStateFlow(
        EducationComponent.State(
            languages = mutableListOf(),
            understandsRussian = false,
            speaksRussian = false,
            readsRussian = false,
            writesRussian = false,
            kindOfActivity = "",
            education = "",
            purposeOfRegistration = ""
        )
    )

    override fun onNext() {
        onOutput(EducationOutput.Filled)
    }

    override fun onBack() {
        onOutput(EducationOutput.Back)
    }

    override fun deleteLanguage(string: String) {
        state.value = state.value.copy(languages = state.value.languages.filter { it != string })

    }

    override fun onChangeExpanded(boolean: Boolean) {
        state.value = state.value.copy(isOpenLanguages = boolean)
    }

    override fun selectLanguage(language: String) {
        state.value = state.value.copy(
            languages = state.value.languages.toMutableList().apply { add(language) })
    }

    override fun changeActivity(activity: String) {
        state.value = state.value.copy(kindOfActivity = activity)
    }

    override fun changeEducation(education: String) {
        state.value = state.value.copy(education = education)

    }

    override fun changePurpose(purpose: String) {
        state.value = state.value.copy(purposeOfRegistration = purpose)

    }
}
package com.example.inrussian.components.onboarding.education

interface EducationComponent {
    val state: State

    data class State(
        val languages: MutableList<String> = mutableListOf(),
        val understandsRussian: Boolean = false,
        val speaksRussian: Boolean = false,
        val readsRussian: Boolean = false,
        val writesRussian: Boolean = false,
        val kindOfActivity: String = "",
        val education: String = "",
        val purposeOfRegistration: String = "",
    ) {
        val continueEnable: Boolean
            get() = languages.isNotEmpty() &&
                    kindOfActivity.isNotBlank() &&
                    education.isNotBlank() &&
                    purposeOfRegistration.isNotBlank()
    }

    fun onNext()
    fun onBack()
}

sealed class EducationOutput {
    object Filled : EducationOutput()
    object Back : EducationOutput()
}

class DefaultEducationComponent(
    private val onOutput: (EducationOutput) -> Unit
) : EducationComponent {

    override val state = EducationComponent.State(
        languages = mutableListOf(),
        understandsRussian = false,
        speaksRussian = false,
        readsRussian = false,
        writesRussian = false,
        kindOfActivity = "",
        education = "",
        purposeOfRegistration = ""
    )

    override fun onNext() {
        onOutput(EducationOutput.Filled)
    }

    override fun onBack() {
        onOutput(EducationOutput.Back)
    }
}
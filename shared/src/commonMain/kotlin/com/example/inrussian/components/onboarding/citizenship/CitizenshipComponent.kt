package com.example.inrussian.components.onboarding.citizenship

interface CitizenshipComponent {
    val state: State

    data class State(
        val citizenship: MutableList<String> = mutableListOf(),
        val nationality: String = "",
        val countryOfResidence: String = "",
        val cityOfResidence: String = "",
        val countryDuringEducation: String = "",
        val timeSpentInRussia: String = "",
    )
    fun onNext()
    fun onBack()
}

sealed class CitizenshipOutput {
    object Filled : CitizenshipOutput()
    object Back : CitizenshipOutput()
}

class DefaultCitizenshipComponent(
    private val onOutput: (CitizenshipOutput) -> Unit
) : CitizenshipComponent {

    override val state = CitizenshipComponent.State(
        citizenship = mutableListOf(),
        nationality = "",
        countryOfResidence = "",
        cityOfResidence = "",
        countryDuringEducation = "",
        timeSpentInRussia = ""
    )

    override fun onNext() {
        onOutput(CitizenshipOutput.Filled)
    }

    override fun onBack() {
        onOutput(CitizenshipOutput.Back)
    }
}
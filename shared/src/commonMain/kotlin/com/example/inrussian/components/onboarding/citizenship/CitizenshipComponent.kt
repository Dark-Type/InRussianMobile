package com.example.inrussian.components.onboarding.citizenship

interface CitizenshipComponent {
    val state: State

    data class State(
        val expanded: Boolean = false,
        val selectedCountry: String? = null,
        val citizenship: MutableList<String> = mutableListOf(),
        val nationality: String = "",
        val countryOfResidence: String = "",
        val cityOfResidence: String = "",
        val countryDuringEducation: String = "",
        val timeSpentInRussia: String = "",
        val selectedTime: String = ""
    ){
        val continueEnable: Boolean get() = citizenship.isNotEmpty() && nationality.isNotBlank() && countryOfResidence.isNotBlank()
    }

    fun onNext()
    fun onBack()

    fun selectCountry(country: String)
    fun onChangeExpanded(expanded: Boolean)

    fun selectNationality(string: String)

    fun deleteCountry(country: String)

    fun selectTime(time: String)
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

    override fun selectCountry(country: String) {

    }

    override fun onChangeExpanded(expanded: Boolean) {

    }

    override fun selectNationality(string: String) {
        TODO("Not yet implemented")
    }

    override fun deleteCountry(country: String) {
        TODO("Not yet implemented")
    }

    override fun selectTime(time: String) {
        TODO("Not yet implemented")
    }
}
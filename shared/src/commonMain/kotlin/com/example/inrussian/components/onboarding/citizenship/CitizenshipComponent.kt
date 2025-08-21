package com.example.inrussian.components.onboarding.citizenship

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface CitizenshipComponent {
    val state: StateFlow<State>

    data class State(
        val selectedCountry: String? = null,
        val citizenship: MutableList<String> = mutableListOf(),
        val nationality: String = "",
        val countryOfResidence: String = "",
        val cityOfResidence: String = "",
        val countryDuringEducation: String = "",
        val timeSpentInRussia: String = "",
        val selectedTime: String = "",
        val isCitizenshipOpen: Boolean = false,
        val isNationalityOpen: Boolean = false,
        val isTimeOpen: Boolean = false,
    ) {
        val continueEnable: Boolean get() = citizenship.isNotEmpty() && nationality.isNotBlank() && countryOfResidence.isNotBlank()
    }

    fun onNext()
    fun onBack()
    fun countryLiveChange(country: String)
    fun cityLiveChange(city: String)
    fun studyCountyChange(country: String)
    fun selectCountry(country: String)
    fun openCitizenship(isOpen: Boolean)
    fun openNationality(isOpen: Boolean)
    fun openTime(isOpen: Boolean)
    fun selectNationality(string: String)

    fun deleteCountry(country: String)

    fun selectTime(time: String)

    fun deleteNationality(nationality: String)

    fun deleteTime(time: String)
}

sealed class CitizenshipOutput {
    object Filled : CitizenshipOutput()
    object Back : CitizenshipOutput()
}

class DefaultCitizenshipComponent(
    private val onOutput: (CitizenshipOutput) -> Unit
) : CitizenshipComponent {

    override val state = MutableStateFlow(
        CitizenshipComponent.State(
            citizenship = mutableListOf(),
            nationality = "",
            countryOfResidence = "",
            cityOfResidence = "",
            countryDuringEducation = "",
            timeSpentInRussia = ""
        )
    )

    override fun onNext() {
        onOutput(CitizenshipOutput.Filled)
    }

    override fun onBack() {
        onOutput(CitizenshipOutput.Back)
    }

    override fun countryLiveChange(country: String) {
        state.value = state.value.copy(countryOfResidence = country)
    }

    override fun cityLiveChange(city: String) {
        state.value = state.value.copy(cityOfResidence = city)

    }

    override fun studyCountyChange(country: String) {
        state.value = state.value.copy(countryDuringEducation = country)
    }

    override fun selectCountry(country: String) {
        state.value = state.value.copy(citizenship = state.value.citizenship.apply { add(country) })
    }

    override fun openCitizenship(isOpen: Boolean) {
        state.value = state.value.copy(isCitizenshipOpen = isOpen)
    }

    override fun openNationality(isOpen: Boolean) {
        state.value = state.value.copy(isNationalityOpen = isOpen)
    }

    override fun openTime(isOpen: Boolean) {
        state.value = state.value.copy(isTimeOpen = isOpen)

    }

    override fun selectNationality(string: String) {
        state.value = state.value.copy(nationality = string)
    }

    override fun deleteCountry(country: String) {
        state.value =
            state.value.copy(citizenship = state.value.citizenship.filter { it != country }
                .toMutableList())
    }

    override fun selectTime(time: String) {
        state.value = state.value.copy(timeSpentInRussia = time)
    }

    override fun deleteNationality(nationality: String) {
        state.value = state.value.copy(nationality = "")
    }

    override fun deleteTime(time: String) {
        state.value = state.value.copy(timeSpentInRussia = "")
    }
}
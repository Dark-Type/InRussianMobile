package com.example.inrussian.components.onboarding.citizenship

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.example.inrussian.stores.auth.register.RegisterStore
import com.example.inrussian.stores.auth.register.RegisterStore.Intent

interface CitizenshipComponent {
    val state: Value<State>

    data class State(
        val selectedCountry: String? = null,
        val citizenship: List<String> = listOf(),
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
    private val onOutput: (CitizenshipOutput) -> Unit,
    private val store: RegisterStore,
) : CitizenshipComponent {

    private val _state = MutableValue(
        CitizenshipComponent.State(
            citizenship = listOf(),
            nationality = "",
            countryOfResidence = "",
            cityOfResidence = "",
            countryDuringEducation = "",
            timeSpentInRussia = ""
        )
    )
    override val state: Value<CitizenshipComponent.State> get() = _state

    override fun onNext() {
        store.accept(Intent.UpdateCitizenship(state = state.value))
        onOutput(CitizenshipOutput.Filled)
    }

    override fun onBack() {
        onOutput(CitizenshipOutput.Back)
    }

    override fun countryLiveChange(country: String) {
        _state.value = _state.value.copy(countryOfResidence = country)
    }

    override fun cityLiveChange(city: String) {
        _state.value = _state.value.copy(cityOfResidence = city)
    }

    override fun studyCountyChange(country: String) {
        _state.value = _state.value.copy(countryDuringEducation = country)
    }

    override fun selectCountry(country: String) {
        _state.value = _state.value.copy(
            citizenship = _state.value.citizenship.toMutableList().apply { add(country) }
        )
    }

    override fun openCitizenship(isOpen: Boolean) {
        _state.value = _state.value.copy(isCitizenshipOpen = isOpen)
    }

    override fun openNationality(isOpen: Boolean) {
        _state.value = _state.value.copy(isNationalityOpen = isOpen)
    }

    override fun openTime(isOpen: Boolean) {
        _state.value = _state.value.copy(isTimeOpen = isOpen)
    }

    override fun selectNationality(string: String) {
        _state.value = _state.value.copy(nationality = string)
    }

    override fun deleteCountry(country: String) {
        _state.value = _state.value.copy(
            citizenship = _state.value.citizenship.filter { it != country }
        )
    }

    override fun selectTime(time: String) {
        _state.value = _state.value.copy(timeSpentInRussia = time)
    }

    override fun deleteNationality(nationality: String) {
        _state.value = _state.value.copy(nationality = "")
    }

    override fun deleteTime(time: String) {
        _state.value = _state.value.copy(timeSpentInRussia = "")
    }
}
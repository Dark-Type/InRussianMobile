package com.example.inrussian.components.onboarding.personalData

import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.MutableValue

interface PersonalDataComponent {
    fun onNext()

    val state: Value<State>

    data class State(
        val name: String = "",
        val surname: String = "",
        val patronymic: String = "",
        val gender: String = "",
        val birthDate: String = "",
        val phoneNumber: String = "",
        val email: String = "",
        val isGenderOpen: Boolean = false,
        val showDataPicker: Boolean = false,
    ) {
        val isEnableContinueButton: Boolean
            get() = surname.isNotEmpty() &&
                    name.isNotEmpty() &&
                    gender.isNotEmpty() &&
                    birthDate.isNotEmpty() &&
                    phoneNumber.isNotEmpty() &&
                    email.isNotEmpty()
    }

    fun changeSurname(surname: String)
    fun changeName(name: String)
    fun changeThirdName(thirdName: String)
    fun changeGender(gender: String)
    fun changeDob(dob: String)
    fun changePhone(phone: String)
    fun changeEmail(email: String)
    fun changeGenderChoose(isOpen: Boolean)
    fun openDataPicker()
    fun onDataChange(date: String)
    fun dataPickerMissClick()
    fun onBack()
    fun onContinue()
}

sealed class PersonalDataOutput {
    object Filled : PersonalDataOutput()
    object Back : PersonalDataOutput()
}

class DefaultPersonalDataComponent(
    private val onOutput: (PersonalDataOutput) -> Unit
) : PersonalDataComponent {

    private val _state = MutableValue(
        PersonalDataComponent.State(
            name = "",
            surname = "",
            patronymic = "",
            gender = "",
            birthDate = "",
            phoneNumber = "",
            email = ""
        )
    )
    override val state: Value<PersonalDataComponent.State> get() = _state

    override fun changeSurname(surname: String) {
        _state.value = _state.value.copy(surname = surname)
    }

    override fun changeName(name: String) {
        _state.value = _state.value.copy(name = name)
    }

    override fun changeThirdName(thirdName: String) {
        _state.value = _state.value.copy(patronymic = thirdName)
    }

    override fun changeGender(gender: String) {
        _state.value = _state.value.copy(gender = gender)
    }

    override fun changeDob(dob: String) {
        _state.value = _state.value.copy(birthDate = dob)
    }

    override fun changePhone(phone: String) {
        _state.value = _state.value.copy(phoneNumber = phone)
    }

    override fun changeEmail(email: String) {
        _state.value = _state.value.copy(email = email)
    }

    override fun changeGenderChoose(isOpen: Boolean) {
        _state.value = _state.value.copy(isGenderOpen = isOpen)
    }

    override fun openDataPicker() {
        _state.value = _state.value.copy(showDataPicker = true)
    }

    override fun onDataChange(date: String) {
        _state.value = _state.value.copy(birthDate = date)
    }

    override fun dataPickerMissClick() {
        _state.value = _state.value.copy(showDataPicker = false)
    }

    override fun onBack() {
        onOutput(PersonalDataOutput.Back)
    }

    override fun onContinue() {
        onOutput(PersonalDataOutput.Filled)
    }

    override fun onNext() {
        onOutput(PersonalDataOutput.Filled)
    }
}
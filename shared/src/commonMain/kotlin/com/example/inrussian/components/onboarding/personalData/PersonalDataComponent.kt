package com.example.inrussian.components.onboarding.personalData

interface PersonalDataComponent {
    fun onNext()

    val state: StateFlow<State>

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

    override val state = MutableStateFlow(
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

    override fun changeSurname(surname: String) {
        state.value = state.value.copy(surname = surname)
    }

    override fun changeName(name: String) {
        state.value = state.value.copy(name = name)

    }

    override fun changeThirdName(thirdName: String) {
        state.value = state.value.copy(patronymic = thirdName)

    }

    override fun changeGender(gender: String) {
        state.value = state.value.copy(gender = gender)

    }

    override fun changeDob(dob: String) {
        state.value = state.value.copy(birthDate = dob)

    }

    override fun changePhone(phone: String) {
        state.value = state.value.copy(phoneNumber = phone)

    }

    override fun changeEmail(email: String) {
        state.value = state.value.copy(email = email)

    }

    override fun changeGenderChoose(isOpen: Boolean) {
        state.value = state.value.copy(isGenderOpen = isOpen)
    }

    override fun openDataPicker() {
        state.value = state.value.copy(showDataPicker = true)
    }

    override fun onDataChange(date: String) {
        state.value = state.value.copy(birthDate = date)
    }

    override fun dataPickerMissClick() {
        state.value = state.value.copy(showDataPicker = false)
    }

    override fun onBack() {
        onOutput(PersonalDataOutput.Back)
    }

    override fun onNext() {
        onOutput(PersonalDataOutput.Filled)
    }
}
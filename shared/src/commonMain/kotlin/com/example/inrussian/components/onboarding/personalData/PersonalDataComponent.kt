package com.example.inrussian.components.onboarding.personalData

interface PersonalDataComponent {
    fun onNext()
    val state: State

    data class State(
        val name: String = "",
        val surname: String = "",
        val patronymic: String = "",
        val gender: String = "",
        val birthDate: String = "",
        val phoneNumber: String = "",
        val email: String = "",
    )
}

sealed class PersonalDataOutput {
    object Filled : PersonalDataOutput()
    object Back : PersonalDataOutput()
}

class DefaultPersonalDataComponent(
    private val onOutput: (PersonalDataOutput) -> Unit
) : PersonalDataComponent {

    override val state = PersonalDataComponent.State(
        name = "",
        surname = "",
        patronymic = "",
        gender = "",
        birthDate = "",
        phoneNumber = "",
        email = ""
    )

    override fun onNext() {
        onOutput(PersonalDataOutput.Filled)
    }
}
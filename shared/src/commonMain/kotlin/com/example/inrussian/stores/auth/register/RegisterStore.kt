package com.example.inrussian.stores.auth.register

import com.arkivanov.mvikotlin.core.store.Store
import com.example.inrussian.models.ErrorKey
import com.example.inrussian.components.onboarding.citizenship.CitizenshipComponent
import com.example.inrussian.components.onboarding.education.EducationComponent
import com.example.inrussian.components.onboarding.language.LanguageComponent
import com.example.inrussian.components.onboarding.personalData.PersonalDataComponent
import dev.icerock.moko.resources.StringResource
import kotlinx.serialization.Serializable

interface RegisterStore : Store<RegisterStore.Intent, RegisterStore.State, RegisterStore.Label> {
    sealed class Intent {
        data object SignUpClick : Intent()
        data class EmailChange(val email: String) : Intent()
        data class PasswordChange(val password: String) : Intent()
        data class ConfirmPasswordChange(val password: String) : Intent()
        data object PasswordImageClick : Intent()
        data object ConfirmPasswordImageClick : Intent()
        data object EmailImageClick : Intent()
        data class UpdateCitizenship(val state: CitizenshipComponent.State) : Intent()
        data class UpdateEducation(val state: EducationComponent.State) : Intent()
        data class UpdateLanguage(val state: LanguageComponent.State) : Intent()
        data class UpdatePersonalData(val state: PersonalDataComponent.State) : Intent()
    }

    data class State(
        val email: String = "",
        val emailError: StringResource? = null,
        val password: String = "",
        val phone: String = "",
        val showPassword: Boolean = false,
        val passwordError: StringResource? = null,
        val confirmPassword: String = "",
        val showConfirmPassword: Boolean = false,
        val confirmPasswordError: StringResource? = null,
        val languageState: LanguageComponent.State? = null,
        val citizenshipState: CitizenshipComponent.State? = null,
        val educationState: EducationComponent.State? = null,
        val personalDataState: PersonalDataComponent.State? = null,
        val loading: Boolean = false
    ) {
        val isButtonActive: Boolean
            get() = email.isNotBlank() && emailError == null &&
                    password.isNotBlank() && passwordError == null &&
                    confirmPassword.isNotBlank() && confirmPasswordError == null &&
                    password == confirmPassword
    }

    sealed interface Action {}

    sealed interface Msg {
        data object Loading : Msg
        data object FinishLoading : Msg
        data class EmailChanged(val email: String) : Msg
        data class PasswordChanged(val password: String) : Msg
        data class ConfirmPasswordChanged(val password: String) : Msg
        data class EmailError(val messageId: StringResource) : Msg
        data class PasswordError(val messageId: StringResource) : Msg
        data class ConfirmPasswordError(val messageId: StringResource) : Msg
        data class UpdateCitizenship(val state: CitizenshipComponent.State) : Msg
        data class UpdateEducation(val state: EducationComponent.State) : Msg
        data class UpdateLanguage(val state: LanguageComponent.State) : Msg
        data class UpdatePersonalData(val state: PersonalDataComponent.State) : Msg
        data object PasswordTransform : Msg
        data object ConfirmPasswordTransform : Msg
        data object DeleteEmail : Msg
        data object Confirm : Msg

    }

    sealed interface Label {
        data object SubmittedSuccessfully : Label
    }


}
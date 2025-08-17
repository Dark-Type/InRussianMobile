package com.example.inrussian.stores.auth.register

import com.arkivanov.mvikotlin.core.store.Store
import com.example.inrussian.models.ErrorKey
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.StringResource

interface RegisterStore : Store<RegisterStore.Intent, RegisterStore.State, RegisterStore.Label> {
    sealed class Intent {
        data object SignUpClick : Intent()
        data class EmailChange(val email: String) : Intent()
        data class PasswordChange(val password: String) : Intent()
        data class ConfirmPasswordChange(val password: String) : Intent()
        data object PasswordImageClick : Intent()
        data object ConfirmPasswordImageClick : Intent()
        data object EmailImageClick : Intent()
    }

    data class State(
        val email: String = "",
        val emailError: StringResource? = null,
        val password: String = "",
        val showPassword: Boolean = false,
        val passwordError: StringResource? = null,
        val confirmPassword: String = "",
        val showConfirmPassword: Boolean = false,
        val confirmPasswordError: StringResource? = null,
        val loading: Boolean = false
    ) {
        val isButtonActive: Boolean
            get() = email.isNotBlank() && emailError == null &&
                    password.isNotBlank() && passwordError == null &&
                    confirmPassword.isNotBlank() && confirmPasswordError == null
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
        data object PasswordTransform : Msg
        data object ConfirmPasswordTransform : Msg
        data object DeleteEmail : Msg
        data object Confirm : Msg

    }

    sealed interface Label {
        data object SubmittedSuccessfully : Label
    }


    @Serializable
    data class PersistableState(
        val email: String = "",
        val password: String = "",
        val showPassword: Boolean = false,
        val confirmPassword: String = "",
        val showConfirmPassword: Boolean = false,
        val emailErrorKey: ErrorKey? = null,
        val passwordErrorKey: ErrorKey? = null,
        val confirmPasswordErrorKey: ErrorKey? = null,
        val loading: Boolean = false
    )

}
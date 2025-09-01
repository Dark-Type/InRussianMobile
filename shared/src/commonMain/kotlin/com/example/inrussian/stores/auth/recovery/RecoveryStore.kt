package com.example.inrussian.stores.auth.recovery

import com.arkivanov.mvikotlin.core.store.Store
import com.example.inrussian.stores.auth.recovery.RecoveryStore.Intent
import com.example.inrussian.stores.auth.recovery.RecoveryStore.Label
import com.example.inrussian.stores.auth.recovery.RecoveryStore.State
import dev.icerock.moko.resources.StringResource


interface RecoveryStore : Store<Intent, State, Label> {
    sealed class Intent {
        data class EmailChange(val email: String) : Intent()
        data class CodeChange(val code: String) : Intent()
        data class PasswordChange(val password: String) : Intent()
        data class ConfirmPasswordChange(val confirmPassword: String) : Intent()
        data object PasswordImageClick : Intent()
        data object ConfirmPasswordImageClick : Intent()
        data object EmailImageClick : Intent()
        data object QuestionClick : Intent()
        data object QuestionButtonClick : Intent()
        data object QuestionDismiss : Intent()
        data object ContinueClick : Intent()
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
        val code: String = "",
        val codeError: StringResource? = null,
        val remainingSeconds: Int = 90,
        val loading: Boolean = false,
        val showEmailScreen: Boolean = true,
        val showCodeScreen: Boolean = false,
        val showPasswordScreen: Boolean = false,
        val questionShow: Boolean = false,
    ) {
        val isButtonActive: Boolean
            get() = (email.isNotBlank() || code.isNotBlank())  && emailError == null && password == confirmPassword && passwordError == null && confirmPasswordError == null //&& timerMinute == 0 && timerSecond == 0

        val timerString: String
            get() {
                val minutes = remainingSeconds / 60
                val seconds = remainingSeconds % 60
                return "${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}"
            }

    }

    sealed interface Action {}

    sealed interface Msg {
        data object Loading : Msg
        data object FinishLoading : Msg
        data class EmailChanged(val email: String) : Msg
        data class PasswordChanged(val password: String) : Msg
        data class ConfirmPasswordChanged(val confirmPassword: String) : Msg
        data class CodeChanged(val code: String) : Msg
        data class EmailError(val messageId: StringResource) : Msg
        data class PasswordError(val messageId: StringResource) : Msg
        data class ConfirmPasswordError(val messageId: StringResource) : Msg
        data class CodeError(val messageId: StringResource) : Msg
        data object QuestionClick : Msg
        data object QuestionDismiss : Msg
        data object PasswordTransform : Msg
        data object ConfirmPasswordTransform : Msg
        data object DeleteEmail : Msg
        data object Confirm : Msg
        data object UpdateTime : Msg
        data object StartTimer : Msg
    }

    sealed interface Label {
        data object UpdateSuccessfully : Label
        data object SetCorrectCode : Label
        data object SetEmail : Label
        data object OpenPhone : Label
    }

}
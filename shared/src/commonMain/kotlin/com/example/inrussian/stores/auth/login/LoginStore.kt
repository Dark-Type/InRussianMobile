package com.example.inrussian.stores.auth.login

import com.arkivanov.mvikotlin.core.store.Store
import com.example.inrussian.stores.auth.login.LoginStore.Intent
import com.example.inrussian.stores.auth.login.LoginStore.Label
import com.example.inrussian.stores.auth.login.LoginStore.State

interface LoginStore : Store<Intent, State, Label> {
    sealed class Intent {
        data object LoginClick : Intent()
        data class EmailChange(val email: String) : Intent()
        data class PasswordChange(val password: String) : Intent()
        data object PasswordImageClick : Intent()
        data object EmailImageClick : Intent()
    }

    data class State(
        val email: String = "",
        val password: String = "",
        val showPassword: Boolean = false,
        val emailError: String? = null,
        val passwordError: String? = null,
        val loading: Boolean = false
    ) {
        val isButtonActive: Boolean
            get() = email.isNotBlank() && emailError == null && password.isNotBlank() && passwordError == null
    }

    sealed interface Action {
    }

    sealed interface Msg {
        data object Loading : Msg
        data object FinishLoading : Msg
        data class EmailChanged(val email: String) : Msg
        data class PasswordChanged(val password: String) : Msg
        data class EmailError(val messageId: String) : Msg
        data class PasswordError(val messageId: String) : Msg
        data object PasswordTransform : Msg
        data object DeleteEmail : Msg
        data object Confirm : Msg

    }

    sealed interface Label {
        data object SubmittedSuccessfully : Label
        data object SubmissionFailed : Label
    }

}
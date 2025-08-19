package com.example.inrussian.components.auth.login

import co.touchlab.kermit.Logger
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.example.inrussian.stores.auth.login.LoginStore
import com.example.inrussian.stores.auth.login.LoginStore.Intent
import com.example.inrussian.stores.auth.login.LoginStore.Label
import com.example.inrussian.utile.componentCoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.math.log

interface LoginComponent {
    val state: StateFlow<LoginStore.State>

    fun onLogin(email: String, password: String)
    fun onForgotPasswordClicked()
    fun onBackClicked()

    fun onShowPasswordClick()

    fun onDeleteEmailClick()

    fun onEmailChange(email: String)
    fun onPasswordChange(password: String)
}

class DefaultLoginComponent(
    componentContext: ComponentContext,
    private val onOutput: (LoginOutput) -> Unit,
    private val store: LoginStore
) : LoginComponent, ComponentContext by componentContext {
    @OptIn(ExperimentalCoroutinesApi::class)
    override val state = store.stateFlow
    val scope = componentCoroutineScope()

    init {
        scope.launch {
            store.labels.collect {
                when (it) {
                    Label.SubmissionFailed -> {}
                    Label.SubmittedSuccessfully -> onOutput(LoginOutput.AuthenticationSuccess)

                }
            }
        }
    }

    override fun onLogin(email: String, password: String) {
        store.accept(Intent.LoginClick)
    }

    override fun onForgotPasswordClicked() {
        onOutput(LoginOutput.NavigateToEnterEmail)
    }

    override fun onBackClicked() {
        onOutput(LoginOutput.NavigateBack)
    }

    override fun onShowPasswordClick() {
        store.accept(Intent.PasswordImageClick)

    }

    override fun onDeleteEmailClick() {
        store.accept(Intent.EmailImageClick)


    }

    override fun onEmailChange(email: String) {
        store.accept(Intent.EmailChange(email))
    }

    override fun onPasswordChange(password: String) {
        store.accept(Intent.PasswordChange(password))

    }
}
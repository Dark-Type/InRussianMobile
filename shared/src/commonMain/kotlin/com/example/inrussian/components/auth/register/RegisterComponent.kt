package com.example.inrussian.components.auth.register

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.example.inrussian.stores.auth.register.RegisterStore
import com.example.inrussian.stores.auth.register.RegisterStore.Intent
import com.example.inrussian.utile.componentCoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

interface RegisterComponent {
    fun onRegister(email: String, password: String)
    fun onBackClicked()
    fun changeEmail(email: String)
    fun changePassword(password: String)
    fun changeConfirmPassword(confirmPassword: String)
    fun onShowPasswordClick()
    fun onShowConfirmPasswordClick()
    fun onEmailDeleteClick()
    val state: StateFlow<RegisterStore.State>
}

sealed class RegisterOutput {
    object AuthenticationSuccess : RegisterOutput()
    object NavigateBack : RegisterOutput()
}

class DefaultRegisterComponent(
    componentContext: ComponentContext,
    private val onOutput: (RegisterOutput) -> Unit,
    private val store: RegisterStore,
) : RegisterComponent, ComponentContext by componentContext {
    override val state = MutableStateFlow(store.state)
    val scope = componentCoroutineScope()
    private val _store = instanceKeeper.getStore { store }
    init {
        scope.launch {
            store.labels.collect {
                when (it) {
                    RegisterStore.Label.SubmittedSuccessfully ->
                        onOutput(RegisterOutput.AuthenticationSuccess)
                }
            }
        }
        scope.launch {

        }

    }

    override fun onRegister(email: String, password: String) {
        store.accept(Intent.SignUpClick)
    }

    override fun onBackClicked() {
        onOutput(RegisterOutput.NavigateBack)
    }

    override fun changeEmail(email: String) {
        store.accept(Intent.EmailChange(email))
    }

    override fun changePassword(password: String) {
        store.accept(Intent.PasswordChange(password))
    }

    override fun changeConfirmPassword(confirmPassword: String) {
        store.accept(Intent.ConfirmPasswordChange(confirmPassword))
    }

    override fun onShowPasswordClick() {
        store.accept(Intent.PasswordImageClick)
    }

    override fun onShowConfirmPasswordClick() {
        store.accept(Intent.ConfirmPasswordImageClick)

    }

    override fun onEmailDeleteClick() {
        store.accept(Intent.EmailImageClick)
    }
    companion object {
        private const val KEY = "LoginComponentState"
    }
}
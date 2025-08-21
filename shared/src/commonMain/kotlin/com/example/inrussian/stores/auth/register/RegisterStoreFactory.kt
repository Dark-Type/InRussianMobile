package com.example.inrussian.stores.auth.register

import co.touchlab.kermit.Logger
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.example.inrussian.domain.Validator
import com.example.inrussian.models.ErrorType
import com.example.inrussian.models.RegisterError
import com.example.inrussian.stores.auth.register.RegisterStore.Action
import com.example.inrussian.stores.auth.register.RegisterStore.Intent
import com.example.inrussian.stores.auth.register.RegisterStore.Label
import com.example.inrussian.stores.auth.register.RegisterStore.Msg
import com.example.inrussian.stores.auth.register.RegisterStore.Msg.Confirm
import com.example.inrussian.stores.auth.register.RegisterStore.Msg.ConfirmPasswordChanged
import com.example.inrussian.stores.auth.register.RegisterStore.Msg.ConfirmPasswordTransform
import com.example.inrussian.stores.auth.register.RegisterStore.Msg.DeleteEmail
import com.example.inrussian.stores.auth.register.RegisterStore.Msg.EmailChanged
import com.example.inrussian.stores.auth.register.RegisterStore.Msg.EmailError
import com.example.inrussian.stores.auth.register.RegisterStore.Msg.FinishLoading
import com.example.inrussian.stores.auth.register.RegisterStore.Msg.Loading
import com.example.inrussian.stores.auth.register.RegisterStore.Msg.PasswordChanged
import com.example.inrussian.stores.auth.register.RegisterStore.Msg.PasswordError
import com.example.inrussian.stores.auth.register.RegisterStore.Msg.PasswordTransform
import com.example.inrussian.stores.auth.register.RegisterStore.State
import com.example.inrussian.utils.ErrorDecoder
import kotlinx.coroutines.launch

class RegisterStoreFactory(
    private val storeFactory: StoreFactory,
    private val errorDecoder: ErrorDecoder,
    private val validator: Validator
) {
    fun create(): RegisterStore =
        object : RegisterStore, Store<Intent, State, Label> by storeFactory.create(
            name = "RegisterStore",
            initialState = State(),
            bootstrapper = SimpleBootstrapper(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl,
        ) {

        }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {

        override fun executeAction(action: Action) {
        }

        override fun executeIntent(intent: Intent) {
            when (intent) {
                Intent.SignUpClick -> {
                    scope.launch {
                        val state = state()
                        Logger.d { "validate" }
                        try {
                            Logger.d { "validate1" }
                            dispatch(Loading)
                            validator.validateEmail(state.email)
                            validator.validatePassword(state.password)
                            validator.validateConfirmPassword(state.password, state.confirmPassword)
                            Logger.d { "validate2" }
                            publish(Label.SubmittedSuccessfully)

                        } catch (e: ErrorType) {
                            Logger.d { "error: $e" }
                            if (e is RegisterError) {
                                when (e) {
                                    ErrorType.InvalidEmail -> dispatch(
                                        EmailError(
                                            errorDecoder.decode(
                                                e
                                            )
                                        )
                                    )

                                    ErrorType.InvalidPassword -> dispatch(
                                        PasswordError(
                                            errorDecoder.decode(
                                                e
                                            )
                                        )
                                    )

                                    ErrorType.UnAuthorize -> dispatch(
                                        PasswordError(
                                            errorDecoder.decode(
                                                e
                                            )
                                        )
                                    )
                                }
                            }
                        }
                        dispatch(FinishLoading)

                    }
                }

                is Intent.EmailChange -> dispatch(EmailChanged(intent.email))
                Intent.EmailImageClick -> dispatch(EmailChanged(""))
                is Intent.PasswordChange -> dispatch(
                    PasswordChanged(
                        intent.password
                    )
                )

                Intent.PasswordImageClick -> dispatch(PasswordTransform)
                is Intent.ConfirmPasswordChange -> dispatch(ConfirmPasswordChanged(intent.password))
                Intent.ConfirmPasswordImageClick -> dispatch(ConfirmPasswordTransform)
            }
        }
    }


    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State = when (msg) {
            Confirm -> copy(loading = true)
            is EmailChanged -> copy(
                email = msg.email,
                emailError = null,
            )

            is EmailError -> copy(emailError = msg.messageId)
            Loading -> copy(
                loading = true, emailError = null, passwordError = null,
            )

            is PasswordChanged -> copy(
                password = msg.password,
                passwordError = null,
            )

            is PasswordError -> copy(passwordError = msg.messageId)

            PasswordTransform -> copy(showPassword = !showPassword)
            DeleteEmail -> copy(email = "")
            FinishLoading -> copy(loading = false)
            is ConfirmPasswordChanged -> copy(confirmPassword = msg.password)
            is Msg.ConfirmPasswordError -> copy(confirmPasswordError = msg.messageId)
            ConfirmPasswordTransform -> copy(showConfirmPassword = !showConfirmPassword)
        }
    }
}
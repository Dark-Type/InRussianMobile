package com.example.inrussian.stores

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.example.inrussian.components.auth.login.LoginOutput
import com.example.inrussian.models.ErrorType
import com.example.inrussian.models.LoginError
import com.example.inrussian.models.models.LoginModel
import com.example.inrussian.repository.auth.AuthRepository
import com.example.inrussian.stores.LoginStore.Action
import com.example.inrussian.stores.LoginStore.Intent
import com.example.inrussian.stores.LoginStore.Label
import com.example.inrussian.stores.LoginStore.Msg
import com.example.inrussian.stores.LoginStore.Msg.Confirm
import com.example.inrussian.stores.LoginStore.Msg.EmailChanged
import com.example.inrussian.stores.LoginStore.Msg.EmailError
import com.example.inrussian.stores.LoginStore.Msg.FinishLoading
import com.example.inrussian.stores.LoginStore.Msg.Loading
import com.example.inrussian.stores.LoginStore.Msg.PasswordChanged
import com.example.inrussian.stores.LoginStore.Msg.PasswordError
import com.example.inrussian.stores.LoginStore.Msg.PasswordTransform
import com.example.inrussian.stores.LoginStore.State
import com.example.inrussian.utile.ErrorDecoder
import kotlinx.coroutines.launch

class LoginStoreFactory(
    private val storeFactory: StoreFactory,
    private val errorDecoder: ErrorDecoder,
    private val repository: AuthRepository,
) {
    fun create(): LoginStore =
        object : LoginStore, Store<Intent, State, Label> by storeFactory.create(
            name = "AuthStore",
            initialState = State(),
            bootstrapper = SimpleBootstrapper(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl,
        ) {}

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {

        override fun executeAction(action: Action) {
        }

        override fun executeIntent(intent: Intent) {
            when (intent) {
                Intent.LoginClick -> {
                    scope.launch {
                        val state = state()
                        try {
                            dispatch(Loading)
                            val token = repository.login(
                                LoginModel(
                                    email = state.email,
                                    password = state.password
                                )
                            ).refreshToken
                            repository.saveRefreshToken(token)
                            publish(Label.SubmittedSuccessfully)

                        } catch (e: ErrorType) {
                            if (e is LoginError) {
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
                            publish(Label.SubmissionFailed)

                        }
                        dispatch(FinishLoading)

                    }
                }

                is Intent.EmailChange -> dispatch(EmailChanged(intent.email))
                Intent.EmailImageClick -> dispatch(EmailChanged(""))
                is Intent.PasswordChange -> dispatch(PasswordChanged(intent.password))
                Intent.PasswordImageClick -> dispatch(PasswordTransform)
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
                email = msg.password,
                passwordError = null,
            )

            is PasswordError -> copy(passwordError = msg.messageId)

            PasswordTransform -> copy(showPassword = !showPassword)
            Msg.DeleteEmail -> copy(email = "")
            Msg.FinishLoading -> copy(loading = false)
        }
    }


    companion object {
        private const val TAG = "AuthStoreFactory"
    }
}
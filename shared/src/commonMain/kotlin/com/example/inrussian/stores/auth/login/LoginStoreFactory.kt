package com.example.inrussian.stores.auth.login

import co.touchlab.kermit.Logger
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.example.inrussian.models.ErrorType
import com.example.inrussian.models.LoginError
import com.example.inrussian.models.models.LoginModel
import com.example.inrussian.repository.auth.AuthRepository
import com.example.inrussian.utils.ErrorDecoder
import kotlinx.coroutines.launch

class LoginStoreFactory(
    private val storeFactory: StoreFactory,
    private val errorDecoder: ErrorDecoder,
    private val repository: AuthRepository,
) {
    fun create(): LoginStore =
        object : LoginStore, Store<LoginStore.Intent, LoginStore.State, LoginStore.Label> by storeFactory.create(
            name = "LoginStore",
            initialState = LoginStore.State(),
            bootstrapper = SimpleBootstrapper(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl,
        ) {}

    private inner class ExecutorImpl : CoroutineExecutor<LoginStore.Intent, LoginStore.Action, LoginStore.State, LoginStore.Msg, LoginStore.Label>() {

        override fun executeAction(action: LoginStore.Action) {
        }

        override fun executeIntent(intent: LoginStore.Intent) {
            when (intent) {
                LoginStore.Intent.LoginClick -> {
                    scope.launch {
                        val state = state()
                        try {
                            dispatch(LoginStore.Msg.Loading)
//                            val token = repository.login(
//                                LoginModel(
//                                    email = state.email,
//                                    password = state.password
//                                )
//                            ).refreshToken
//                            repository.saveRefreshToken(token)
                            // ВЕРНУТЬ ПРИ ЗАЦЕПЕ API
                            publish(LoginStore.Label.SubmittedSuccessfully)

                        } catch (e: ErrorType) {
                            if (e is LoginError) {
                                when (e) {
                                    ErrorType.InvalidEmail -> dispatch(
                                        LoginStore.Msg.EmailError(
                                            errorDecoder.decode(
                                                e
                                            )
                                        )
                                    )

                                    ErrorType.InvalidPassword -> dispatch(
                                        LoginStore.Msg.PasswordError(
                                            errorDecoder.decode(
                                                e
                                            )
                                        )
                                    )

                                    ErrorType.UnAuthorize -> dispatch(
                                        LoginStore.Msg.PasswordError(
                                            errorDecoder.decode(
                                                e
                                            )
                                        )
                                    )
                                }
                            }
                            publish(LoginStore.Label.SubmissionFailed)

                        }
                        dispatch(LoginStore.Msg.FinishLoading)

                    }
                }

                is LoginStore.Intent.EmailChange -> {
                    Logger.i {  "changeEmail"}
                    dispatch(LoginStore.Msg.EmailChanged(intent.email))
                }
                LoginStore.Intent.EmailImageClick -> dispatch(LoginStore.Msg.EmailChanged(""))
                is LoginStore.Intent.PasswordChange -> dispatch(
                    LoginStore.Msg.PasswordChanged(
                        intent.password
                    )
                )
                LoginStore.Intent.PasswordImageClick -> dispatch(LoginStore.Msg.PasswordTransform)
            }
        }
    }


    private object ReducerImpl : Reducer<LoginStore.State, LoginStore.Msg> {
        override fun LoginStore.State.reduce(msg: LoginStore.Msg): LoginStore.State = when (msg) {
            LoginStore.Msg.Confirm -> copy(loading = true)
            is LoginStore.Msg.EmailChanged -> {
                Logger.i {  "changeEmail2"}
                copy(
                    email = msg.email,
                    emailError = null,
                )
            }

            is LoginStore.Msg.EmailError -> copy(emailError = msg.messageId)
            LoginStore.Msg.Loading -> copy(
                loading = true, emailError = null, passwordError = null,
            )

            is LoginStore.Msg.PasswordChanged -> copy(
                password = msg.password,
                passwordError = null,
            )

            is LoginStore.Msg.PasswordError -> copy(passwordError = msg.messageId)

            LoginStore.Msg.PasswordTransform -> copy(showPassword = !showPassword)
            LoginStore.Msg.DeleteEmail -> copy(email = "")
            LoginStore.Msg.FinishLoading -> copy(loading = false)
        }
    }


    companion object {
        private const val TAG = "AuthStoreFactory"
    }
}
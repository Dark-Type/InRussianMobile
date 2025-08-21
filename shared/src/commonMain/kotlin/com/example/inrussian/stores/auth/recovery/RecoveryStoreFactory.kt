package com.example.inrussian.stores.auth.recovery

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.example.inrussian.domain.Validator
import com.example.inrussian.models.ErrorType
import com.example.inrussian.models.PasswordRecoveryError
import com.example.inrussian.repository.auth.AuthRepository
import com.example.inrussian.stores.auth.recovery.RecoveryStore.Action
import com.example.inrussian.stores.auth.recovery.RecoveryStore.Intent
import com.example.inrussian.stores.auth.recovery.RecoveryStore.Label
import com.example.inrussian.stores.auth.recovery.RecoveryStore.Msg
import com.example.inrussian.stores.auth.recovery.RecoveryStore.Msg.CodeChanged
import com.example.inrussian.stores.auth.recovery.RecoveryStore.Msg.CodeError
import com.example.inrussian.stores.auth.recovery.RecoveryStore.Msg.Confirm
import com.example.inrussian.stores.auth.recovery.RecoveryStore.Msg.ConfirmPasswordChanged
import com.example.inrussian.stores.auth.recovery.RecoveryStore.Msg.ConfirmPasswordError
import com.example.inrussian.stores.auth.recovery.RecoveryStore.Msg.ConfirmPasswordTransform
import com.example.inrussian.stores.auth.recovery.RecoveryStore.Msg.DeleteEmail
import com.example.inrussian.stores.auth.recovery.RecoveryStore.Msg.EmailChanged
import com.example.inrussian.stores.auth.recovery.RecoveryStore.Msg.EmailError
import com.example.inrussian.stores.auth.recovery.RecoveryStore.Msg.FinishLoading
import com.example.inrussian.stores.auth.recovery.RecoveryStore.Msg.Loading
import com.example.inrussian.stores.auth.recovery.RecoveryStore.Msg.PasswordChanged
import com.example.inrussian.stores.auth.recovery.RecoveryStore.Msg.PasswordError
import com.example.inrussian.stores.auth.recovery.RecoveryStore.Msg.PasswordTransform
import com.example.inrussian.stores.auth.recovery.RecoveryStore.State
import com.example.inrussian.stores.auth.recovery.RecoveryStoreFactory.ReducerImpl.backUpForSecond
import com.example.inrussian.utils.ErrorDecoder
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RecoveryStoreFactory(
    private val storeFactory: StoreFactory,
    private val errorDecoder: ErrorDecoder,
    private val validator: Validator,
    private val repository: AuthRepository,
) {
    fun create(): RecoveryStore =
        object : RecoveryStore, Store<Intent, State, Label> by storeFactory.create(
            name = "RecoveryStore",
            initialState = State(),
            bootstrapper = SimpleBootstrapper(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl,
        ) {}

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        suspend fun startTimer(state: State) {
            var minute = state.timerMinute
            var second = state.timerSecond
            while (minute != 0 || second != 0) {
                delay(1000)
                val (newMinute, newSecond) = backUpForSecond(minute, second)
                minute = newMinute
                second = newSecond
                dispatch(Msg.UpdateTime)
            }
        }


        override fun executeAction(action: Action) {
        }

        override fun executeIntent(intent: Intent) {
            when (intent) {


                is Intent.EmailChange -> dispatch(EmailChanged(intent.email))
                Intent.EmailImageClick -> dispatch(EmailChanged(""))
                is Intent.PasswordChange -> dispatch(
                    PasswordChanged(
                        intent.password
                    )
                )

                Intent.PasswordImageClick -> dispatch(PasswordTransform)
                is Intent.CodeChange -> dispatch(CodeChanged(intent.code))
                is Intent.ConfirmPasswordChange -> dispatch(ConfirmPasswordChanged(intent.confirmPassword))
                Intent.ConfirmPasswordImageClick -> dispatch(ConfirmPasswordTransform)
                Intent.ContinueClick -> {
                    scope.launch {
                        val localState = state()
                        try {
                            with(localState) {
                                dispatch(Confirm)
                                if (showPasswordScreen) {
                                    validator.validatePassword(password)
                                    validator.validateConfirmPassword(password, confirmPassword)
                                    repository.updatePassword(email, password)
                                    publish(Label.UpdateSuccessfully)
                                    startTimer(state())
                                } else if (showCodeScreen) {
                                    repository.sendCode(code, email)
                                    publish(Label.SetCorrectCode)
                                } else if (showEmailScreen) {
                                    validator.validateEmail(email)
                                    publish(Label.SetEmail)
                                }

                            }
                        } catch (e: ErrorType) {
                            if (e is PasswordRecoveryError) {
                                when (e) {
                                    ErrorType.IncorrectCode -> CodeError(errorDecoder.decode(e))
                                    ErrorType.InvalidConfirmPassword -> ConfirmPasswordError(
                                        errorDecoder.decode(e)
                                    )

                                    ErrorType.InvalidPassword -> PasswordError(errorDecoder.decode(e))
                                }
                            }
                        }
                        dispatch(FinishLoading)
                    }
                }

                Intent.QuestionButtonClick -> publish(Label.OpenPhone)
                Intent.QuestionClick -> dispatch(Msg.QuestionClick)
                Intent.QuestionDismiss -> dispatch(Msg.QuestionDismiss)
            }
        }
    }


    private object ReducerImpl : Reducer<State, Msg> {
        fun backUpForSecond(minute: Int, second: Int): Pair<Int, Int> {
            return if (minute == 0 && second == 0) {
                0 to 0
            } else if (second == 0) {
                (minute - 1) to 59
            } else {
                minute to (second - 1)
            }
        }

        override fun State.reduce(msg: Msg): State = when (msg) {
            is CodeChanged -> copy(code = msg.code)
            is CodeError -> copy(codeError = msg.messageId)
            is ConfirmPasswordChanged -> copy(confirmPassword = msg.confirmPassword)
            is ConfirmPasswordError -> copy(confirmPasswordError = msg.messageId)
            ConfirmPasswordTransform -> copy(showConfirmPassword = !showConfirmPassword)
            Confirm -> {
                copy(loading = true)
            }

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
            DeleteEmail -> copy(email = "")
            FinishLoading -> copy(loading = false)
            Msg.QuestionClick -> copy(questionShow = true)
            Msg.QuestionDismiss -> copy(questionShow = false)
            Msg.UpdateTime -> {
                val (newMinute, newSecond) = backUpForSecond(timerMinute, timerSecond)
                copy(timerMinute = newMinute, timerSecond = newSecond)
            }
            Msg.StartTimer -> {
                val (newMinute, newSecond) = backUpForSecond(timerMinute, timerSecond)
                copy(timerMinute = newMinute, timerSecond = newSecond)
            }
        }

    }
}
package com.example.inrussian.stores.auth.register

import co.touchlab.kermit.Logger
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.example.inrussian.components.main.profile.Gender
import com.example.inrussian.components.main.profile.PeriodSpent
import com.example.inrussian.components.main.profile.UserProfile
import com.example.inrussian.domain.Validator
import com.example.inrussian.models.ErrorType
import com.example.inrussian.models.RegisterError
import com.example.inrussian.models.models.LoginModel
import com.example.inrussian.models.models.RegisterModel
import com.example.inrussian.models.models.SystemLanguage
import com.example.inrussian.repository.auth.AuthRepository
import com.example.inrussian.repository.main.user.UserRepository
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
import com.example.inrussian.stores.auth.register.RegisterStore.Msg.UpdateCitizenship
import com.example.inrussian.stores.auth.register.RegisterStore.Msg.UpdateEducation
import com.example.inrussian.stores.auth.register.RegisterStore.Msg.UpdateLanguage
import com.example.inrussian.stores.auth.register.RegisterStore.Msg.UpdatePersonalData
import com.example.inrussian.stores.auth.register.RegisterStore.State
import com.example.inrussian.utils.ErrorDecoder
import kotlinx.coroutines.launch
import kotlin.time.Clock.System.now
import kotlin.time.ExperimentalTime

class RegisterStoreFactory(
    private val storeFactory: StoreFactory,
    private val errorDecoder: ErrorDecoder,
    private val validator: Validator,
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
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

        @OptIn(ExperimentalTime::class)
        override fun executeIntent(intent: Intent) {
            scope.launch {
                Logger.d {state().toString() }
                when (intent) {
                    Intent.SignUpClick -> {
                        scope.launch {
                            val state = state()

                            try {
                                dispatch(Loading)
                                validator.validateEmail(state.email)
                                validator.validatePassword(state.password)
                                validator.validateConfirmPassword(
                                    state.password, state.confirmPassword
                                )

                                val response = authRepository.login(
                                    LoginModel(
                                        email = state.email,
                                        password = state.password
                                    )
                                )

                                throw ErrorType.EmailExist
                            } catch (e: ErrorType) {
                                if (e is RegisterError) {
                                    when (e) {
                                        ErrorType.InvalidEmail -> dispatch(
                                            EmailError(
                                                errorDecoder.decode(
                                                    e
                                                )
                                            )
                                        )

                                        ErrorType.EmailExist -> dispatch(
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
                            } catch (e: Exception) {
                                publish(Label.SubmittedSuccessfully)
                            }
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
                    is Intent.UpdateCitizenship -> {
                        dispatch(UpdateCitizenship(intent.state))
                    }

                    is Intent.UpdateEducation -> {
                        dispatch(UpdateEducation(intent.state))
                        scope.launch {
                            val state = state()

                            try {
                                val token = authRepository.register(
                                    RegisterModel(
                                        email = state.email,
                                        password = state.password,
                                        phone = state.personalDataState?.phoneNumber ?: "",
                                        systemLanguage = SystemLanguage.valueOf(
                                            state.languageState?.selectedLanguage ?: "RUSSIAN"
                                        )
                                    )
                                )
                                authRepository.setToken(token.accessToken)
                                authRepository.saveRefreshToken(token.refreshToken)

                                userRepository.createProfile(
                                    UserProfile(
                                        surname = state.personalDataState?.surname ?: "",
                                        name = state.personalDataState?.name ?: "",
                                        patronymic = state.personalDataState?.patronymic,
                                        gender = Gender.valueOf(
                                            state.personalDataState?.gender ?: ""
                                        ),
                                        dob = state.personalDataState?.birthDate ?: "",
                                        dor = now().toString(),
                                        citizenship = state.citizenshipState?.citizenship,
                                        nationality = state.citizenshipState?.nationality,
                                        countryOfResidence = state.citizenshipState?.countryOfResidence,
                                        cityOfResidence = state.citizenshipState?.cityOfResidence,
                                        countryDuringEducation = state.citizenshipState?.countryDuringEducation,
                                        periodSpent = PeriodSpent.valueOf(
                                            state.citizenshipState?.timeSpentInRussia ?: ""
                                        ),
                                        kindOfActivity = state.educationState?.kindOfActivity,
                                        education = state.educationState?.education,
                                        purposeOfRegister = state.educationState?.purposeOfRegistration,
                                        languages = state.educationState?.languages ?: listOf(),
                                        language = com.example.inrussian.components.main.profile.SystemLanguage.valueOf(
                                            (state.languageState?.selectedLanguage
                                                ?: SystemLanguage.RUSSIAN) as String
                                        ),
                                        email = state.email
                                    )
                                )
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
                            } catch (e: Exception) {
                                println(e.message)
                            }
                        }
                    }

                    is Intent.UpdateLanguage -> dispatch(UpdateLanguage(intent.state))
                    is Intent.UpdatePersonalData -> dispatch(UpdatePersonalData(intent.state))
                }
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
            is UpdateCitizenship -> copy(citizenshipState = msg.state)
            is UpdateEducation -> copy(educationState = msg.state)
            is UpdateLanguage -> copy(languageState = msg.state)
            is UpdatePersonalData -> copy(personalDataState = msg.state)
        }
    }
}
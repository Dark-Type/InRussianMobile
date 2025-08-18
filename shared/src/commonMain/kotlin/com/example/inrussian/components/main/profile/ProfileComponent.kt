package com.example.inrussian.components.main.profile

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.example.inrussian.components.auth.root.AuthRootComponent
import com.example.inrussian.di.AboutComponentFactory
import com.example.inrussian.di.EditProfileComponentFactory
import com.example.inrussian.di.PrivacyPolicyComponentFactory
import com.example.inrussian.navigation.configurations.AuthConfiguration
import com.example.inrussian.repository.main.user.UserRepository
import com.example.inrussian.navigation.configurations.ProfileConfiguration
import com.example.inrussian.repository.main.BadgeRepository
import com.example.inrussian.repository.main.settings.SettingsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

sealed interface ProfileOutput {
    data object NavigateBack : ProfileOutput
}

interface ProfileComponent {
    val stack: Value<ChildStack<*, Child>>

    fun openEditProfile()
    fun openAbout()
    fun openPrivacyPolicy()
    fun onBack()

    sealed interface Child {
        data class MainChild(val component: ProfileMainComponent) : Child
        data class EditProfileChild(val component: EditProfileComponent) : Child
        data class AboutChild(val component: AboutComponent) : Child
        data class PrivacyPolicyChild(val component: PrivacyPolicyComponent) : Child
    }
}

class DefaultProfileComponent(
    componentContext: ComponentContext,
    private val onOutput: (ProfileOutput) -> Unit,
    private val userRepository: UserRepository,
    private val badgeRepository: BadgeRepository,
    private val settingsRepository: SettingsRepository,
    private val aboutText: String,
    private val privacyText: String,
    private val editProfileFactory: EditProfileComponentFactory,
    private val aboutFactory: AboutComponentFactory,
    private val privacyPolicyFactory: PrivacyPolicyComponentFactory
) : ProfileComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<ProfileConfiguration>()

    override val stack: Value<ChildStack<*, ProfileComponent.Child>> =
        childStack(
            source = navigation,
            serializer = ProfileConfiguration.serializer(),
            initialConfiguration = ProfileConfiguration.Main,
            handleBackButton = true,
            childFactory = ::child,
        )

    override fun openEditProfile() {
        navigation.pushNew(ProfileConfiguration.EditProfile)
    }

    override fun openAbout() {
        navigation.pushNew(ProfileConfiguration.About)
    }

    override fun openPrivacyPolicy() {
        navigation.pushNew(ProfileConfiguration.PrivacyPolicy)
    }

    override fun onBack() {
        if (stack.value.backStack.isNotEmpty()) {
            navigation.pop()
        } else {
            onOutput(ProfileOutput.NavigateBack)
        }
    }

    private fun child(
        configuration: ProfileConfiguration,
        componentContext: ComponentContext
    ): ProfileComponent.Child =
        when (configuration) {
            is ProfileConfiguration.Main ->
                ProfileComponent.Child.MainChild(
                    DefaultProfileMainComponent(
                        componentContext = componentContext,
                        userRepository = userRepository,
                        badgeRepository = badgeRepository,
                        settingsRepository = settingsRepository,
                        onEdit = ::openEditProfile,
                        onAbout = ::openAbout,
                        onPrivacy = ::openPrivacyPolicy
                    )
                )

            is ProfileConfiguration.EditProfile ->
                ProfileComponent.Child.EditProfileChild(
                    editProfileFactory(componentContext) { output ->
                        if (output is EditProfileOutput.NavigateBack) onBack()
                    }
                )

            is ProfileConfiguration.About ->
                ProfileComponent.Child.AboutChild(
                    aboutFactory(componentContext) { output ->
                        if (output is AboutOutput.NavigateBack) onBack()
                    }
                )

            is ProfileConfiguration.PrivacyPolicy ->
                ProfileComponent.Child.PrivacyPolicyChild(
                    privacyPolicyFactory(componentContext) { output ->
                        if (output is PrivacyPolicyOutput.NavigateBack) onBack()
                    }
                )
        }


}
interface ProfileMainComponent {
    val state: Value<ProfileMainState>
    fun onEditClick()
    fun onAboutClick()
    fun onPrivacyPolicyClick()
    fun toggleNotifications()
    fun cycleTheme()
}


class DefaultProfileMainComponent(
    componentContext: ComponentContext,
    private val userRepository: UserRepository,
    private val badgeRepository: BadgeRepository,
    private val settingsRepository: SettingsRepository,
    private val onEdit: () -> Unit,
    private val onAbout: () -> Unit,
    private val onPrivacy: () -> Unit
) : ProfileMainComponent, ComponentContext by componentContext {

    private val scope = CoroutineScope(Dispatchers.Main.immediate)

    private val _state = MutableValue(ProfileMainState(isLoading = true))
    override val state: Value<ProfileMainState> = _state

    init {
        scope.launch {
            combine(
                userRepository.userFlow,
                userRepository.userProfileFlow,
                settingsRepository.notificationsEnabled,
                settingsRepository.theme
            ) { user, profile, notifications, theme ->
                Triple(user, profile, notifications to theme)
            }.collect { (user, profile, pair) ->
                val (notifications, theme) = pair
                val badges = badgeRepository.badgesForUser(user.id).first()
                _state.value = _state.value.copy(
                    isLoading = false,
                    user = user,
                    profile = profile,
                    badges = badges,
                    notificationsEnabled = notifications,
                    theme = theme
                )
            }
        }
    }

    override fun onEditClick() = onEdit()
    override fun onAboutClick() = onAbout()
    override fun onPrivacyPolicyClick() = onPrivacy()
    override fun toggleNotifications() = settingsRepository.toggleNotifications()

    override fun cycleTheme() {
        val next = when (_state.value.theme) {
            AppTheme.SYSTEM -> AppTheme.LIGHT
            AppTheme.LIGHT -> AppTheme.DARK
            AppTheme.DARK -> AppTheme.SYSTEM
        }
        settingsRepository.setTheme(next)
    }

    fun dispose() {
        scope.cancel()
    }
}
sealed interface EditProfileOutput { data object NavigateBack : EditProfileOutput }
sealed interface AboutOutput { data object NavigateBack : AboutOutput }
sealed interface PrivacyPolicyOutput { data object NavigateBack : PrivacyPolicyOutput }

interface EditProfileComponent {
    val state: Value<EditProfileState>
    fun updateSurname(value: String)
    fun updateName(value: String)
    fun updatePatronymic(value: String)
    fun updateGender(value: Gender)
    fun updateDob(value: String)
    fun updateCitizenship(value: String)
    fun updateNationality(value: String)
    fun updateCountryOfResidence(value: String)
    fun updateCityOfResidence(value: String)
    fun updateEducation(value: String)
    fun updatePurpose(value: String)
    fun save()
    fun onBack()
}
interface AboutComponent {
    val state: Value<StaticTextState>
    fun onBack()
}

interface PrivacyPolicyComponent {
    val state: Value<StaticTextState>
    fun onBack()
}


class DefaultEditProfileComponent(
    componentContext: ComponentContext,
    private val userRepository: UserRepository,
    private val onOutput: (EditProfileOutput) -> Unit
) : EditProfileComponent, ComponentContext by componentContext {

    private val scope = CoroutineScope(Dispatchers.Main.immediate)

    private val _state = MutableValue(EditProfileState(isLoading = true))
    override val state: Value<EditProfileState> = _state

    init {
        scope.launch {
            val profile = userRepository.userProfileFlow.first()
            _state.value = EditProfileState(
                isLoading = false,
                original = profile,
                workingCopy = profile,
                canSave = false
            )
        }
    }

    private fun update(copy: (UserProfile) -> UserProfile) {
        val working = _state.value.workingCopy ?: return
        val newWorking = copy(working)
        val canSave = newWorking != _state.value.original
        _state.value = _state.value.copy(
            workingCopy = newWorking,
            canSave = canSave
        )
    }

    override fun updateSurname(value: String) = update { it.copy(surname = value) }
    override fun updateName(value: String) = update { it.copy(name = value) }
    override fun updatePatronymic(value: String) = update { it.copy(patronymic = value.ifBlank { null }) }
    override fun updateGender(value: Gender) = update { it.copy(gender = value) }
    override fun updateDob(value: String) = update { it.copy(dob = value) }
    override fun updateCitizenship(value: String) = update { it.copy(citizenship = value.ifBlank { null }) }
    override fun updateNationality(value: String) = update { it.copy(nationality = value.ifBlank { null }) }
    override fun updateCountryOfResidence(value: String) = update { it.copy(countryOfResidence = value.ifBlank { null }) }
    override fun updateCityOfResidence(value: String) = update { it.copy(cityOfResidence = value.ifBlank { null }) }
    override fun updateEducation(value: String) = update { it.copy(education = value.ifBlank { null }) }
    override fun updatePurpose(value: String) = update { it.copy(purposeOfRegister = value.ifBlank { null }) }

    override fun save() {
        val working = _state.value.workingCopy ?: return
        if (!_state.value.canSave) return
        scope.launch {
            _state.value = _state.value.copy(isSaving = true)
            userRepository.updateProfile(working)
            _state.value = _state.value.copy(
                original = working,
                canSave = false,
                isSaving = false
            )
            onBack()
        }
    }

    override fun onBack() = onOutput(EditProfileOutput.NavigateBack)

    fun dispose() {
        scope.cancel()
    }
}
class DefaultAboutComponent(
    componentContext: ComponentContext,
    aboutText: String,
    private val onOutput: (AboutOutput) -> Unit
) : AboutComponent, ComponentContext by componentContext {

    private val _state = MutableValue(
        StaticTextState(
            title = "О приложении",
            text = aboutText
        )
    )
    override val state: Value<StaticTextState> = _state

    override fun onBack() = onOutput(AboutOutput.NavigateBack)
}
class DefaultPrivacyPolicyComponent(
    componentContext: ComponentContext,
    policyText: String,
    private val onOutput: (PrivacyPolicyOutput) -> Unit
) : PrivacyPolicyComponent, ComponentContext by componentContext {

    private val _state = MutableValue(
        StaticTextState(
            title = "Политика конфиденциальности",
            text = policyText
        )
    )
    override val state: Value<StaticTextState> = _state

    override fun onBack() = onOutput(PrivacyPolicyOutput.NavigateBack)
}
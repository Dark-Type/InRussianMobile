package com.example.inrussian.components.main.profile

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.value.Value
import com.example.inrussian.components.auth.root.AuthRootComponent
import com.example.inrussian.navigation.configurations.AuthConfiguration
import com.example.inrussian.repository.main.user.UserRepository
import com.example.inrussian.navigation.configurations.ProfileConfiguration
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
    private val editProfileFactory: (ComponentContext, (EditProfileOutput) -> Unit) -> EditProfileComponent,
    private val aboutFactory: (ComponentContext, (AboutOutput) -> Unit) -> AboutComponent,
    private val privacyPolicyFactory: (ComponentContext, (PrivacyPolicyOutput) -> Unit) -> PrivacyPolicyComponent
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
                        componentContext,
                        onEdit = ::openEditProfile,
                        onAbout = ::openAbout,
                        onPrivacy = ::openPrivacyPolicy
                    )
                )
            is ProfileConfiguration.EditProfile ->
                ProfileComponent.Child.EditProfileChild(
                    editProfileFactory(componentContext) { output: EditProfileOutput ->
                        if (output is EditProfileOutput.NavigateBack) onBack()
                    }
                )
            is ProfileConfiguration.About ->
                ProfileComponent.Child.AboutChild(
                    aboutFactory(componentContext) { output: AboutOutput ->
                        if (output is AboutOutput.NavigateBack) onBack()
                    }
                )
            is ProfileConfiguration.PrivacyPolicy ->
                ProfileComponent.Child.PrivacyPolicyChild(
                    privacyPolicyFactory(componentContext) { output: PrivacyPolicyOutput ->
                        if (output is PrivacyPolicyOutput.NavigateBack) onBack()
                    }
                )
        }
}

interface ProfileMainComponent {
    fun onEditClick()
    fun onAboutClick()
    fun onPrivacyPolicyClick()
}

class DefaultProfileMainComponent(
    componentContext: ComponentContext,
    private val onEdit: () -> Unit,
    private val onAbout: () -> Unit,
    private val onPrivacy: () -> Unit
) : ProfileMainComponent, ComponentContext by componentContext {
    override fun onEditClick() = onEdit()
    override fun onAboutClick() = onAbout()
    override fun onPrivacyPolicyClick() = onPrivacy()
}

sealed interface EditProfileOutput { data object NavigateBack : EditProfileOutput }
sealed interface AboutOutput { data object NavigateBack : AboutOutput }
sealed interface PrivacyPolicyOutput { data object NavigateBack : PrivacyPolicyOutput }

interface EditProfileComponent { fun onBack() }
interface AboutComponent { fun onBack() }
interface PrivacyPolicyComponent { fun onBack() }

class DefaultEditProfileComponent(
    componentContext: ComponentContext,
    private val onOutput: (EditProfileOutput) -> Unit
) : EditProfileComponent, ComponentContext by componentContext {
    override fun onBack() = onOutput(EditProfileOutput.NavigateBack)
}

class DefaultAboutComponent(
    componentContext: ComponentContext,
    private val onOutput: (AboutOutput) -> Unit
) : AboutComponent, ComponentContext by componentContext {
    override fun onBack() = onOutput(AboutOutput.NavigateBack)
}

class DefaultPrivacyPolicyComponent(
    componentContext: ComponentContext,
    private val onOutput: (PrivacyPolicyOutput) -> Unit
) : PrivacyPolicyComponent, ComponentContext by componentContext {
    override fun onBack() = onOutput(PrivacyPolicyOutput.NavigateBack)
}
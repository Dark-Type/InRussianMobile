package com.example.inrussian.components.auth.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.value.Value
import com.example.inrussian.components.auth.base.BaseAuthComponent
import com.example.inrussian.components.auth.base.BaseAuthOutput
import com.example.inrussian.components.auth.login.LoginComponent
import com.example.inrussian.components.auth.login.LoginOutput
import com.example.inrussian.components.auth.passwordRecovery.enterEmail.EnterEmailComponent
import com.example.inrussian.components.auth.passwordRecovery.enterEmail.EnterEmailOutput
import com.example.inrussian.components.auth.passwordRecovery.enterRecoveryCode.EnterRecoveryCodeComponent
import com.example.inrussian.components.auth.passwordRecovery.enterRecoveryCode.EnterRecoveryCodeOutput
import com.example.inrussian.components.auth.passwordRecovery.updatePassword.UpdatePasswordComponent
import com.example.inrussian.components.auth.passwordRecovery.updatePassword.UpdatePasswordOutput
import com.example.inrussian.components.auth.register.RegisterComponent
import com.example.inrussian.components.auth.register.RegisterOutput
import com.example.inrussian.components.auth.root.AuthRootComponent.Child.BaseAuthChild
import com.example.inrussian.components.auth.root.AuthRootComponent.Child.EnterEmailChild
import com.example.inrussian.components.auth.root.AuthRootComponent.Child.EnterRecoveryCodeChild
import com.example.inrussian.components.auth.root.AuthRootComponent.Child.LoginChild
import com.example.inrussian.components.auth.root.AuthRootComponent.Child.RegisterChild
import com.example.inrussian.components.auth.root.AuthRootComponent.Child.SsoPopoverChild
import com.example.inrussian.components.auth.root.AuthRootComponent.Child.UpdatePasswordChild
import com.example.inrussian.components.auth.ssoPopover.SsoPopoverComponent
import com.example.inrussian.components.auth.ssoPopover.SsoPopoverOutput
import com.example.inrussian.navigation.configurations.AuthConfiguration

interface AuthRootComponent {
    val stack: Value<ChildStack<*, Child>>

    sealed class Child {
        class BaseAuthChild(val component: BaseAuthComponent) : Child()
        class LoginChild(val component: LoginComponent) : Child()
        class RegisterChild(val component: RegisterComponent) : Child()
        class SsoPopoverChild(val component: SsoPopoverComponent) : Child()

        class EnterEmailChild(val component: EnterEmailComponent) : Child()

        class EnterRecoveryCodeChild(val component: EnterRecoveryCodeComponent) : Child()

        class UpdatePasswordChild(val component: UpdatePasswordComponent) : Child()

    }
}

class DefaultAuthRootComponent(
    componentContext: ComponentContext,
    private val onOutput: (AuthOutput) -> Unit,
    private val baseAuthComponentFactory: (ComponentContext, (BaseAuthOutput) -> Unit) -> BaseAuthComponent,
    private val loginComponentFactory: (ComponentContext, (LoginOutput) -> Unit) -> LoginComponent,
    private val registerComponentFactory: (ComponentContext, (RegisterOutput) -> Unit) -> RegisterComponent,
    private val ssoPopoverComponentFactory: (ComponentContext, (SsoPopoverOutput) -> Unit) -> SsoPopoverComponent,
    private val updatePasswordFactory: (ComponentContext, (UpdatePasswordOutput) -> Unit) -> UpdatePasswordComponent,
    private val enterEmailFactory: (ComponentContext, (EnterEmailOutput) -> Unit) -> EnterEmailComponent,
    private val enterRecoveryCodeFactory: (ComponentContext, (EnterRecoveryCodeOutput) -> Unit) -> EnterRecoveryCodeComponent,
) : AuthRootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<AuthConfiguration>()

    override val stack: Value<ChildStack<*, AuthRootComponent.Child>> =
        childStack(
            source = navigation,
            serializer = AuthConfiguration.serializer(),
            initialConfiguration = AuthConfiguration.BaseAuth,
            handleBackButton = true,
            childFactory = ::child,
        )

    private fun child(
        configuration: AuthConfiguration,
        componentContext: ComponentContext
    ): AuthRootComponent.Child {
        println("Creating child for configuration: $configuration")

        return when (configuration) {
            is AuthConfiguration.BaseAuth -> BaseAuthChild(
                baseAuthComponentFactory(componentContext, ::onBaseAuthOutput)
            )

            is AuthConfiguration.Login -> LoginChild(
                loginComponentFactory(componentContext, ::onLoginOutput)
            )

            is AuthConfiguration.Register -> RegisterChild(
                registerComponentFactory(componentContext, ::onRegisterOutput)
            )

            is AuthConfiguration.VkPopover -> SsoPopoverChild(
                ssoPopoverComponentFactory(componentContext, ::onSsoPopoverOutput)
            )

            is AuthConfiguration.UpdatePassword -> UpdatePasswordChild(
                updatePasswordFactory(componentContext, ::onUpdatePasswordOutput)
            )

            is AuthConfiguration.EnterEmail -> EnterEmailChild(
                enterEmailFactory(componentContext, ::onEnterEmailOutput)
            )

            is AuthConfiguration.EnterRecoveryCode -> EnterRecoveryCodeChild(
                enterRecoveryCodeFactory(componentContext, ::onEnterRecoveryCodeOutput)
            )

            AuthConfiguration.YandexPopover ->  SsoPopoverChild(
                ssoPopoverComponentFactory(componentContext, ::onSsoPopoverOutput)
            )
        }
    }

    private fun onBaseAuthOutput(output: BaseAuthOutput): Unit =
        when (output) {
            is BaseAuthOutput.NavigateToLogin -> navigation.pushNew(AuthConfiguration.Login)
            is BaseAuthOutput.NavigateToRegister -> navigation.pushNew(AuthConfiguration.Register)
            BaseAuthOutput.NavigateToVk -> navigation.pushNew(AuthConfiguration.VkPopover)
            BaseAuthOutput.NavigateToYandex -> navigation.pushNew(AuthConfiguration.YandexPopover)
        }

    private fun onEnterEmailOutput(output: EnterEmailOutput): Unit =
        when (output) {
            is EnterEmailOutput.NavigateToRecoveryCode -> navigation.pushNew(AuthConfiguration.EnterRecoveryCode)
            is EnterEmailOutput.NavigateBack -> navigation.pop()
        }

    private fun onEnterRecoveryCodeOutput(output: EnterRecoveryCodeOutput): Unit =
        when (output) {
            is EnterRecoveryCodeOutput.NavigateToUpdatePassword -> navigation.pushNew(
                AuthConfiguration.UpdatePassword
            )

            is EnterRecoveryCodeOutput.NavigateBack -> navigation.pop()
        }

    private fun onUpdatePasswordOutput(output: UpdatePasswordOutput): Unit =
        when (output) {
            is UpdatePasswordOutput.PasswordUpdated -> onOutput(AuthOutput.NavigateToOnboarding)
            is UpdatePasswordOutput.NavigateBack -> navigation.pop()
        }


    private fun onLoginOutput(output: LoginOutput): Unit =
        when (output) {
            is LoginOutput.NavigateToEnterEmail -> navigation.pushNew(AuthConfiguration.EnterEmail)
            is LoginOutput.AuthenticationSuccess -> onOutput(AuthOutput.NavigateToOnboarding)
            is LoginOutput.NavigateBack -> navigation.pop()
        }

    private fun onRegisterOutput(output: RegisterOutput): Unit =
        when (output) {
            is RegisterOutput.AuthenticationSuccess -> onOutput(AuthOutput.NavigateToOnboarding)
            is RegisterOutput.NavigateBack -> navigation.pop()
        }

    private fun onSsoPopoverOutput(output: SsoPopoverOutput): Unit =
        when (output) {
            is SsoPopoverOutput.AuthenticationSuccess -> onOutput(AuthOutput.NavigateToOnboarding)
            is SsoPopoverOutput.NavigateBack -> navigation.pop()
        }
}
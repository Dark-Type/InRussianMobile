package com.example.inrussian.components.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.replaceAll
import com.arkivanov.decompose.value.Value
import com.example.inrussian.components.auth.root.AuthRootComponent
import com.example.inrussian.components.auth.root.AuthOutput
import com.example.inrussian.components.main.root.MainRootComponent
import com.example.inrussian.components.main.root.MainOutput
import com.example.inrussian.components.onboarding.root.OnboardingRootComponent
import com.example.inrussian.components.onboarding.root.OnboardingOutput
import com.example.inrussian.navigation.configurations.Configuration
import com.example.inrussian.stores.root.RootIntent
import org.koin.core.component.KoinComponent
import com.example.inrussian.stores.root.RootStore
import org.koin.core.component.inject

interface RootComponent {
    val stack: Value<ChildStack<*, Child>>

    sealed class Child {
        class AuthChild(val component: AuthRootComponent) : Child()
        class OnboardingChild(val component: OnboardingRootComponent) : Child()
        class MainChild(val component: MainRootComponent) : Child()
    }
}

class DefaultRootComponent(
    componentContext: ComponentContext,
    private val authComponentFactory: (ComponentContext, (AuthOutput) -> Unit) -> AuthRootComponent,
    private val onboardingComponentFactory: (ComponentContext, (OnboardingOutput) -> Unit) -> OnboardingRootComponent,
    private val mainComponentFactory: (ComponentContext, (MainOutput) -> Unit) -> MainRootComponent,
) : RootComponent, ComponentContext by componentContext, KoinComponent {

    private val rootStore: RootStore by inject()

    private val navigation = StackNavigation<Configuration>()

    override val stack: Value<ChildStack<*, RootComponent.Child>> =
        childStack(
            source = navigation,
            serializer = Configuration.serializer(),
            initialConfiguration = Configuration.Auth,
            handleBackButton = true,
            childFactory = ::child
        )

    private fun child(
        configuration: Configuration,
        componentContext: ComponentContext
    ): RootComponent.Child =
        when (configuration) {
            is Configuration.Auth -> RootComponent.Child.AuthChild(
                authComponentFactory(componentContext.childContext("auth"), ::onAuthOutput)
            )

            is Configuration.Onboarding -> RootComponent.Child.OnboardingChild(
                onboardingComponentFactory(
                    componentContext.childContext("onboarding"),
                    ::onOnboardingOutput
                )
            )

            is Configuration.Main -> RootComponent.Child.MainChild(
                mainComponentFactory(componentContext.childContext("main"), ::onMainOutput)
            )
        }

    private fun onAuthOutput(output: AuthOutput) {
        when (output) {
            is AuthOutput.NavigateToOnboarding -> {
                rootStore.accept(RootIntent.ShowOnboarding)
                navigation.replaceAll(Configuration.Onboarding)
            }
        }
    }

    private fun onOnboardingOutput(output: OnboardingOutput) {
        when (output) {
            is OnboardingOutput.NavigateToMain -> {
                rootStore.accept(RootIntent.ShowMain)
                navigation.replaceAll(Configuration.Main)
            }

            OnboardingOutput.Back -> {
                rootStore.accept(RootIntent.ShowAuth)
                navigation.replaceAll(Configuration.Auth)
            }
        }
    }

    private fun onMainOutput(output: MainOutput) {
        when (output) {
            is MainOutput.NavigateBack -> {
                rootStore.accept(RootIntent.ShowAuth)
                navigation.replaceAll(Configuration.Auth)
            }
        }
    }
}
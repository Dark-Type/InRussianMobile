package com.example.inrussian.components.onboarding.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack

import com.arkivanov.decompose.value.Value
import com.example.inrussian.navigation.configurations.OnboardingConfiguration

import com.example.inrussian.components.onboarding.interactiveOnboarding.InteractiveOnboardingComponent
import com.example.inrussian.components.onboarding.interactiveOnboarding.InteractiveOnboardingOutput

interface OnboardingRootComponent {
    val stack: Value<ChildStack<*, Child>>

    sealed class Child {
        class InteractiveOnboardingChild(val component: InteractiveOnboardingComponent) : Child()
    }
}

class DefaultOnboardingRootComponent(
    componentContext: ComponentContext,
    private val onOutput: (OnboardingOutput) -> Unit,
    private val interactiveOnboardingComponentFactory: (ComponentContext, (InteractiveOnboardingOutput) -> Unit) -> InteractiveOnboardingComponent,
) : OnboardingRootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<OnboardingConfiguration>()

    override val stack: Value<ChildStack<*, OnboardingRootComponent.Child>> =
        childStack(
            source = navigation,
            serializer = OnboardingConfiguration.serializer(),
            initialConfiguration = OnboardingConfiguration.InteractiveOnboarding,
            handleBackButton = true,
            childFactory = ::child,
        )

    private fun child(
        configuration: OnboardingConfiguration,
        componentContext: ComponentContext
    ): OnboardingRootComponent.Child =
        when (configuration) {
            is OnboardingConfiguration.InteractiveOnboarding -> OnboardingRootComponent.Child.InteractiveOnboardingChild(
                interactiveOnboardingComponentFactory(componentContext, ::onInteractiveOnboardingOutput)
            )
        }

    private fun onInteractiveOnboardingOutput(output: InteractiveOnboardingOutput) {
        when (output) {
            is InteractiveOnboardingOutput.Finished -> onOutput(OnboardingOutput.NavigateToMain)
        }
    }
}
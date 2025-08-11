package com.example.inrussian.components.onboarding.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.value.Value
import com.example.inrussian.navigation.configurations.OnboardingConfiguration
import com.example.inrussian.components.onboarding.language.LanguageComponent
import com.example.inrussian.components.onboarding.language.LanguageOutput
import com.example.inrussian.components.onboarding.personalData.PersonalDataComponent
import com.example.inrussian.components.onboarding.personalData.PersonalDataOutput
import com.example.inrussian.components.onboarding.citizenship.CitizenshipComponent
import com.example.inrussian.components.onboarding.citizenship.CitizenshipOutput
import com.example.inrussian.components.onboarding.education.EducationComponent
import com.example.inrussian.components.onboarding.education.EducationOutput
import com.example.inrussian.components.onboarding.confirmation.ConfirmationComponent
import com.example.inrussian.components.onboarding.confirmation.ConfirmationOutput
import com.example.inrussian.components.onboarding.interactiveOnboarding.InteractiveOnboardingComponent
import com.example.inrussian.components.onboarding.interactiveOnboarding.InteractiveOnboardingOutput

interface OnboardingRootComponent {
    val stack: Value<ChildStack<*, Child>>

    sealed class Child {
        class LanguageChild(val component: LanguageComponent) : Child()
        class PersonalDataChild(val component: PersonalDataComponent) : Child()
        class CitizenshipChild(val component: CitizenshipComponent) : Child()
        class EducationChild(val component: EducationComponent) : Child()
        class ConfirmationChild(val component: ConfirmationComponent) : Child()
        class InteractiveOnboardingChild(val component: InteractiveOnboardingComponent) : Child()
    }
}

class DefaultOnboardingRootComponent(
    componentContext: ComponentContext,
    private val onOutput: (OnboardingOutput) -> Unit,
    private val languageComponentFactory: (ComponentContext, (LanguageOutput) -> Unit) -> LanguageComponent,
    private val personalDataComponentFactory: (ComponentContext, (PersonalDataOutput) -> Unit) -> PersonalDataComponent,
    private val citizenshipComponentFactory: (ComponentContext, (CitizenshipOutput) -> Unit) -> CitizenshipComponent,
    private val educationComponentFactory: (ComponentContext, (EducationOutput) -> Unit) -> EducationComponent,
    private val confirmationComponentFactory: (ComponentContext, (ConfirmationOutput) -> Unit) -> ConfirmationComponent,
    private val interactiveOnboardingComponentFactory: (ComponentContext, (InteractiveOnboardingOutput) -> Unit) -> InteractiveOnboardingComponent,
) : OnboardingRootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<OnboardingConfiguration>()

    override val stack: Value<ChildStack<*, OnboardingRootComponent.Child>> =
        childStack(
            source = navigation,
            serializer = OnboardingConfiguration.serializer(),
            initialConfiguration = OnboardingConfiguration.LanguageEmpty,
            handleBackButton = true,
            childFactory = ::child,
        )

    private fun child(
        configuration: OnboardingConfiguration,
        componentContext: ComponentContext
    ): OnboardingRootComponent.Child =
        when (configuration) {
            is OnboardingConfiguration.LanguageEmpty,
            is OnboardingConfiguration.LanguageFilled -> OnboardingRootComponent.Child.LanguageChild(
                languageComponentFactory(componentContext, ::onLanguageOutput)
            )

            is OnboardingConfiguration.PersonalDataEmpty,
            is OnboardingConfiguration.PersonalDataFilled -> OnboardingRootComponent.Child.PersonalDataChild(
                personalDataComponentFactory(componentContext, ::onPersonalDataOutput)
            )

            is OnboardingConfiguration.CitizenshipEmpty,
            is OnboardingConfiguration.CitizenshipFilled -> OnboardingRootComponent.Child.CitizenshipChild(
                citizenshipComponentFactory(componentContext, ::onCitizenshipOutput)
            )

            is OnboardingConfiguration.EducationEmpty,
            is OnboardingConfiguration.EducationFilled -> OnboardingRootComponent.Child.EducationChild(
                educationComponentFactory(componentContext, ::onEducationOutput)
            )

            is OnboardingConfiguration.Confirmation -> OnboardingRootComponent.Child.ConfirmationChild(
                confirmationComponentFactory(componentContext, ::onConfirmationOutput)
            )

            is OnboardingConfiguration.InteractiveOnboarding -> OnboardingRootComponent.Child.InteractiveOnboardingChild(
                interactiveOnboardingComponentFactory(
                    componentContext,
                    ::onInteractiveOnboardingOutput
                )
            )
        }

    private fun onLanguageOutput(output: LanguageOutput) {
        when (output) {
            is LanguageOutput.Filled -> navigation.pushNew(OnboardingConfiguration.PersonalDataEmpty)
            is LanguageOutput.Back -> navigation.pop()
        }
    }

    private fun onPersonalDataOutput(output: PersonalDataOutput) {
        when (output) {
            is PersonalDataOutput.Filled -> navigation.pushNew(OnboardingConfiguration.CitizenshipEmpty)
            is PersonalDataOutput.Back -> navigation.pop()
        }
    }

    private fun onCitizenshipOutput(output: CitizenshipOutput) {
        when (output) {
            is CitizenshipOutput.Filled -> navigation.pushNew(OnboardingConfiguration.EducationEmpty)
            is CitizenshipOutput.Back -> navigation.pop()
        }
    }

    private fun onEducationOutput(output: EducationOutput) {
        when (output) {
            is EducationOutput.Filled -> navigation.pushNew(OnboardingConfiguration.Confirmation)
            is EducationOutput.Back -> navigation.pop()
        }
    }

    private fun onConfirmationOutput(output: ConfirmationOutput) {
        when (output) {
            is ConfirmationOutput.Confirmed -> navigation.pushNew(OnboardingConfiguration.InteractiveOnboarding)
        }
    }

    private fun onInteractiveOnboardingOutput(output: InteractiveOnboardingOutput) {
        when (output) {
            is InteractiveOnboardingOutput.Finished -> onOutput(OnboardingOutput.NavigateToMain)
        }
    }
}
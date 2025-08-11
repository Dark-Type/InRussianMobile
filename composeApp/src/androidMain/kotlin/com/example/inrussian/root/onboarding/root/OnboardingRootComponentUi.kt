package com.example.inrussian.root.onboarding.root

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.example.inrussian.components.onboarding.root.OnboardingRootComponent
import com.example.inrussian.root.onboarding.language.LanguageComponentUi
import com.example.inrussian.root.onboarding.personalData.PersonalDataComponentUi
import com.example.inrussian.root.onboarding.citizenship.CitizenshipComponentUi
import com.example.inrussian.root.onboarding.education.EducationComponentUi
import com.example.inrussian.root.onboarding.confirmation.ConfirmationComponentUi
import com.example.inrussian.root.onboarding.onboarding.InteractiveOnboardingComponentUi
@Composable
fun OnboardingRootComponentUi(component: OnboardingRootComponent) {
    val stack by component.stack.subscribeAsState()
    when (val child = stack.active.instance) {
        is OnboardingRootComponent.Child.LanguageChild ->
            LanguageComponentUi(child.component)

        is OnboardingRootComponent.Child.PersonalDataChild ->
            PersonalDataComponentUi(child.component)

        is OnboardingRootComponent.Child.CitizenshipChild ->
            CitizenshipComponentUi(child.component)

        is OnboardingRootComponent.Child.EducationChild ->
            EducationComponentUi(child.component)

        is OnboardingRootComponent.Child.ConfirmationChild ->
            ConfirmationComponentUi(child.component)

        is OnboardingRootComponent.Child.InteractiveOnboardingChild ->
            InteractiveOnboardingComponentUi(child.component)
    }
}
package com.example.inrussian.root.onboarding.root

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.example.inrussian.components.onboarding.root.OnboardingRootComponent
import com.example.inrussian.root.auth.register.CitizenshipDate
import com.example.inrussian.root.auth.register.EducationUi
import com.example.inrussian.root.auth.register.FormSaveUi
import com.example.inrussian.root.auth.register.LanguageChooseUi
import com.example.inrussian.root.auth.register.PersonaDataUi
import com.example.inrussian.root.onboarding.onboarding.InteractiveOnboardingComponentUi

@Composable
fun OnboardingRootComponentUi(component: OnboardingRootComponent) {
    val stack by component.stack.subscribeAsState()
    when (val child = stack.active.instance) {
        is OnboardingRootComponent.Child.LanguageChild ->
            LanguageChooseUi(child.component)

        is OnboardingRootComponent.Child.PersonalDataChild ->
            PersonaDataUi(child.component)

        is OnboardingRootComponent.Child.CitizenshipChild ->
            CitizenshipDate(child.component)

        is OnboardingRootComponent.Child.EducationChild ->
            EducationUi(child.component)

        is OnboardingRootComponent.Child.ConfirmationChild ->
            FormSaveUi(child.component)

        is OnboardingRootComponent.Child.InteractiveOnboardingChild ->
            InteractiveOnboardingComponentUi(child.component)
    }
}
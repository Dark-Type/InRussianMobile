package com.example.inrussian.root.auth.root

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.example.inrussian.components.auth.root.AuthRootComponent
import com.example.inrussian.components.onboarding.root.OnboardingRootComponent
import com.example.inrussian.root.auth.base.BaseAuthUi
import com.example.inrussian.root.auth.login.LoginUi
import com.example.inrussian.root.auth.recovery.code.EnterRecoveryCodeUi
import com.example.inrussian.root.auth.recovery.email.EnterEmailUi
import com.example.inrussian.root.auth.recovery.password.UpdatePasswordUi
import com.example.inrussian.root.auth.register.CitizenshipDate
import com.example.inrussian.root.auth.register.EducationUi
import com.example.inrussian.root.auth.register.FormSaveUi
import com.example.inrussian.root.auth.register.LanguageChooseUi
import com.example.inrussian.root.auth.register.PersonaDataUi
import com.example.inrussian.root.auth.register.RegisterUi
import com.example.inrussian.root.auth.sso.SsoPopoverUi

@Composable
fun AuthRootComponentUi(component: AuthRootComponent) {

    val stack by component.stack.subscribeAsState()
    when (val child = stack.active.instance) {
        is AuthRootComponent.Child.BaseAuthChild -> BaseAuthUi(child.component)
        is AuthRootComponent.Child.LoginChild -> LoginUi(child.component)
        is AuthRootComponent.Child.RegisterChild -> RegisterUi(child.component)
        is AuthRootComponent.Child.SsoPopoverChild -> SsoPopoverUi(child.component)
        is AuthRootComponent.Child.EnterEmailChild -> EnterEmailUi(child.component)
        is AuthRootComponent.Child.EnterRecoveryCodeChild -> EnterRecoveryCodeUi(child.component)
        is AuthRootComponent.Child.UpdatePasswordChild -> UpdatePasswordUi(child.component)
        is AuthRootComponent.Child.LanguageChild -> LanguageChooseUi(child.component)
        is AuthRootComponent.Child.PersonalDataChild -> PersonaDataUi(child.component)
        is AuthRootComponent.Child.CitizenshipChild -> CitizenshipDate(child.component)
        is AuthRootComponent.Child.EducationChild -> EducationUi(child.component)
        is AuthRootComponent.Child.ConfirmationChild -> FormSaveUi(child.component)
    }
}
package com.example.inrussian.di

import com.arkivanov.decompose.ComponentContext
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
import com.example.inrussian.components.auth.root.AuthOutput
import com.example.inrussian.components.auth.root.AuthRootComponent
import com.example.inrussian.components.auth.ssoPopover.SsoPopoverComponent
import com.example.inrussian.components.auth.ssoPopover.SsoPopoverOutput
import com.example.inrussian.components.main.home.CourseDetailsComponent
import com.example.inrussian.components.main.home.CourseDetailsOutput
import com.example.inrussian.components.main.home.HomeComponent
import com.example.inrussian.components.main.home.HomeOutput
import com.example.inrussian.components.main.profile.AboutComponent
import com.example.inrussian.components.main.profile.AboutOutput
import com.example.inrussian.components.main.profile.EditProfileComponent
import com.example.inrussian.components.main.profile.EditProfileOutput
import com.example.inrussian.components.main.profile.PrivacyPolicyComponent
import com.example.inrussian.components.main.profile.PrivacyPolicyOutput
import com.example.inrussian.components.main.profile.ProfileComponent
import com.example.inrussian.components.main.profile.ProfileOutput
import com.example.inrussian.components.main.root.MainOutput
import com.example.inrussian.components.main.root.MainRootComponent
import com.example.inrussian.components.main.train.ThemeTasksComponent
import com.example.inrussian.components.main.train.TrainComponent
import com.example.inrussian.components.main.train.TrainCoursesListComponent
import com.example.inrussian.components.main.train.TrainOutput
import com.example.inrussian.components.onboarding.citizenship.CitizenshipComponent
import com.example.inrussian.components.onboarding.citizenship.CitizenshipOutput
import com.example.inrussian.components.onboarding.confirmation.ConfirmationComponent
import com.example.inrussian.components.onboarding.confirmation.ConfirmationOutput
import com.example.inrussian.components.onboarding.education.EducationComponent
import com.example.inrussian.components.onboarding.education.EducationOutput
import com.example.inrussian.components.onboarding.interactiveOnboarding.InteractiveOnboardingComponent
import com.example.inrussian.components.onboarding.interactiveOnboarding.InteractiveOnboardingOutput
import com.example.inrussian.components.onboarding.language.LanguageComponent
import com.example.inrussian.components.onboarding.language.LanguageOutput
import com.example.inrussian.components.onboarding.personalData.PersonalDataComponent
import com.example.inrussian.components.onboarding.personalData.PersonalDataOutput
import com.example.inrussian.components.onboarding.root.OnboardingOutput
import com.example.inrussian.components.onboarding.root.OnboardingRootComponent
import org.koin.core.qualifier.named

typealias AuthRootFactory = (ComponentContext, (AuthOutput) -> Unit) -> AuthRootComponent
typealias OnboardingRootFactory = (ComponentContext, (OnboardingOutput) -> Unit) -> OnboardingRootComponent
typealias MainRootFactory = (ComponentContext, (MainOutput) -> Unit) -> MainRootComponent

val QAuthRootFactory = named("AuthRootFactory")
val QOnboardingRootFactory = named("OnboardingRootFactory")
val QMainRootFactory = named("MainRootFactory")

// Auth subtree
typealias BaseAuthFactory = (ComponentContext, (BaseAuthOutput) -> Unit) -> BaseAuthComponent
typealias LoginFactory = (ComponentContext, (LoginOutput) -> Unit) -> LoginComponent
typealias EnterEmailFactory = (ComponentContext, (EnterEmailOutput) -> Unit) -> EnterEmailComponent
typealias EnterRecoveryCodeFactory = (ComponentContext, (EnterRecoveryCodeOutput) -> Unit) -> EnterRecoveryCodeComponent
typealias UpdatePasswordFactory = (ComponentContext, (UpdatePasswordOutput) -> Unit) -> UpdatePasswordComponent
typealias RegisterFactory = (ComponentContext, (RegisterOutput) -> Unit) -> RegisterComponent
typealias SsoPopoverFactory = (ComponentContext, (SsoPopoverOutput) -> Unit) -> SsoPopoverComponent


val QBaseAuthFactory = named("BaseAuthFactory")
val QRegisterFactory = named("RegisterFactory")
val QRegisterStoreFactory = named("RegisterStoreFactory")
val QSsoPopoverFactory = named("SsoPopoverFactory")
val QLoginFactory = named("LoginFactory")
val QEnterEmailFactory = named("EnterEmailFactory")
val QEnterRecoveryCodeFactory = named("EnterRecoveryCodeFactory")
val QUpdatePasswordFactory = named("UpdatePasswordFactory")
val QLoginStoreFactory = named("LoginStoreFactory")
val QRecoveryStoreFactory = named("RecoveryStoreFactory")

typealias LanguageFactory = (ComponentContext, (LanguageOutput) -> Unit) -> LanguageComponent

val QLanguageFactory = named("LanguageFactory")

typealias PersonalDataFactory = (ComponentContext, (PersonalDataOutput) -> Unit) -> PersonalDataComponent

val QPersonalDataFactory = named("PersonalDataFactory")

typealias CitizenshipFactory = (ComponentContext, (CitizenshipOutput) -> Unit) -> CitizenshipComponent

val QCitizenshipFactory = named("CitizenshipFactory")

typealias EducationFactory = (ComponentContext, (EducationOutput) -> Unit) -> EducationComponent

val QEducationFactory = named("EducationFactory")

typealias ConfirmationFactory = (ComponentContext, (ConfirmationOutput) -> Unit) -> ConfirmationComponent

val QConfirmationFactory = named("ConfirmationFactory")

typealias InteractiveOnboardingFactory = (ComponentContext, (InteractiveOnboardingOutput) -> Unit) -> InteractiveOnboardingComponent

val QInteractiveOnboardingFactory = named("InteractiveOnboardingFactory")

typealias HomeFactory = (ComponentContext, (HomeOutput) -> Unit) -> HomeComponent
typealias TrainFactory = (ComponentContext, (TrainOutput) -> Unit) -> TrainComponent
typealias ProfileFactory = (ComponentContext, (ProfileOutput) -> Unit) -> ProfileComponent

typealias CourseDetailsComponentFactory = (
    ComponentContext,
    courseId: String,
    onOutput: (CourseDetailsOutput) -> Unit
) -> CourseDetailsComponent

val QCourseDetailsComponentFactory = named("CourseDetailsComponentFactory")

val QSectionDetailComponentFactory = named("SectionDetailComponentFactory")


val QTrainStoreFactory = named("TrainStoreFactory")

val QTasksComponentFactory = named("TasksComponentFactory")
val QHomeFactory = named("HomeFactory")
val QTrainFactory = named("TrainFactory")
val QProfileFactory = named("ProfileFactory")

typealias EditProfileComponentFactory =
            (
            componentContext: ComponentContext,
            onOutput: (EditProfileOutput) -> Unit
        ) -> EditProfileComponent

typealias AboutComponentFactory =
            (
            componentContext: ComponentContext,
            onOutput: (AboutOutput) -> Unit
        ) -> AboutComponent

typealias PrivacyPolicyComponentFactory =
            (
            componentContext: ComponentContext,
            onOutput: (PrivacyPolicyOutput) -> Unit
        ) -> PrivacyPolicyComponent

val QEditProfileComponentFactory = named("EditProfileComponentFactory")
val QAboutComponentFactory = named("AboutComponentFactory")
val QPrivacyPolicyComponentFactory = named("PrivacyPolicyComponentFactory")


val QTrainCoursesListFactory = named("TrainCoursesListFactory")
val QThemeTasksFactory = named("ThemeTasksFactory")

typealias TrainCoursesListFactory = (ComponentContext, (String, List<String>) -> Unit) -> TrainCoursesListComponent
typealias ThemeTasksFactory = (ComponentContext, String, () -> Unit, () -> Unit) -> ThemeTasksComponent

val QTrainComponentFactory = named("TrainComponentFactory")
val QTrainCoursesListComponentFactory = named("TrainCoursesListComponentFactory")
val QThemeTasksComponentFactory = named("ThemeTasksComponentFactory")

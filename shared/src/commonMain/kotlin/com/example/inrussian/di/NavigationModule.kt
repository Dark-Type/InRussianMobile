package com.example.inrussian.di

import com.arkivanov.decompose.ComponentContext
import com.example.inrussian.components.auth.base.DefaultBaseAuthComponent
import com.example.inrussian.components.auth.login.DefaultLoginComponent
import com.example.inrussian.components.auth.passwordRecovery.enterEmail.DefaultEnterEmailComponent
import com.example.inrussian.components.auth.passwordRecovery.enterRecoveryCode.DefaultEnterRecoveryCodeComponent
import com.example.inrussian.components.auth.passwordRecovery.updatePassword.DefaultUpdatePasswordComponent
import com.example.inrussian.components.auth.register.DefaultRegisterComponent
import com.example.inrussian.components.auth.root.DefaultAuthRootComponent
import com.example.inrussian.components.auth.ssoPopover.DefaultSsoPopoverComponent
import com.example.inrussian.components.main.home.DefaultCourseDetailsComponent
import com.example.inrussian.components.main.home.DefaultHomeComponent
import com.example.inrussian.components.main.home.HomeOutput
import com.example.inrussian.components.main.profile.DefaultAboutComponent
import com.example.inrussian.components.main.profile.DefaultEditProfileComponent
import com.example.inrussian.components.main.profile.DefaultPrivacyPolicyComponent
import com.example.inrussian.components.main.profile.DefaultProfileComponent
import com.example.inrussian.components.main.root.DefaultMainRootComponent
import com.example.inrussian.components.main.train.DefaultSectionDetailComponent
import com.example.inrussian.components.main.train.DefaultTasksComponent
import com.example.inrussian.components.main.train.DefaultTrainComponent
import com.example.inrussian.components.main.train.TrainComponentImpl
import com.example.inrussian.components.onboarding.citizenship.DefaultCitizenshipComponent
import com.example.inrussian.components.onboarding.confirmation.DefaultConfirmationComponent
import com.example.inrussian.components.onboarding.education.DefaultEducationComponent
import com.example.inrussian.components.onboarding.interactiveOnboarding.DefaultInteractiveOnboardingComponent
import com.example.inrussian.components.onboarding.language.DefaultLanguageComponent
import com.example.inrussian.components.onboarding.personalData.DefaultPersonalDataComponent
import com.example.inrussian.components.onboarding.root.DefaultOnboardingRootComponent
import com.example.inrussian.components.root.DefaultRootComponent
import com.example.inrussian.components.root.RootComponent
import com.example.inrussian.di.main.QAboutText
import com.example.inrussian.di.main.QPrivacyText
import org.koin.dsl.module


val navigationModule = module {

    factory<(ComponentContext) -> RootComponent> {
        { componentContext ->
            DefaultRootComponent(
                componentContext = componentContext,
                authComponentFactory = get(QAuthRootFactory),
                onboardingComponentFactory = get(QOnboardingRootFactory),
                mainComponentFactory = get(QMainRootFactory),
            )
        }
    }
    factory<MainRootFactory>(qualifier = QMainRootFactory) {
        { componentContext, onOutput ->
            DefaultMainRootComponent(
                componentContext = componentContext,
                onOutput = onOutput,
                homeFactory = get(QHomeFactory),
                trainFactory = get(QTrainFactory),
                profileFactory = get(QProfileFactory)
            )
        }
    }

    factory<AuthRootFactory>(qualifier = QAuthRootFactory) {
        { componentContext, onOutput ->
            DefaultAuthRootComponent(
                componentContext = componentContext,
                onOutput = onOutput,
                baseAuthComponentFactory = get(QBaseAuthFactory),
                loginComponentFactory = get(QLoginFactory),
                registerComponentFactory = get(QRegisterFactory),
                ssoPopoverComponentFactory = get(QSsoPopoverFactory),
                enterEmailFactory = get(QEnterEmailFactory),
                updatePasswordFactory = get(QUpdatePasswordFactory),
                enterRecoveryCodeFactory = get(QEnterRecoveryCodeFactory),
                languageComponentFactory = get(QLanguageFactory),
                personalDataComponentFactory = get(QPersonalDataFactory),
                citizenshipComponentFactory = get(QCitizenshipFactory),
                educationComponentFactory = get(QEducationFactory),
                confirmationComponentFactory = get(QConfirmationFactory)
            )
        }
    }
    factory<OnboardingRootFactory>(qualifier = QOnboardingRootFactory) {
        { componentContext, onOutput ->
            DefaultOnboardingRootComponent(
                componentContext = componentContext,
                onOutput = onOutput,

                interactiveOnboardingComponentFactory = get(QInteractiveOnboardingFactory)
            )
        }
    }


    factory<BaseAuthFactory>(qualifier = QBaseAuthFactory) {
        { componentContext, onOutput ->
            DefaultBaseAuthComponent(
                componentContext = componentContext,
                onOutput = onOutput,
                authRepository = get()
            )
        }
    }

    factory<LoginFactory>(qualifier = QLoginFactory) {
        { componentContext, onOutput ->
            DefaultLoginComponent(componentContext, onOutput, get())
        }
    }



    factory<RegisterFactory>(qualifier = QRegisterFactory) {
        { componentContext, onOutput ->
            DefaultRegisterComponent(
                componentContext = componentContext,
                onOutput = onOutput,
                store = get()
            )
        }
    }

    factory<SsoPopoverFactory>(qualifier = QSsoPopoverFactory) {
        { componentContext, onOutput ->
            DefaultSsoPopoverComponent(
                componentContext = componentContext,
                onOutput = onOutput
            )
        }
    }
    factory<EnterEmailFactory>(qualifier = QEnterEmailFactory) {
        { componentContext, onOutput ->
            DefaultEnterEmailComponent(
                componentContext = componentContext,
                onOutput = onOutput,
                store = get()
            )
        }
    }
    factory<EnterRecoveryCodeFactory>(qualifier = QEnterRecoveryCodeFactory) {
        { componentContext, onOutput ->
            DefaultEnterRecoveryCodeComponent(
                componentContext = componentContext,
                onOutput = onOutput,
                store = get()
            )
        }
    }


    factory<UpdatePasswordFactory>(qualifier = QUpdatePasswordFactory) {
        { componentContext, onOutput ->
            DefaultUpdatePasswordComponent(
                componentContext = componentContext,
                onOutput = onOutput,
                authRepository = get()
            )
        }
    }

    factory<LanguageFactory>(qualifier = QLanguageFactory) {
        { componentContext, onOutput ->
            DefaultLanguageComponent(componentContext, onOutput)
        }
    }
    factory<PersonalDataFactory>(qualifier = QPersonalDataFactory) {
        { componentContext, onOutput ->
            DefaultPersonalDataComponent(onOutput)
        }
    }
    factory<CitizenshipFactory>(qualifier = QCitizenshipFactory) {
        { componentContext, onOutput ->
            DefaultCitizenshipComponent(onOutput)
        }
    }
    factory<EducationFactory>(qualifier = QEducationFactory) {
        { componentContext, onOutput ->
            DefaultEducationComponent(onOutput)
        }
    }
    factory<ConfirmationFactory>(qualifier = QConfirmationFactory) {
        { componentContext, onOutput ->
            DefaultConfirmationComponent(onOutput)
        }
    }
    factory<InteractiveOnboardingFactory>(qualifier = QInteractiveOnboardingFactory) {
        { componentContext, onOutput ->
            DefaultInteractiveOnboardingComponent(onOutput)
        }
    }
    factory<CourseDetailsComponentFactory>(QCourseDetailsComponentFactory) {
        { componentContext, courseId, onOutput ->
            DefaultCourseDetailsComponent(
                componentContext = componentContext,
                courseId = courseId,
                repository = get(),
                onOutput = onOutput
            )
        }
    }

    factory<TasksComponentFactory>(QTasksComponentFactory) {
        { componentContext, sectionId, option, onOutput ->
            DefaultTasksComponent(
                componentContext = componentContext,
                repository = get(),
                sectionId = sectionId,
                option = option,
                onOutput = onOutput
            )
        }
    }

    factory<SectionDetailComponentFactory>(QSectionDetailComponentFactory) {
        { componentContext, sectionId, onOutput ->
            DefaultSectionDetailComponent(
                componentContext = componentContext,
                repository = get(),
                sectionId = sectionId,
                onOutput = onOutput,
                tasksFactory = get(QTasksComponentFactory)
            )
        }
    }

    factory<TrainComponentFactory>(QTrainComponentFactory) {
        { componentContext, sectionId, onOutput ->
            TrainComponentImpl(
                componentContext = componentContext,
                storeModule =

            )
        }
    }
    factory<EditProfileComponentFactory>(QEditProfileComponentFactory) {
        { componentContext, onOutput ->
            DefaultEditProfileComponent(
                componentContext = componentContext,
                userRepository = get(),
                onOutput = onOutput
            )
        }
    }

    factory<AboutComponentFactory>(QAboutComponentFactory) {
        { componentContext, onOutput ->
            DefaultAboutComponent(
                componentContext = componentContext,
                aboutText = get(QAboutText),
                onOutput = onOutput
            )
        }
    }
    factory<PrivacyPolicyComponentFactory>(QPrivacyPolicyComponentFactory) {
        { componentContext, onOutput ->
            DefaultPrivacyPolicyComponent(
                componentContext = componentContext,
                policyText = get(QPrivacyText),
                onOutput = onOutput
            )
        }
    }

    factory<HomeFactory>(qualifier = QHomeFactory) {
        { componentContext: ComponentContext, onOutput: (HomeOutput) -> Unit ->
            DefaultHomeComponent(
                componentContext = componentContext,
                onOutput = onOutput,
                courseDetailsComponentFactory = get(QCourseDetailsComponentFactory),
                repository = get()
            )
        }
    }

    factory<TrainFactory>(QTrainFactory) {
        { componentContext, onOutput ->
            DefaultTrainComponent(
                componentContext = componentContext,
                onOutput = onOutput,
                sectionDetailComponentFactory = get(QSectionDetailComponentFactory),
                repository = get()
            )
        }
    }

    factory<ProfileFactory>(QProfileFactory) {
        { componentContext, onOutput ->
            DefaultProfileComponent(
                componentContext = componentContext,
                onOutput = onOutput,
                userRepository = get(),
                badgeRepository = get(),
                settingsRepository = get(),
                aboutText = get(QAboutText),
                privacyText = get(QPrivacyText),
                editProfileFactory = get(QEditProfileComponentFactory),
                aboutFactory = get(QAboutComponentFactory),
                privacyPolicyFactory = get(QPrivacyPolicyComponentFactory)
            )
        }
    }
}
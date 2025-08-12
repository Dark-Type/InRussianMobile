package com.example.inrussian.di

import com.arkivanov.decompose.ComponentContext
import com.example.inrussian.components.auth.base.DefaultBaseAuthComponent
import com.example.inrussian.components.auth.login.DefaultLoginComponent
import com.example.inrussian.components.auth.ssoPopover.DefaultSsoPopoverComponent
import com.example.inrussian.components.auth.register.DefaultRegisterComponent
import com.example.inrussian.components.auth.passwordRecovery.enterEmail.DefaultEnterEmailComponent
import com.example.inrussian.components.auth.passwordRecovery.enterRecoveryCode.DefaultEnterRecoveryCodeComponent
import com.example.inrussian.components.auth.passwordRecovery.updatePassword.DefaultUpdatePasswordComponent
import com.example.inrussian.components.auth.root.DefaultAuthRootComponent
import com.example.inrussian.components.main.home.DefaultCourseDetailsComponent
import com.example.inrussian.components.main.home.DefaultHomeComponent
import com.example.inrussian.components.main.profile.DefaultAboutComponent
import com.example.inrussian.components.main.profile.DefaultEditProfileComponent
import com.example.inrussian.components.main.profile.DefaultPrivacyPolicyComponent
import com.example.inrussian.components.main.profile.DefaultProfileComponent
import com.example.inrussian.components.main.root.DefaultMainRootComponent
import com.example.inrussian.components.main.train.DefaultSectionDetailComponent
import com.example.inrussian.components.main.train.DefaultTasksComponent
import com.example.inrussian.components.main.train.DefaultTrainComponent
import com.example.inrussian.components.onboarding.language.DefaultLanguageComponent
import com.example.inrussian.components.onboarding.root.DefaultOnboardingRootComponent
import com.example.inrussian.components.root.DefaultRootComponent
import com.example.inrussian.components.root.RootComponent
import com.example.inrussian.components.onboarding.citizenship.DefaultCitizenshipComponent
import com.example.inrussian.components.onboarding.confirmation.DefaultConfirmationComponent
import com.example.inrussian.components.onboarding.education.DefaultEducationComponent
import com.example.inrussian.components.onboarding.interactiveOnboarding.DefaultInteractiveOnboardingComponent
import com.example.inrussian.components.onboarding.personalData.DefaultPersonalDataComponent
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
                enterRecoveryCodeFactory = get(QEnterRecoveryCodeFactory)
            )
        }
    }
    factory<OnboardingRootFactory>(qualifier = QOnboardingRootFactory) {
        { componentContext, onOutput ->
            DefaultOnboardingRootComponent(
                componentContext = componentContext,
                onOutput = onOutput,
                languageComponentFactory = get(QLanguageFactory),
                personalDataComponentFactory = get(QPersonalDataFactory),
                citizenshipComponentFactory = get(QCitizenshipFactory),
                educationComponentFactory = get(QEducationFactory),
                confirmationComponentFactory = get(QConfirmationFactory),
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
            DefaultLoginComponent(
                componentContext = componentContext,
                onOutput = onOutput,
                authRepository = get()
            )
        }
    }



    factory<RegisterFactory>(qualifier = QRegisterFactory) {
        { componentContext, onOutput ->
            DefaultRegisterComponent(
                componentContext = componentContext,
                onOutput = onOutput,
                authRepository = get()
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
                authRepository = get()
            )
        }
    }
    factory<EnterRecoveryCodeFactory>(qualifier = QEnterRecoveryCodeFactory) {
        { componentContext, onOutput ->
            DefaultEnterRecoveryCodeComponent(
                componentContext = componentContext,
                onOutput = onOutput,
                authRepository = get()
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
    factory<CourseDetailsComponentFactory>(qualifier = QCourseDetailsComponentFactory) {
        { componentContext, courseId, onOutput ->
            DefaultCourseDetailsComponent(
                componentContext = componentContext,
                courseId = courseId,
                onOutput = onOutput
            )
        }
    }

    factory<TasksComponentFactory>(qualifier = QTasksComponentFactory) {
        { componentContext, sectionId, option, onOutput ->
            DefaultTasksComponent(
                componentContext = componentContext,
                sectionId = sectionId,
                option = option,
                onOutput = onOutput
            )
        }
    }

    factory<SectionDetailComponentFactory>(qualifier = QSectionDetailComponentFactory) {
        { componentContext, sectionId, onOutput ->
            DefaultSectionDetailComponent(
                componentContext = componentContext,
                sectionId = sectionId,
                onOutput = onOutput,
                tasksFactory = get(QTasksComponentFactory)
            )
        }
    }
    factory<EditProfileComponentFactory>(qualifier = QEditProfileComponentFactory) {
        { componentContext, onOutput ->
            DefaultEditProfileComponent(
                componentContext = componentContext,
                onOutput = onOutput
            )
        }
    }

    factory<AboutComponentFactory>(qualifier = QAboutComponentFactory) {
        { componentContext, onOutput ->
            DefaultAboutComponent(
                componentContext = componentContext,
                onOutput = onOutput
            )
        }
    }

    factory<PrivacyPolicyComponentFactory>(qualifier = QPrivacyPolicyComponentFactory) {
        { componentContext, onOutput ->
            DefaultPrivacyPolicyComponent(
                componentContext = componentContext,
                onOutput = onOutput
            )
        }
    }

    factory<HomeFactory>(qualifier = QHomeFactory) {
        { componentContext, onOutput ->
            DefaultHomeComponent(
                componentContext = componentContext,
                onOutput = onOutput,
                courseDetailsComponentFactory = get(QCourseDetailsComponentFactory),
            )
        }
    }

    factory<TrainFactory>(qualifier = QTrainFactory) {
        { componentContext, onOutput ->
            DefaultTrainComponent(
                componentContext = componentContext,
                onOutput = onOutput,
                sectionDetailComponentFactory = get(QSectionDetailComponentFactory),
            )
        }
    }

    factory<ProfileFactory>(qualifier = QProfileFactory) {
        { componentContext, onOutput ->
            DefaultProfileComponent(
                componentContext = componentContext,
                onOutput = onOutput,
                userRepository = get(),
                editProfileFactory = get(QEditProfileComponentFactory),
                aboutFactory = get(QAboutComponentFactory),
                privacyPolicyFactory = get(QPrivacyPolicyComponentFactory)
            )
        }
    }
}
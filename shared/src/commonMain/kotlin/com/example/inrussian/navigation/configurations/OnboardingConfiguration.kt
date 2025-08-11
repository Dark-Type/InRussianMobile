package com.example.inrussian.navigation.configurations

import kotlinx.serialization.Serializable

@Serializable
sealed class OnboardingConfiguration {
    @Serializable
    data object LanguageEmpty : OnboardingConfiguration()

    @Serializable
    data object LanguageFilled : OnboardingConfiguration()

    @Serializable
    data object PersonalDataEmpty : OnboardingConfiguration()

    @Serializable
    data object PersonalDataFilled : OnboardingConfiguration()

    @Serializable
    data object CitizenshipEmpty : OnboardingConfiguration()

    @Serializable
    data object CitizenshipFilled : OnboardingConfiguration()

    @Serializable
    data object EducationEmpty : OnboardingConfiguration()

    @Serializable
    data object EducationFilled : OnboardingConfiguration()

    @Serializable
    data object Confirmation : OnboardingConfiguration()

    @Serializable
    data object InteractiveOnboarding : OnboardingConfiguration()
}
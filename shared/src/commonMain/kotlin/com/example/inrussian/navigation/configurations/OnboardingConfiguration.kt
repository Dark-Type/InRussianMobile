package com.example.inrussian.navigation.configurations

import kotlinx.serialization.Serializable

@Serializable
sealed class OnboardingConfiguration {
    @Serializable
    data object InteractiveOnboarding : OnboardingConfiguration()
}
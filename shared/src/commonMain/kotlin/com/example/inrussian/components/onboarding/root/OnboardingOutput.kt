package com.example.inrussian.components.onboarding.root

sealed class OnboardingOutput {
    object NavigateToMain : OnboardingOutput()
    object Back : OnboardingOutput()
}
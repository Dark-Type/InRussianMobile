package com.example.inrussian.components.onboarding.interactiveOnboarding

interface InteractiveOnboardingComponent {
    fun onFinish()
}

sealed class InteractiveOnboardingOutput {
    object Finished : InteractiveOnboardingOutput()
}

class DefaultInteractiveOnboardingComponent(
    private val onOutput: (InteractiveOnboardingOutput) -> Unit
) : InteractiveOnboardingComponent {
    override fun onFinish() {
        onOutput(InteractiveOnboardingOutput.Finished)
    }
}
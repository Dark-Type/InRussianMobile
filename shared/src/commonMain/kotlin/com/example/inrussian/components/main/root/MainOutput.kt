package com.example.inrussian.components.main.root

sealed interface MainOutput {
    data object NavigateToOnboarding : MainOutput
    data object NavigateToAuth : MainOutput
}

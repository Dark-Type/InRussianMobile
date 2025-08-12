package com.example.inrussian.components.main.root

sealed interface MainOutput {
    data object NavigateBack : MainOutput
}

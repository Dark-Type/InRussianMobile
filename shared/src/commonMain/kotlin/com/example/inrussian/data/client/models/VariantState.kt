package com.example.inrussian.data.client.models

sealed interface VariantState {
        data object Selected : VariantState
        data object NotSelected : VariantState
        data object Correct : VariantState
        data object Incorrect : VariantState
    }
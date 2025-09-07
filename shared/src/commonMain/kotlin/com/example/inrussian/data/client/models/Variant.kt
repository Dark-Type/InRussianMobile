package com.example.inrussian.data.client.models

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class Variant @OptIn(ExperimentalUuidApi::class) constructor(
        val id: Uuid = Uuid.random(),
        val isCorrect: Boolean = false,
        val text: String = "",
        val state: VariantState = VariantState.NotSelected
    )


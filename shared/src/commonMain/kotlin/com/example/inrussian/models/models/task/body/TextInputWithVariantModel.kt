package com.example.inrussian.models.models.task.body

import kotlinx.serialization.Serializable

@Serializable
data class TextInputWithVariantModel(
    val label: String,
    val text: String,
    val gaps: List<GapWithVariantModel>
)
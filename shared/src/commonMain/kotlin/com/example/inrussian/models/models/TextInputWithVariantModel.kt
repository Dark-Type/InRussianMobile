package com.example.inrussian.models.models

import kotlinx.serialization.Serializable

@Serializable
data class TextInputWithVariantModel(
    val text: String,
    val gaps: List<GapWithVariantModel>
)
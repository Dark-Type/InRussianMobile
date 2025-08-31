package com.example.inrussian.models.models

import kotlinx.serialization.Serializable

@Serializable
data class GapWithVariantModel(
    val position: Int,
    val variants: List<String>,
    val correctVariant: String,
)
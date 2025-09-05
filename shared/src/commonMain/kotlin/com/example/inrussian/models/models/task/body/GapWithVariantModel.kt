package com.example.inrussian.models.models.task.body

import kotlinx.serialization.Serializable

@Serializable
data class GapWithVariantModel(
    val position: Int,
    val variants: List<String>,
    val correctVariant: String,
)
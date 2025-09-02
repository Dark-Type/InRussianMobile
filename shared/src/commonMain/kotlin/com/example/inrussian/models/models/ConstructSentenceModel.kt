package com.example.inrussian.models.models

import kotlinx.serialization.Serializable

@Serializable
data class ConstructSentenceModel(
    val audio: String?,
    val variants: List<String>
)

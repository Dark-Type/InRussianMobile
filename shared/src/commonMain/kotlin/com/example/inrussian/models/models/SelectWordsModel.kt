package com.example.inrussian.models.models

import kotlinx.serialization.Serializable

@Serializable
data class SelectWordsModel(
    val audio: String,
    val variants: List<Pair<String, Boolean>>
)

package com.example.inrussian.models.models

import kotlinx.serialization.Serializable

@Serializable
data class ListenAndSelectModel(
    val text: String,
    val variants: List<Pair<String, Boolean>>
)
package com.example.inrussian.models.models

import kotlinx.serialization.Serializable

@Serializable
data class AudioBlocks(
    val name: String,
    val description: String?,
    val audio: String,
    val descriptionTranslate: String?,
)
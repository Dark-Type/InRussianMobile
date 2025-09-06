package com.example.inrussian.models.models.task.support

import kotlinx.serialization.Serializable

@Serializable
data class AudioBlocks(
    val name: String,
    val description: String? = null,
    val audio: String,
    val descriptionTranslate: String? = null,
)
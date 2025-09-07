package com.example.inrussian.models.models.task.body

import kotlinx.serialization.Serializable

@Serializable
data class ImageBlock(
    val name: String,
    val description: String? = null,
    val image: String,
    val descriptionTranslate: String? = null,
)
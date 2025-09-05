package com.example.inrussian.models.models.task.body

import kotlinx.serialization.Serializable

@Serializable
data class ImageBlocks(
    val name: String,
    val description: String?,
    val image: String,
    val descriptionTranslate: String?,
)
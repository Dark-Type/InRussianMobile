package com.example.inrussian.models.models

import kotlinx.serialization.Serializable

@Serializable
data class ImageBlocks(
    val name: String,
    val description: String?,
    val image: String,
    val descriptionTranslate: String?,
)
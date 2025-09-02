package com.example.inrussian.models.models

import kotlinx.serialization.Serializable

@Serializable
data class ImageAndSelectModel(
    val imageBlocks: List<ImageBlocks>,
    val variants: List<Pair<String, Boolean>>
)
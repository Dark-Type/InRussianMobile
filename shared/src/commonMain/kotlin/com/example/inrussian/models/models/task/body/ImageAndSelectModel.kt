package com.example.inrussian.models.models.task.body

import kotlinx.serialization.Serializable

@Serializable
data class ImageAndSelectModel(
    val imageBlocks: List<ImageBlock>,
    val variants: List<Pair<String, Boolean>>
)
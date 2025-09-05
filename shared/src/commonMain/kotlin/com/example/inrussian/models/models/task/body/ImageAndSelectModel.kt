package com.example.inrussian.models.models.task.body

import com.example.inrussian.models.models.task.body.ImageBlocks
import kotlinx.serialization.Serializable

@Serializable
data class ImageAndSelectModel(
    val imageBlocks: List<ImageBlocks>,
    val variants: List<Pair<String, Boolean>>
)
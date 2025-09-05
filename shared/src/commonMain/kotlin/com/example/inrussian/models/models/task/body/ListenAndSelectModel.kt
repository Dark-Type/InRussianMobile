package com.example.inrussian.models.models.task.body

import com.example.inrussian.models.models.task.support.AudioBlocks
import kotlinx.serialization.Serializable

@Serializable
data class ListenAndSelectModel(
    val audioBlocks: List<AudioBlocks>,
    val variants: List<Pair<String, Boolean>>
)
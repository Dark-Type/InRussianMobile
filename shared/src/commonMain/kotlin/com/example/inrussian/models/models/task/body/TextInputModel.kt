package com.example.inrussian.models.models.task.body

import com.example.inrussian.models.models.task.support.Gap
import kotlinx.serialization.Serializable

@Serializable
data class TextInputModel(
    val label: String,
    val text: String,
    val gaps: List<Gap>
)

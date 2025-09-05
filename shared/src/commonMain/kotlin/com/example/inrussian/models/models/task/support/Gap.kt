package com.example.inrussian.models.models.task.support

import kotlinx.serialization.Serializable

@Serializable
data class Gap(
    val correctWord: String,
    val index: Int
)
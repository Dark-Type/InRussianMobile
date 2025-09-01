package com.example.inrussian.models.models

import kotlinx.serialization.Serializable

@Serializable
data class TaskCountRequest(
    val count: Long
)

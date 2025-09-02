package com.example.inrussian.models.models

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlin.uuid.Uuid

@Serializable
data class AttemptRequest(
    @Contextual val attemptId: String,
    @Contextual val taskId: String,
    val attemptsCount: Int,
    val timeSpentMs: Long
)
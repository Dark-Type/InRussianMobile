package com.example.inrussian.models.models

import com.example.inrussian.components.main.train.TaskType
import kotlinx.serialization.Serializable
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Serializable
data class TaskModel @OptIn(ExperimentalTime::class, ExperimentalUuidApi::class) constructor(
    val id: String = Uuid.random().toString(),
    val taskType: List<TaskType>,
    val taskBody: TaskBody,
    val question: String?,
    val createdAt: String = Clock.System.now().toString(),
    val updatedAt: String = Clock.System.now().toString()
)
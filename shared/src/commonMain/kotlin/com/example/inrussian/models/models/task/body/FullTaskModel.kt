package com.example.inrussian.models.models.task.body

import com.example.inrussian.repository.main.train.TaskBody
import com.example.inrussian.repository.main.train.TaskType

data class FullTaskModel(
    val id: String,
    val themeId: String,
    val question: String?,
    val body: TaskBody,
    val types: List<TaskType>,
    val createdAt: String,
    val updatedAt: String
)
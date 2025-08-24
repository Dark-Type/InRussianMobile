package com.example.inrussian.models.models

import com.example.inrussian.components.main.train.TaskType

data class FullTaskMode(
    val id: String,
    val taskType: List<TaskType>,
    val taskBody: TaskBody
)


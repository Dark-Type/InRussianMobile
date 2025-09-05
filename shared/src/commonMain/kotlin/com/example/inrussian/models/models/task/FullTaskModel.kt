package com.example.inrussian.models.models.task

import com.example.inrussian.components.main.train.TaskType

data class FullTaskModel(
    val id: String,
    val taskText: String,
    val taskType: List<TaskType>,
    val taskBody: TaskBody
)
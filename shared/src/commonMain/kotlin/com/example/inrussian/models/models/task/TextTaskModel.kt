package com.example.inrussian.models.models.task

import com.example.inrussian.models.models.TaskState

data class TextTaskModel(
    override val id: String,
    val text: String = "",
    val isAnswer: Boolean = false,
    override var state: TaskState = TaskState.NotSelected,
) : Task(id, state)
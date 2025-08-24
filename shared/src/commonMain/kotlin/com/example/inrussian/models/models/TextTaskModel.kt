package com.example.inrussian.models.models

data class TextTaskModel(
    override val id: String,
    val text: String,
    override val state: TaskState = TaskState.NotSelected,
) : Task(id,state)

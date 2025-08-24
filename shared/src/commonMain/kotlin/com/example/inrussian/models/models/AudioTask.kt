package com.example.inrussian.models.models

data class AudioTask(
    override val id: String,
    val isPlay: Boolean = false,
    override val state: TaskState = TaskState.NotSelected,
) : Task(id,state)

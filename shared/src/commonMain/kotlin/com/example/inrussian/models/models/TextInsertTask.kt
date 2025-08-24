package com.example.inrussian.models.models

data class TextInsertTask(

    val label: String,
    val sentence: List<Sentence>,
    override val id: String = "",
    override val state: TaskState = TaskState.NotSelected,
) : Task(id, state)

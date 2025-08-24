package com.example.inrussian.models.models.task

import com.example.inrussian.models.models.Sentence
import com.example.inrussian.models.models.TaskState

data class TextInsertTask(
    val label: String,
    val sentence: List<Sentence>,
    override val id: String = "",
    override var state: TaskState = TaskState.NotSelected,
) : Task(id, state)
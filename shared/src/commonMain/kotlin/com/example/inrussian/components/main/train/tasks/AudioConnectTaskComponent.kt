package com.example.inrussian.components.main.train.tasks

import com.arkivanov.decompose.value.Value
import com.example.inrussian.models.models.task.Task

interface AudioConnectTaskComponent {
    val state: Value<State>

    data class State(
        val elements: List<Task> = listOf(),
        val isChecked: Boolean = false,
        val electedTask: Task? = null,
        val hasError: Boolean? = null,
        val pairs: MutableList<Pair<Task, Task>> = mutableListOf()
    )

    fun onTaskClick(taskId: String)

    fun onContinueClick()
}
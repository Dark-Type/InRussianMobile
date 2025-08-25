package com.example.inrussian.models.models.task

import com.example.inrussian.models.models.task.Task
import com.example.inrussian.models.models.TaskState

data class AudioTask(
    override val id: String,
    val isPlay: Boolean = false,
    val url: String = "",
    override var state: TaskState = TaskState.NotSelected,
) : Task(id,state) {
    override fun copyWithState(newState: TaskState): Task {
        return this.copy(state = newState)
    }
}
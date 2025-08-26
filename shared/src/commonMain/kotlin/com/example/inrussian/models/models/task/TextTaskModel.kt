package com.example.inrussian.models.models.task

import com.example.inrussian.models.models.TaskState

data class TextTaskModel(
    override val id: String,
    val text: String = "",
    val isAnswer: Boolean = false,
    override var state: TaskState = TaskState.NotSelected,
) : Task(id, state){
    override fun copyWithState(newState: TaskState): Task {
        return this.copy(state = newState)
    }
}
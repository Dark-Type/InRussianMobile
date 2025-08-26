package com.example.inrussian.models.models.task

import com.example.inrussian.models.models.TaskState
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class Task(
    @SerialName("task_id")
    open val id: String,
    @SerialName("state_id")
    open var state: TaskState,
) {
    abstract fun copyWithState(newState: TaskState): Task
}
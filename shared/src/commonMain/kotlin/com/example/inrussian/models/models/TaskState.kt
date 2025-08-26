package com.example.inrussian.models.models

import kotlinx.serialization.Serializable

@Serializable
sealed interface TaskState {
    @Serializable
    data object Selected : TaskState
    @Serializable
    data object Correct : TaskState
    @Serializable
    data object Incorrect : TaskState
    @Serializable
    data object NotSelected : TaskState

    @Serializable
    data object Connect : TaskState
}

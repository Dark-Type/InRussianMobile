package com.example.inrussian.models.models

sealed interface TaskState {
    data object Selected : TaskState
    data object Correct : TaskState
    data object Incorrect : TaskState
    data object NotSelected : TaskState
}

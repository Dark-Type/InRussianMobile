package com.example.inrussian.models.models.task

import com.example.inrussian.models.models.TaskState
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ImageConnectTaskModel(
    @SerialName("ImageConnectTaskModel_id")
    override val id: String,
    val imageUrl: String,
    @SerialName("ImageConnectTaskModel_state")
    override var state: TaskState = TaskState.NotSelected
) : Task(id, state)
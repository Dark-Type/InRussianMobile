package com.example.inrussian.components.main.train

import com.example.inrussian.models.models.AudioConnectTaskModel
import com.example.inrussian.models.models.TaskBody.AudioConnectTask
import com.example.inrussian.models.models.task.ImageConnectTaskModel
import com.example.inrussian.models.models.TaskBody.ImageConnectTask
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
fun ImageConnectTask.toImageConnectTask() = this.variant.map {
    ImageConnectTaskModel(
        id = Uuid.random().toString(),
        imageUrl = it.first,
    )
}

@OptIn(ExperimentalUuidApi::class)
fun AudioConnectTask.toAudioConnectionTask() = this.variant.map {
  AudioConnectTaskModel(
        id = Uuid.random().toString(),
        url = it.first,
        text = it.second,
    )
}

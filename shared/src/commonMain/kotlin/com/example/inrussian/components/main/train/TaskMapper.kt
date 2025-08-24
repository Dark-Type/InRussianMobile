package com.example.inrussian.components.main.train

import com.example.inrussian.models.models.AudioConnectTaskModel
import com.example.inrussian.models.models.TaskBody.AudioTask
import com.example.inrussian.models.models.task.ImageConnectTaskModel
import com.example.inrussian.models.models.TaskBody.ImageTask
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
fun ImageTask.toImageConnectTask() = this.variant.map {
    ImageConnectTaskModel(
        id = Uuid.random().toString(),
        imageUrl = it.first,
    )
}

@OptIn(ExperimentalUuidApi::class)
fun AudioTask.toAudioConnectionTask() = this.variant.map {
  AudioConnectTaskModel(
        id = Uuid.random().toString(),
        url = it.first,
        text = it.second,
    )
}

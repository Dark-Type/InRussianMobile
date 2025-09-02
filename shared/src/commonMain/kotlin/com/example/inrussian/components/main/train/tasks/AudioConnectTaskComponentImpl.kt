package com.example.inrussian.components.main.train.tasks

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.example.inrussian.models.models.TaskBody
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
class AudioConnectTaskComponentImpl(
    context: ComponentContext,
    private val onContinueClicked: (Boolean) -> Unit,
    private val inChecking: (Boolean) -> Unit,
    private val onButtonEnable: (Boolean) -> Unit,
    listAudioTasks: TaskBody.AudioTask,
) : AudioConnectTaskComponent, ComponentContext by context {
    override val state: Value<AudioConnectTaskComponent.State> get() = TODO()
    override fun onTaskClick(taskId: String) {
        TODO("Not yet implemented")
    }

    override fun onContinueClick() {
        TODO("Not yet implemented")
    }


}

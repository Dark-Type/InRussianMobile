package com.example.inrussian.stores.main.train

import com.arkivanov.mvikotlin.core.store.Store
import com.example.inrussian.models.models.task.TaskResponse
import com.example.inrussian.repository.main.train.FullTaskModel
import com.example.inrussian.stores.main.train.TrainStore.Intent
import com.example.inrussian.stores.main.train.TrainStore.Label
import com.example.inrussian.stores.main.train.TrainStore.State

interface TrainStore :
    Store<Intent, State, Label> {

    sealed interface Intent {
        data class ButtonClick(val isPass: Boolean) : Intent
        data class OnButtonStateChange(val isEnable: Boolean) : Intent
        data class InCheckStateChange(val inCheck: Boolean) : Intent
    }

    data class State(
        val showedTask: FullTaskModel? = null,
        val isCorrect: Boolean? = null,
        val isButtonEnable: Boolean = false,
        val isLoading: Boolean = true,
        val percent: Float? = null,
    )

    sealed interface Label {
        data class ChangeTask(val task: FullTaskModel?) : Label
    }

    sealed interface Msg {
        data class UpdateTasks(val tasks: TaskResponse) : Msg
        data class UpdateButtonState(val isEnable: Boolean) : Msg
        data class UpdateCheckState(val inCheck: Boolean?) : Msg
    }
}


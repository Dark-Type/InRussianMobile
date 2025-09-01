package com.example.inrussian.stores.main.train

import com.arkivanov.mvikotlin.core.store.Store
import com.example.inrussian.models.models.FullTaskModel
import com.example.inrussian.stores.main.train.TrainStore.Intent
import com.example.inrussian.stores.main.train.TrainStore.Label
import com.example.inrussian.stores.main.train.TrainStore.State
import com.example.inrussian.utils.SuspendingQueue
import com.example.inrussian.utils.queueOf

interface TrainStore : Store<Intent, State, Label> {
    sealed interface Intent {
        data class ContinueClick(val isPass: Boolean) : Intent
        data class OnButtonStateChange(val isEnable: Boolean) : Intent
        data class InCheckStateChange(val inCheck: Boolean) : Intent
    }

    data class State(
        val tasks: List<FullTaskModel>? = null,
        val rejectedTask: SuspendingQueue<FullTaskModel> = queueOf(),
        val currentTaskIndex: Int = 0,
        val isStartRepeat: Boolean = false,
        val showedTask: FullTaskModel? = null,
        val errorCounter: Int = 0,
        val isChecking: Boolean = true,
        val isButtonEnable: Boolean = false,
        val isLoading: Boolean = true
    ) {
        val percent: Float? = tasks?.let {  currentTaskIndex.toFloat() / it.size.toFloat() }
    }

    sealed interface Action {
        data class LoadTasks(val themeId: String) : Action
    }

    sealed interface Msg {
        data class UpdateTasks(val tasks: List<FullTaskModel>) : Msg
        data object UpdateTaskFromQueue : Msg
        data class UpdateTask(val task: FullTaskModel?) : Msg
        data class AddTaskInQueue(val task: FullTaskModel) : Msg
        data object UpdateCounter : Msg
        data object UpdateIndexAndTask : Msg
        data class UpdateButtonState(val isEnable: Boolean) : Msg
        data class UpdateCheckState(val inCheck: Boolean) : Msg
        data class StartRepeat(val isRepeat: Boolean) : Msg
        data class StayNewCounter(val counter: Int) : Msg
    }

    sealed interface Label {
        data class ChangeTask(val task: FullTaskModel?) : Label
    }

}
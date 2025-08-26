package com.example.inrussian.stores.main.train

import co.touchlab.kermit.Logger
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.example.inrussian.repository.main.train.TrainRepository
import com.example.inrussian.stores.main.train.TrainStore.Action
import com.example.inrussian.stores.main.train.TrainStore.Intent
import com.example.inrussian.stores.main.train.TrainStore.Label
import com.example.inrussian.stores.main.train.TrainStore.Msg
import com.example.inrussian.stores.main.train.TrainStore.Msg.AddTaskInQueue
import com.example.inrussian.stores.main.train.TrainStore.Msg.UpdateButtonState
import com.example.inrussian.stores.main.train.TrainStore.Msg.UpdateCounter
import com.example.inrussian.stores.main.train.TrainStore.Msg.UpdateIndexAndTask
import com.example.inrussian.stores.main.train.TrainStore.State
import com.example.inrussian.utils.ErrorDecoder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TrainStoreFactory(
    private val storeFactory: StoreFactory,
    private val errorDecoder: ErrorDecoder,
    private val repository: TrainRepository
) {
    fun create(courseId: String): TrainStore =
        object : TrainStore, Store<Intent, State, Label> by storeFactory.create(
            name = "TrainStore",
            initialState = State(),
            bootstrapper = SimpleBootstrapper(Action.LoadTasks(courseId)),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl,
        ) {}

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {

        override fun executeAction(action: Action) {
            when (action) {
                is Action.LoadTasks -> {
                    scope.launch(Dispatchers.Main) {
                        dispatch(Msg.UpdateTasks(repository.getTasksByCourseId(action.courseId)))
                        Logger.i() { "Update" }
                    }
                }
            }
        }

        override fun executeIntent(intent: Intent) {
            when (intent) {
                is Intent.ContinueClick -> {
                    val state = state()
                    if (!intent.isPass) {
                        dispatch(UpdateCounter)
                        state.showedTask?.let { dispatch(AddTaskInQueue(it)) }
                    }
                    dispatch(UpdateIndexAndTask)
                }

                is Intent.OnButtonStateChange -> dispatch(UpdateButtonState(intent.isEnable))
                is Intent.InCheckStateChange -> dispatch(Msg.UpdateCheckState(intent.inCheck))
            }
        }
    }


    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State = when (msg) {
            is AddTaskInQueue -> copy(rejectedTask = rejectedTask.apply {
                offerSync(msg.task)
            })

            UpdateCounter -> copy(errorCounter = errorCounter + 1)
            UpdateIndexAndTask -> copy(
                currentTaskIndex = currentTaskIndex + 1,
                showedTask = tasks?.get(currentTaskIndex + 1)
            )

            is Msg.UpdateTasks -> {
                Logger.i() { "copy" }
                copy(tasks = msg.tasks, showedTask = msg.tasks.firstOrNull())
            }

            is UpdateButtonState -> copy(isButtonEnable = msg.isEnable)
            is Msg.UpdateCheckState -> copy(isChecking = msg.inCheck)
        }
    }


    companion object {
        private const val TAG = "AuthStoreFactory"
    }
}

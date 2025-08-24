package com.example.inrussian.stores.main.train

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
import com.example.inrussian.stores.main.train.TrainStore.State
import com.example.inrussian.utils.ErrorDecoder
import kotlinx.coroutines.launch

class TrainStoreFactory(
    private val storeFactory: StoreFactory,
    private val courseId: String,
    private val errorDecoder: ErrorDecoder,
    private val repository: TrainRepository
) {
    fun create(): TrainStore =
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
                    scope.launch {
                        Msg.UpdateTasks(repository.getTasksByCourseId(action.courseId))
                    }
                }
            }
        }

        override fun executeIntent(intent: Intent) {
            when (intent) {
                is Intent.ContinueClick -> {
                    val state = state()
                    if (!intent.isPass) {
                        dispatch(Msg.UpdateCounter)
                        state.showedTask?.let { dispatch(Msg.AddTaskInQueue(it)) }
                    }
                    dispatch(Msg.UpdateIndexAndTask)
                }
            }
        }
    }


    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State = when (msg) {
            is Msg.AddTaskInQueue -> copy(rejectedTask = rejectedTask.apply {
                offerSync(msg.task)
            })
            Msg.UpdateCounter -> copy(errorCounter = errorCounter + 1)
            Msg.UpdateIndexAndTask -> copy(
                currentTaskIndex = currentTaskIndex + 1,
                showedTask = tasks?.get(currentTaskIndex + 1)
            )
            is Msg.UpdateTasks -> copy(tasks = msg.tasks)
        }
    }


    companion object {
        private const val TAG = "AuthStoreFactory"
    }
}

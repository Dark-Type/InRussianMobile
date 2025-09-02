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
import com.example.inrussian.stores.main.train.TrainStore.Msg.UpdateButtonState
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
                        dispatch(Msg.UpdateTasks(repository.getNextTask()))
                    }
                }
            }
        }

        override fun executeIntent(intent: Intent) {
            scope.launch {
                when (intent) {
                    is Intent.ContinueClick -> {
                        repository.sendResultAndGetNextTask()
                        dispatch(Msg.UpdateTasks(repository.getNextTask()))
                    }

                    is Intent.OnButtonStateChange -> dispatch(UpdateButtonState(intent.isEnable))
                    is Intent.InCheckStateChange -> dispatch(Msg.UpdateCheckState(intent.inCheck))
                }
            }

        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State = when (msg) {


            is Msg.UpdateTasks -> {
                copy(
                    showedTask = msg.tasks.task,
                    percent = msg.tasks.percent
                )
            }

            is UpdateButtonState -> copy(isButtonEnable = msg.isEnable)
            is Msg.UpdateCheckState -> copy(isChecking = msg.inCheck)

        }
    }


    companion object {
        private const val TAG = "AuthStoreFactory"
    }
}
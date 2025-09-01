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
                        dispatch(Msg.UpdateTasks(repository.getTasksByThemeId(action.themeId)))
                    }
                }
            }
        }

        override fun executeIntent(intent: Intent) {
            scope.launch {
                when (intent) {
                    is Intent.ContinueClick -> {
                        val st = state()

                        val willAddToRejected = !intent.isPass
                        if (willAddToRejected) {
                            dispatch(UpdateCounter)
                            st.showedTask?.let { dispatch(AddTaskInQueue(it)) }
                        }

                        val shouldMoveToNext = if (st.isStartRepeat) {
                            true
                        } else {
                            (st.currentTaskIndex + 1) < (st.tasks?.size ?: 0)
                        }

                        if (shouldMoveToNext) {
                            if (st.isStartRepeat) {
                                if (st.rejectedTask.size()!=0) {
                                    dispatch(Msg.UpdateTaskFromQueue)
                                } else {
                                    dispatch(Msg.StartRepeat(false))
                                    dispatch(Msg.UpdateTask(null))
                                }
                            } else {
                                dispatch(UpdateIndexAndTask)
                            }
                        } else {
                            if (st.rejectedTask.size()!=0) {
                                dispatch(Msg.StartRepeat(true))
                                dispatch(Msg.UpdateTaskFromQueue)
                            } else {
                                dispatch(Msg.UpdateTask(null))
                            }
                        }
                    }                    /*is Intent.ContinueClick -> {
                        val st = state()

                        val willAddToRejected = !intent.isPass
                        if (willAddToRejected) {
                            dispatch(UpdateCounter)
                            st.showedTask?.let { dispatch(AddTaskInQueue(it)) }
                        }

                        val oldRejectedSize = st.rejectedTask.size()
                        val newRejectedSize = oldRejectedSize + if (willAddToRejected) 1 else 0

                        val tasksSize = st.tasks?.size ?: 0
                        val isLastMain = (st.currentTaskIndex + 1) >= tasksSize

                        if (!st.isStartRepeat) {
                            if (!isLastMain) {
                                dispatch(UpdateIndexAndTask)
                            } else {
                                if (newRejectedSize > 0) {
                                    dispatch(Msg.StartRepeat(true))
                                    dispatch(Msg.UpdateTaskFromQueue)
                                } else {
                                    dispatch(Msg.UpdateTask(null))
                                }
                            }
                        } else {
                            if (st.rejectedTask.size() != 0) {
                                dispatch(Msg.UpdateTaskFromQueue)
                            } else {
                                dispatch(Msg.StartRepeat(false))
                                dispatch(Msg.UpdateTask(null))
                            }
                        }
                    }
*/
                    is Intent.OnButtonStateChange -> dispatch(UpdateButtonState(intent.isEnable))
                    is Intent.InCheckStateChange -> dispatch(Msg.UpdateCheckState(intent.inCheck))
                }
            }

        }
    }


    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State = when (msg) {
            is AddTaskInQueue -> copy(rejectedTask = rejectedTask.apply {
                offerSync(msg.task)
            })

            UpdateCounter -> copy(errorCounter = errorCounter + 1)

            UpdateIndexAndTask -> {
                val newIndex = currentTaskIndex + 1
                copy(
                    currentTaskIndex = newIndex,
                    showedTask = tasks?.getOrNull(newIndex)
                )
            }

            is Msg.UpdateTasks -> {
                copy(tasks = msg.tasks, showedTask = msg.tasks.firstOrNull())
            }

            is UpdateButtonState -> copy(isButtonEnable = msg.isEnable)
            is Msg.UpdateCheckState -> copy(isChecking = msg.inCheck)
            is Msg.StartRepeat -> copy(isStartRepeat = msg.isRepeat)
            is Msg.StayNewCounter -> copy(currentTaskIndex = msg.counter)

            Msg.UpdateTaskFromQueue -> {
                val nextTask = rejectedTask.pollS()
                copy(showedTask = nextTask)
            }

            is Msg.UpdateTask -> copy(showedTask = msg.task)
        }
        /*override fun State.reduce(msg: Msg): State = when (msg) {

            is AddTaskInQueue -> copy(rejectedTask = rejectedTask.apply {
                offerSync(msg.task)
            })

            UpdateCounter -> copy(errorCounter = errorCounter + 1)
            UpdateIndexAndTask -> copy(
                currentTaskIndex = currentTaskIndex + 1,
                showedTask = tasks?.get(currentTaskIndex + 1)
            )

            is Msg.UpdateTasks -> {
                copy(tasks = msg.tasks, showedTask = msg.tasks[currentTaskIndex])
            }

            is UpdateButtonState -> copy(isButtonEnable = msg.isEnable)
            is Msg.UpdateCheckState -> copy(isChecking = msg.inCheck)
            is Msg.StartRepeat -> copy(isStartRepeat = isStartRepeat)
            is Msg.StayNewCounter -> copy(currentTaskIndex = msg.counter)
            Msg.UpdateTaskFromQueue -> copy(showedTask = rejectedTask.pollS())
            is Msg.UpdateTask -> copy(showedTask = msg.task)
        }*/
    }


    companion object {
        private const val TAG = "AuthStoreFactory"
    }
}
/*scope.launch {
               when (intent) {
                   is Intent.ContinueClick -> {
                       val state = state()
                       if (!intent.isPass) {
                           dispatch(UpdateCounter)
                           state.showedTask?.let { dispatch(AddTaskInQueue(it)) }
                       }
                       if (state.currentTaskIndex + 1 == state.tasks?.size && state.rejectedTask.size() != 0 || state.isStartRepeat) {
                           dispatch(Msg.StartRepeat(true))
                           if (state.rejectedTask.size() != 0)
                               dispatch(Msg.UpdateTaskFromQueue)
                           else
                               dispatch(Msg.StartRepeat(false))
                       }
                       if (state.rejectedTask.size() == 0 && state.isStartRepeat) {
                           dispatch(UpdateTask(null))
                       } else
                           dispatch(UpdateIndexAndTask)
                   }

                   is Intent.OnButtonStateChange -> dispatch(UpdateButtonState(intent.isEnable))
                   is Intent.InCheckStateChange -> dispatch(Msg.UpdateCheckState(intent.inCheck))
               }
           }*/

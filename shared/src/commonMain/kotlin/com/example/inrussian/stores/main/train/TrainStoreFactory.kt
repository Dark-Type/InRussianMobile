package com.example.inrussian.stores.main.train

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.example.inrussian.models.models.task.TaskModel
import com.example.inrussian.repository.main.train.AttemptRequest
import com.example.inrussian.repository.main.train.FullTaskModel
import com.example.inrussian.repository.main.train.TaskResponse
import com.example.inrussian.repository.main.train.TrainRepository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class TrainStoreFactory(
    private val storeFactory: StoreFactory,
    private val repository: TrainRepository
) {

    fun create(themeId: String): TrainStore =
        object : TrainStore,
            Store<TrainStore.Intent, TrainStore.State, TrainStore.Label> by storeFactory.create(
                name = "TrainStore($themeId)",
                initialState = TrainStore.State(),
                bootstrapper = SimpleBootstrapper(Action.LoadTasks(themeId)),
                executorFactory = { ExecutorImpl(themeId) },
                reducer = ReducerImpl
            ) {}

    /* ---------------- Internal Action / Msg ---------------- */

    private sealed interface Action {
        data class LoadTasks(val themeId: String) : Action
    }

    private sealed interface Msg : TrainStore.Msg {
        data class UpdateTasks(val tasks: TaskResponse) : Msg, TrainStore.Msg
        data class UpdateButtonState(val isEnable: Boolean) : Msg, TrainStore.Msg
        data class UpdateCheckState(val inCheck: Boolean?) : Msg, TrainStore.Msg
    }

    /* ---------------- Executor ---------------- */

    private inner class ExecutorImpl(
        private val themeId: String
    ) : CoroutineExecutor<TrainStore.Intent, Action, TrainStore.State, Msg, TrainStore.Label>() {

        // Local progress cache
        private var solved = 0
        private var total = 0
        private var currentTaskId: String? = null
        private var attemptCounterForCurrent = 0

        override fun executeAction(action: Action) {
            when (action) {
                is Action.LoadTasks -> loadNextTask()
            }
        }

        @OptIn(ExperimentalUuidApi::class)
        override fun executeIntent(intent: TrainStore.Intent) {
            when (intent) {
                is TrainStore.Intent.ButtonClick -> {
                    scope.launch {
                        val current = state().showedTask ?: return@launch
                        // Interpret ButtonClick:
                        // If user clicked "pass" meaning they want to submit / move on
                        // We treat 'isPass' as user decided correctness (true = correct)
                        val isCorrect = intent.isPass
                        attemptCounterForCurrent += 1

                        // Submit attempt to repository
                        val attemptReq = AttemptRequest(
                            attemptId = Uuid.random(),
                            taskId = Uuid.parse(current.id),
                            attemptsCount = attemptCounterForCurrent,
                            timeSpentMs = 1200L // mock for now
                        )
                        val result = repository.submitAttempt(attemptReq)

                        solved = result.solved
                        total = result.total

                        if (isCorrect) {
                            // Reset for next task
                            attemptCounterForCurrent = 0
                            loadNextTask()
                            dispatch(Msg.UpdateCheckState(null))
                            dispatch(Msg.UpdateButtonState(false))
                        } else {
                            // Mark incorrect state
                            dispatch(Msg.UpdateCheckState(false))
                            dispatch(Msg.UpdateButtonState(true))
                        }
                    }
                }

                is TrainStore.Intent.InCheckStateChange ->
                    dispatch(Msg.UpdateCheckState(intent.inCheck))

                is TrainStore.Intent.OnButtonStateChange ->
                    dispatch(Msg.UpdateButtonState(intent.isEnable))
            }
        }

        private fun loadNextTask() {
            scope.launch(Dispatchers.Main) {
                dispatch(Msg.UpdateButtonState(false))
                val taskModel = repository.nextTask(themeId)
                if (total == 0 && taskModel != null) {
                    // If total unknown yet fetch theme contents to compute total
                    val contents = repository.themeContents(themeId)
                    total = contents.tasks.size
                    solved = contents.tasks.count { it.id != taskModel.id } - // naive guess
                            contents.tasks.size // simplified; will sync after first attempt
                    if (solved < 0) solved = 0
                }
                currentTaskId = taskModel?.id
                val percent = if (total == 0) null else (solved.toFloat() / total.toFloat())
                val wrapped = TaskResponse(
                    percent = percent?.coerceIn(0f, 1f) ?: 0f,
                    task = taskModel?.toFullTaskModel(themeId)
                )
                dispatch(Msg.UpdateTasks(wrapped))
                publish(TrainStore.Label.ChangeTask(wrapped.task))
            }
        }
    }

    /* ---------------- Reducer ---------------- */

    private object ReducerImpl : Reducer<TrainStore.State, Msg> {
        override fun TrainStore.State.reduce(msg: Msg): TrainStore.State =
            when (msg) {
                is Msg.UpdateTasks -> copy(
                    showedTask = msg.tasks.task,
                    percent = msg.tasks.percent,
                    isLoading = false
                )

                is Msg.UpdateButtonState -> copy(isButtonEnable = msg.isEnable)
                is Msg.UpdateCheckState -> copy(isCorrect = msg.inCheck)
            }
    }

    private fun TaskModel.toFullTaskModel(themeId: String) = FullTaskModel(
        id = id,
        themeId = themeId,
        question = question,
        body = taskBody,
        types = taskType,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

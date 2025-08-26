package com.example.inrussian.components.main.train.tasks

import co.touchlab.kermit.Logger
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.example.inrussian.components.main.train.tasks.ImageConnectTaskComponent.State
import com.example.inrussian.models.models.TaskBody
import com.example.inrussian.models.models.TaskState
import com.example.inrussian.models.models.task.ImageConnectTaskModel
import com.example.inrussian.models.models.task.Task
import com.example.inrussian.models.models.task.TextTaskModel
import com.example.inrussian.utils.componentCoroutineScope
import kotlinx.coroutines.launch
import kotlin.random.Random
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class ImageConnectTaskComponentImpl(
    context: ComponentContext,
    private val onContinueClicked: (Boolean) -> Unit,
    private val inChecking: (Boolean) -> Unit,
    private val onButtonEnable: (Boolean) -> Unit,
    listImageTasks: TaskBody.ImageTask
) : ImageConnectTaskComponent, ComponentContext by context {
    var correctList = mutableListOf<Pair<Task, Task>>()
    override val state = MutableValue(State())
    val scope = componentCoroutineScope()

    init {
        val tasks = listImageTasks.variant.map { (qText, aText) ->
            val q = TextTaskModel(id = Uuid.random().toString(), text = qText, isAnswer = false)
            val a = ImageConnectTaskModel(id = Uuid.random().toString(), imageUrl = aText)
            q to a
        }

        val shuffledAnswers = tasks.map { it.second }.shuffled()
        val elements = tasks.mapIndexed { index, (q, _) -> q to shuffledAnswers[index] }

        this.correctList = tasks.toMutableList()
        state.value = state.value.copy(elements = elements)
        scope.launch {
            state.subscribe {
                Logger.i { (it.pairs.size == it.elements.size).toString() + "<- is enable" }
                onButtonEnable(it.pairs.size == it.elements.size)
            }
        }
    }

    override fun onTaskClick(taskId: String) {
        if (!state.value.isChecked) {
            if (state.value.selectedTask == null) {
                val pairConnectedTask =
                    state.value.pairs.find { it.first.id == taskId || it.second.id == taskId }
                if (state.value.selectedTask?.id == taskId)
                    state.value = state.value.copy(
                        selectedTask = state.value.selectedTask?.copyWithState(TaskState.NotSelected)
                    )
                else if (pairConnectedTask == null)
                    state.value = state.value.copy(selectedTask = findById(taskId).apply {
                        state = TaskState.Selected
                    })
                else {
                    findById(pairConnectedTask.first.id).state = TaskState.NotSelected
                    findById(pairConnectedTask.second.id).state = TaskState.NotSelected
                    state.value.pairs.remove(pairConnectedTask)
                }
            } else {
                val task = findById(taskId)
                val pairConnectedTask =
                    state.value.pairs.find { it.first.id == taskId || it.second.id == taskId }
                if (pairConnectedTask == null)

                    if (state.value.selectedTask!!::class == task::class) {
                        state.value.updateTaskInPairs(taskId, newState = TaskState.Selected)
                        state.value.selectedTask?.state = TaskState.NotSelected
                    } else {
                        if (task.state != TaskState.Selected) {
                            state.value.pairs.add(if (task is TextTaskModel) task to state.value.selectedTask!! else state.value.selectedTask!! to task)
                            state.value.selectedTask?.state = TaskState.Connect
                            state.value = state.value.copy(selectedTask = null)
                            task.state = TaskState.Connect
                        } else if (task == state.value.selectedTask) {
                            state.value.selectedTask?.state = TaskState.NotSelected
                            state.value = state.value.copy(selectedTask = null)
                        }
                    }
            }

        }
        Logger.d { state.value.toString() }

    }

    fun findById(taskId: String): Task {
        val taskPair = state.value.elements.find { it.first.id == taskId || it.second.id == taskId }
        return if (taskPair?.second?.id == taskId) taskPair.second else taskPair!!.first
    }

    fun State.updateTaskInPairs(
        taskId: String,
        newState: TaskState
    ): State {
        val updatedPairs = pairs.map { pair ->
            when {
                pair.first.id == taskId -> pair.first.copyWithState(newState) to pair.second
                pair.second.id == taskId -> pair.first to pair.second.copyWithState(newState)
                else -> pair
            }
        }
        return this.copy(pairs = updatedPairs.toMutableList())
    }


    override fun onContinueClick() {
        if (state.value.isChecked) {
            inChecking(true)
            state.value.hasError?.let { onContinueClicked(it) }
        } else {
            var hasError = false
            val answerList = state.value.copy().elements
            Logger.i { "current-> $correctList" }
            Logger.i { "elements-> ${state.value.elements}" }
            Logger.i { "pairs-> ${state.value.pairs}" }
            state.value.pairs.forEachIndexed { i, e ->
                if (correctList.find { it.first.id == e.first.id }?.second?.id != e.second.id) {
                    Logger.i { "find-> ${state.value.elements.find { it.first.id == e.first.id }}" }

                    state.value.elements.find { it.first.id == e.first.id }?.first?.state =
                        TaskState.Incorrect

                    state.value.elements.find { it.second.id == e.second.id }?.second?.state =
                        TaskState.Incorrect
                    Logger.i { "update-> ${state.value.elements.find { it.second.id == e.second.id }}" }
                    hasError = true
                } else {
                    Logger.i { "find-> ${state.value.elements.find { it.first.id == e.first.id }}" }
                    state.value.elements.find { it.first.id == e.first.id }?.first?.state =
                        TaskState.Correct

                    state.value.elements.find { it.second.id == e.second.id }?.second?.state =
                        TaskState.Correct
                    Logger.i { "update-> ${state.value.elements.find { it.second.id == e.second.id }}" }

                }
            }
            state.value =
                state.value.copy(elements = answerList, hasError = hasError, isChecked = true)

            inChecking(false)

        }
    }

}

fun randomUniqueListShuffle(a: Int, b: Int, k: Int, rnd: Random = Random): List<Int> {
    require(a <= b) { "a must be <= b" }
    val n = b - a + 1
    require(k in 0 until n) { "k must be in range 0..(b-a+1)" }

    val list = (a until b).toMutableList()
    list.shuffle(rnd)
    return list.subList(0, k)
}
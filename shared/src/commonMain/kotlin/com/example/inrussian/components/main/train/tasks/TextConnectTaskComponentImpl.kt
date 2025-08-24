package com.example.inrussian.components.main.train.tasks

import co.touchlab.kermit.Logger
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.example.inrussian.models.models.TaskBody.TextTask
import com.example.inrussian.models.models.TaskState
import com.example.inrussian.models.models.task.AudioTask
import com.example.inrussian.models.models.task.ImageConnectTaskModel
import com.example.inrussian.models.models.task.Task
import com.example.inrussian.models.models.task.TextInsertTask
import com.example.inrussian.models.models.task.TextTaskModel
import com.example.inrussian.utils.componentCoroutineScope
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class TextConnectTaskComponentImpl(
    component: ComponentContext,
    private val onContinueClicked: (Boolean) -> Unit,
    listTextTasks: TextTask
) :
    TextConnectTaskComponent,
    ComponentContext by component {
    val correctList = mutableListOf<Pair<Task, Task>>()
    override val state = MutableValue(TextConnectTaskComponent.State())
    val scope = componentCoroutineScope()

    init {

        val list = MutableList<Pair<Task, Task>?>(listTextTasks.variant.size) { null }
        val order = randomUniqueListShuffle(0, list.size, list.size)
        Logger.i { order.toString() }
        listTextTasks.variant.forEachIndexed { index, element ->
            correctList.add(
                TextTaskModel(
                    id = Uuid.random().toString(), text = element.first
                ) to TextTaskModel(
                    id = Uuid.random().toString(), isAnswer = true, text = element.second
                )
            )
            list[index] = (TextTaskModel(
                id = Uuid.random().toString(), text = element.first
            ) to TextTaskModel(
                id = Uuid.random().toString(),
                isAnswer = true,
                text = listTextTasks.variant[order[index]].second
            ))

        }
        state.value = state.value.copy(list.map { it!! })
    }
    override fun onTaskClick(taskId: String) {
        val cur = state.value
        if (cur.isChecked) return

        val elements = cur.elements
        val clickedIndex = elements.indexOfFirst { it.first.id == taskId || it.second.id == taskId }
        if (clickedIndex == -1) return

        val (first, second) = elements[clickedIndex]
        val clickedElement = if (first.id == taskId) first else second

        // Проверяем, находится ли элемент в существующей паре
        val existingPair = cur.pairs.find { it.first.id == taskId || it.second.id == taskId }

        if (existingPair != null) {
            // Удаляем пару и снимаем выделение с обоих элементов
            val updatedPairs = cur.pairs - existingPair
            val updatedElements = elements.map { elementPair ->
                val newFirst = if (elementPair.first.id == existingPair.first.id ||
                    elementPair.first.id == existingPair.second.id) {
                    elementPair.first.withState(TaskState.NotSelected)
                } else {
                    elementPair.first
                }

                val newSecond = if (elementPair.second.id == existingPair.first.id ||
                    elementPair.second.id == existingPair.second.id) {
                    elementPair.second.withState(TaskState.NotSelected)
                } else {
                    elementPair.second
                }

                newFirst to newSecond
            }

            // Проверяем, все ли возможные пары соединены
            val isAllConnectedNow = updatedPairs.size == elements.size

            state.value = cur.copy(
                elements = updatedElements,
                pairs = updatedPairs as MutableList<Pair<Task, Task>>,
                selectedTask = null,
                isAllConnected = isAllConnectedNow
            )
            return
        }

        // Если элемент не в паре
        when (cur.selectedTask) {
            null -> {
                // Выделяем новый элемент
                val updatedElements = elements.map { elementPair ->
                    if (elementPair.first.id == taskId) {
                        elementPair.first.withState(TaskState.Selected) to elementPair.second
                    } else if (elementPair.second.id == taskId) {
                        elementPair.first to elementPair.second.withState(TaskState.Selected)
                    } else {
                        elementPair
                    }
                }
                state.value = cur.copy(
                    elements = updatedElements,
                    selectedTask = clickedElement
                )
            }
            else -> {
                if (cur.selectedTask.id == taskId) {
                    // Снимаем выделение с текущего элемента
                    val updatedElements = elements.map { elementPair ->
                        if (elementPair.first.id == taskId) {
                            elementPair.first.withState(TaskState.NotSelected) to elementPair.second
                        } else if (elementPair.second.id == taskId) {
                            elementPair.first to elementPair.second.withState(TaskState.NotSelected)
                        } else {
                            elementPair
                        }
                    }
                    state.value = cur.copy(
                        elements = updatedElements,
                        selectedTask = null
                    )
                } else {
                    // Проверяем, можно ли создать пару (элементы из разных категорий)
                    val isSameType = (cur.selectedTask as? TextTaskModel)?.isAnswer ==
                            (clickedElement as? TextTaskModel)?.isAnswer

                    if (isSameType) {
                        // Элементы из одной категории - просто заменяем выделенный элемент
                        val updatedElements = elements.map { elementPair ->
                            val newFirst = when {
                                elementPair.first.id == cur.selectedTask.id ->
                                    elementPair.first.withState(TaskState.NotSelected)
                                elementPair.first.id == taskId ->
                                    elementPair.first.withState(TaskState.Selected)
                                else -> elementPair.first
                            }

                            val newSecond = when {
                                elementPair.second.id == cur.selectedTask.id ->
                                    elementPair.second.withState(TaskState.NotSelected)
                                elementPair.second.id == taskId ->
                                    elementPair.second.withState(TaskState.Selected)
                                else -> elementPair.second
                            }

                            newFirst to newSecond
                        }

                        state.value = cur.copy(
                            elements = updatedElements,
                            selectedTask = clickedElement
                        )
                    } else {
                        // Элементы из разных категорий - создаем пару
                        val newPair = cur.selectedTask to clickedElement
                        val updatedPairs = cur.pairs + newPair

                        val updatedElements = elements.map { elementPair ->
                            val newFirst = when (elementPair.first.id) {
                                cur.selectedTask.id, taskId ->
                                    elementPair.first.withState(TaskState.Connect)
                                else -> elementPair.first
                            }

                            val newSecond = when (elementPair.second.id) {
                                cur.selectedTask.id, taskId ->
                                    elementPair.second.withState(TaskState.Connect)
                                else -> elementPair.second
                            }

                            newFirst to newSecond
                        }

                        // Проверяем, все ли возможные пары соединены
                        val isAllConnectedNow = updatedPairs.size == elements.size

                        state.value = cur.copy(
                            elements = updatedElements,
                            pairs = updatedPairs as MutableList<Pair<Task, Task>>,
                            selectedTask = null,
                            isAllConnected = isAllConnectedNow
                        )
                    }
                }
            }
        }
    }
    fun Task.withState(state: TaskState): Task = when (this) {
        is TextTaskModel -> TextTaskModel(id = id, text = text, state = state)
        is AudioTask -> AudioTask(id = id, isPlay, state = state)
        is ImageConnectTaskModel -> ImageConnectTaskModel(
            id = id,
            imageUrl = imageUrl,
            state = state
        )

        is TextInsertTask -> TextInsertTask(label, sentence, id, state)
    }

    override fun onContinueClick() {
        if (state.value.isChecked) {
            state.value.hasError?.let { onContinueClicked(it) }
        } else {
            state.value.pairs.forEachIndexed { i, e ->
                if (e.second.id != correctList[i].second.id)
                    state.value = state.value.copy(
                        hasError = false, isChecked = true
                    )
            }
            if (state.value.hasError == null)
                state.value = state.value.copy(
                    hasError = true, isChecked = true
                )
        }
    }
}
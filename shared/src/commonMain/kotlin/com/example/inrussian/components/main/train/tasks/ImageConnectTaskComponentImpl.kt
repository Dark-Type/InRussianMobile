package com.example.inrussian.components.main.train.tasks

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.example.inrussian.models.models.task.ImageConnectTaskModel
import com.example.inrussian.models.models.task.Task
import com.example.inrussian.models.models.TaskBody
import com.example.inrussian.models.models.TaskState
import com.example.inrussian.models.models.task.TextTaskModel
import com.example.inrussian.utils.componentCoroutineScope
import kotlin.random.Random
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class ImageConnectTaskComponentImpl(
    context: ComponentContext,
    private val onContinueClicked: (Boolean) -> Unit,
    listImageTasks: TaskBody.ImageTask
) : ImageConnectTaskComponent, ComponentContext by context {
    val correctList = mutableListOf<Pair<Task, Task>>()
    override val state = MutableValue(ImageConnectTaskComponent.State())
    val scope = componentCoroutineScope()

    init {
        val list = MutableList<Pair<Task, Task>?>(listImageTasks.variant.size) { null }
        val order = randomUniqueListShuffle(0, list.size, list.size)
        listImageTasks.variant.forEachIndexed { index, element ->
            correctList.add(
                TextTaskModel(
                    id = Uuid.random().toString(), isAnswer = true, text = element.first
                ) to TextTaskModel(
                    id = Uuid.random().toString(), text = element.second
                )
            )
            list[index] = (TextTaskModel(
                id = Uuid.random().toString(), text = element.first
            ) to TextTaskModel(
                id = Uuid.random().toString(),
                isAnswer = true,
                text = listImageTasks.variant[order[index]].second
            ))

        }
        state.value = state.value.copy(list.map { it!! })
    }

    override fun onTaskClick(taskId: String) {
        val element = state.value.elements.find { it.first.id == taskId ||it.second.id == taskId  }
        val currentElement = if(element?.first?.id!=taskId)element?.first else element.second

        if (!state.value.isChecked) {
            if (taskId == state.value.electedTask?.id) {
                state.value = state.value.copy(electedTask = null)
            } else if (currentElement?.state != TaskState.Selected) {
                currentElement?.state = TaskState.Selected
                if (state.value.electedTask == null)
                    state.value = state.value.copy(electedTask = currentElement)
                else
                    state.value =
                        state.value.copy(elements = state.value.elements.toMutableList().apply {
                            element?.let { add(it) }
                        }, electedTask = null)
            }
        }

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
fun randomUniqueListShuffle(a: Int, b: Int, k: Int, rnd: Random = Random): List<Int> {
    require(a <= b) { "a must be <= b" }
    val n = b - a+1
    require(k in 0 until n) { "k must be in range 0..(b-a+1)" }

    val list = (a until b).toMutableList()
    list.shuffle(rnd)
    return list.subList(0, k)
}
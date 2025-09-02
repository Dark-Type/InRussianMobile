package com.example.inrussian.components.main.train.tasks

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.example.inrussian.models.models.TaskBody

class TextInputTaskComponent(
    context: ComponentContext,
    private val onContinueClicked: (Boolean) -> Unit,
    private val inChecking: (Boolean) -> Unit,
    private val onButtonEnable: (Boolean) -> Unit,
    listAudioTasks: TaskBody.TextInputTask,
) : TextInputTask {
    private val _state = MutableValue(
        TextInputTask.State(listAudioTasks.task.map {
            TextInputTask.InputBlock(
                text = it.text,
                label = it.label,
                gaps = it.gaps.map {
                    TextInputTask.InputGap(
                        answer = it.correctWord,
                        pos = it.index
                    )
                })
        })
    )
    override val state: Value<TextInputTask.State> = _state

    override fun onTextChange(blockIndex: Int, gapPos: Int, newText: String) {
        _state.update { s ->
            val newBlocks = s.blocks.mapIndexed { idx, block ->
                if (idx != blockIndex) return@mapIndexed block
                val newGaps = block.gaps.map { gap ->
                    if (gap.pos == gapPos) gap.copy(answer = newText) else gap
                }
                block.copy(gaps = newGaps)
            }
            s.copy(blocks = newBlocks)
        }
    }

    override fun onContinueClick() {
        TODO("Not yet implemented")
    }

}
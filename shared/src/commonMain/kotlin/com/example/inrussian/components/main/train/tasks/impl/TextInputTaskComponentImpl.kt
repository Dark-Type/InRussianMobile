package com.example.inrussian.components.main.train.tasks.impl

import co.touchlab.kermit.Logger
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.example.inrussian.components.main.train.tasks.interfaces.TextInputTaskComponent
import com.example.inrussian.components.main.train.tasks.interfaces.TextInputTaskComponent.InputBlock
import com.example.inrussian.components.main.train.tasks.interfaces.TextInputTaskComponent.InputGap
import com.example.inrussian.components.main.train.tasks.interfaces.TextInputTaskComponent.State
import com.example.inrussian.models.models.TaskBody

class TextInputTaskComponentImpl(
    component: ComponentContext,
    private val onContinueClicked: (Boolean) -> Unit,
    private val inChecking: (Boolean) -> Unit,
    private val onButtonEnable: (Boolean) -> Unit,
    listTextInputTasks: TaskBody.TextInputTask,
) : TextInputTaskComponent, ComponentContext by component {
    private val _state = MutableValue(State(blocks = listTextInputTasks.task.map { m ->
        InputBlock(
            words = m.text.split(" ").filter { it != " " },
            label = m.label,
            gaps = m.gaps.map { InputGap(pos = it.index, answer = it.correctWord) }
                .sortedBy { it.pos })
    }))
    override val state: Value<State> = _state
    override fun onTextChange(blockIndex: Int, gapId: String, newText: String) {
        _state.update { currentState ->
            val updatedBlocks = currentState.blocks.mapIndexed { index, block ->
                if (index != blockIndex) block
                else {
                    val updatedGaps = block.gaps.map { gap ->
                        if (gap.id != gapId) gap
                        else gap.copy(input = newText)
                    }
                    block.copy(gaps = updatedGaps)
                }
            }

            val allFilled = updatedBlocks.all { block ->
                block.gaps.all { it.input.isNotBlank() }
            }

            currentState.copy(blocks = updatedBlocks, isButtonEnabled = allFilled)
        }

        onButtonEnable(_state.value.isButtonEnabled)
    }

    override fun onContinueClick() {
        inChecking(true)

        _state.update { currentState ->
            val updatedBlocks = currentState.blocks.map { block ->
                val updatedGaps = block.gaps.map { gap ->
                    val newState = if (gap.input.equals(gap.answer, ignoreCase = true)) {
                        TextInputTaskComponent.GapState.Success
                    } else {
                        onButtonEnable(false)
                        TextInputTaskComponent.GapState.Error
                    }
                    gap.copy(state = newState)
                }
                block.copy(gaps = updatedGaps)
            }

            currentState.copy(blocks = updatedBlocks)
        }

        val isAllCorrect = _state.value.blocks.all { block ->
            block.gaps.all { it.state is TextInputTaskComponent.GapState.Success }
        }
        onContinueClicked(isAllCorrect)

    }

}
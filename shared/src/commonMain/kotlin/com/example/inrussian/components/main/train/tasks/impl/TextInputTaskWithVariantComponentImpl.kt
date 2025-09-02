package com.example.inrussian.components.main.train.tasks.impl

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.example.inrussian.components.main.train.tasks.interfaces.TextInputTaskWithVariantComponent
import com.example.inrussian.models.models.TaskBody

class TextInputTaskWithVariantComponentImpl(
    component: ComponentContext,
    private val onContinueClicked: (Boolean) -> Unit,
    private val inChecking: (Boolean) -> Unit,
    private val onButtonEnable: (Boolean) -> Unit,
    listTextInputTasks: TaskBody.TextInputWithVariantTask,
) : TextInputTaskWithVariantComponent, ComponentContext by component {
    private val _state = MutableValue(
        TextInputTaskWithVariantComponent.State(
            blocks = TextInputTaskWithVariantComponent.InputBlock(
                words = listTextInputTasks.task.text.split(" ").filter { it.isNotBlank() },
                label = listTextInputTasks.task.label,
                gaps = listTextInputTasks.task.gaps.map { gap ->
                    TextInputTaskWithVariantComponent.InputGap(
                        variants = gap.variants,
                        answer = gap.correctVariant,
                        pos = gap.position
                    )
                }.sortedBy { it.pos }
            )
        )
    )

    init {
        onButtonEnable(false)
        inChecking(true)
    }

    override val state: Value<TextInputTaskWithVariantComponent.State> = _state

    override fun onVariantSelected(blockIndex: Int, gapId: String, selectedVariant: String) {
        _state.update { currentState ->
            val updatedGaps = currentState.blocks?.gaps?.map { gap ->
                if (gap.id != gapId) gap
                else gap.copy(selected = selectedVariant)
            }

            val allFilled = updatedGaps?.all { it.selected.isNotBlank() }

            currentState.copy(
                blocks = currentState.blocks?.copy(gaps = updatedGaps ?: listOf()),
                isButtonEnabled = allFilled == true
            )
        }

        onButtonEnable(_state.value.isButtonEnabled)
    }

    override fun onContinueClick() {


        _state.update { currentState ->
            val updatedGaps = currentState.blocks?.gaps?.map { gap ->
                val newState = if (gap.selected == gap.answer) {
                    TextInputTaskWithVariantComponent.GapState.Success
                } else {
                    TextInputTaskWithVariantComponent.GapState.Error
                }
                gap.copy(state = newState)
            }

            currentState.copy(
                blocks = currentState.blocks?.copy(gaps = updatedGaps ?: listOf())
            )
        }

        val isAllCorrect = _state.value.blocks?.gaps?.all { gap ->
            gap.state is TextInputTaskWithVariantComponent.GapState.Success
        }
        inChecking(isAllCorrect == true)

        onContinueClicked(isAllCorrect == true)
    }
}
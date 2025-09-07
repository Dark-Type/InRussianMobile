package com.example.inrussian.components.main.train.tasks.impl

import co.touchlab.kermit.Logger
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.example.inrussian.components.main.train.tasks.interfaces.ImageAndSelectComponent
import com.example.inrussian.components.main.train.tasks.interfaces.ImageAndSelectComponent.State
import com.example.inrussian.data.client.models.Variant
import com.example.inrussian.data.client.models.VariantState
import com.example.inrussian.repository.main.train.TaskBody.ImageAndSelect
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class ImageAndSelectComponentImpl(
    component: ComponentContext,
    private val onContinueClicked: (Boolean) -> Unit,
    private val onButtonEnable: (Boolean) -> Unit,
    imageAndSelectTask: ImageAndSelect,
) : ImageAndSelectComponent, ComponentContext by component {
    
    private val _state = MutableValue<State>(
        State(
            imageBlocks = imageAndSelectTask.task.imageBlocks,
            variants = imageAndSelectTask.task.variants.map {
                Variant(
                    isCorrect = it.second, text = it.first
                )
            })
    )
    
    
    init {
        _state.subscribe {
            onButtonEnable(it.selectedElementId != null)
            Logger.d { it.toString() }
            
        }
    }
    
    override val state: Value<State> = _state
    
    override fun onSelect(variantId: Uuid) {
        updateState(variantId, VariantState.Selected)
        _state.update {
            it.copy(selectedElementId = variantId)
        }
    }
    
    private fun updateState(id: Uuid, state: VariantState) {
        _state.update { s ->
            s.copy(variants = s.variants.toMutableList().map { v ->
                if (v.id == id) Variant(v.id, v.isCorrect, v.text, state)
                else Variant(
                    v.id, v.isCorrect, v.text, VariantState.NotSelected
                )
            })
        }
    }
    
    override fun onContinueClick() {
        if (state.value.selectedElementId != null) {
            val isCorrectAnswer =
                state.value.variants.find { it.id == state.value.selectedElementId }?.isCorrect == true
            
            updateState(
                state.value.selectedElementId!!,
                if (isCorrectAnswer) VariantState.Correct else VariantState.Incorrect
            )
            onContinueClicked(_state.value.variants.all {
                (it.isCorrect && it.state is VariantState.Selected) || !it.isCorrect
            })
        }
        
    }
}
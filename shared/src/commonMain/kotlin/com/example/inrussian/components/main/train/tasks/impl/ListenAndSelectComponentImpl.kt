package com.example.inrussian.components.main.train.tasks.impl

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.example.inrussian.components.main.train.tasks.interfaces.ListenAndSelectComponent
import com.example.inrussian.components.main.train.tasks.interfaces.ListenAndSelectComponent.State
import com.example.inrussian.models.models.task.TaskBody.ListenAndSelect
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class ListenAndSelectComponentImpl(
    component: ComponentContext,
    private val onContinueClicked: (Boolean) -> Unit,
    private val onButtonEnable: (Boolean) -> Unit,
    listenAndSelectTask: ListenAndSelect,
) : ListenAndSelectComponent, ComponentContext by component {
    
    private val _state = MutableValue<State>(
        State(
            audioBlocks = listenAndSelectTask.task.audioBlocks,
            variants = listenAndSelectTask.task.variants.map {
                ListenAndSelectComponent.Variant(
                    isCorrect = it.second, text = it.first
                )
            })
    )
    
    
    init {
        _state.subscribe {
            onButtonEnable(it.selectedElementId != null)
        }
    }
    
    override val state: Value<State> = _state
    
    override fun onSelect(variantId: Uuid) {
        updateState(variantId, ListenAndSelectComponent.VariantState.Selected)
        _state.update {
            it.copy(selectedElementId = variantId)
        }
    }
    
    private fun updateState(id: Uuid, state: ListenAndSelectComponent.VariantState) {
        _state.update { s ->
            val variants = s.variants.toMutableList()
            
            variants.map { v ->
                if (v.id == id) ListenAndSelectComponent.Variant(v.id, v.isCorrect, v.text, state)
                else ListenAndSelectComponent.Variant(
                    v.id, v.isCorrect, v.text, ListenAndSelectComponent.VariantState.NotSelected
                )
            }
            s.copy()
        }
    }
    
    override fun onContinueClick() {
        if (state.value.selectedElementId != null) {
            val isCorrectAnswer =
                state.value.variants.find { it.id == state.value.selectedElementId }?.isCorrect == true
            
            updateState(
                state.value.selectedElementId!!,
                if (isCorrectAnswer) ListenAndSelectComponent.VariantState.Correct else ListenAndSelectComponent.VariantState.Incorrect
            )
            onContinueClicked(_state.value.variants.all {
                (it.isCorrect && it.state is ListenAndSelectComponent.VariantState.Selected) || !it.isCorrect
            })
        }
        
    }
}
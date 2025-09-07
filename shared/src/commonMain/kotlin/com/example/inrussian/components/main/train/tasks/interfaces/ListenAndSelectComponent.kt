package com.example.inrussian.components.main.train.tasks.interfaces

import com.arkivanov.decompose.value.Value
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
interface ListenAndSelectComponent {
    
    val state: Value<State>
    
    data class State  (
        val selectedElementId: Uuid? = null,
        val audioBlocks: List<com.example.inrussian.repository.main.train.AudioBlocks> = listOf(),
        val variants: List<Variant> = listOf()
    )
    
    sealed interface VariantState {
        data object Selected : VariantState
        data object NotSelected : VariantState
        data object Correct : VariantState
        data object Incorrect : VariantState
    }
    
    data class Variant (
        val id: Uuid = Uuid.random(),
        val isCorrect: Boolean = false,
        val text: String = "",
        val state: VariantState = VariantState.NotSelected
    )
    
    fun onSelect(variantId: Uuid)
    fun onContinueClick()
}
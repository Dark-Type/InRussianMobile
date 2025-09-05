package com.example.inrussian.components.main.train.tasks.interfaces

import com.arkivanov.decompose.value.Value
import com.example.inrussian.models.models.task.support.AudioBlocks
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

interface ListenAndSelectComponent {
    
    val state: Value<State>
    
    data class State(
        val selectedElementId: String? = null,
        val audioBlocks: List<AudioBlocks> = listOf(),
        val variants: List<Variant> = listOf()
    )
    
    sealed interface VariantState {
        data object Selected : VariantState
        data object NotSelected : VariantState
        data object Correct : VariantState
        data object Incorrect : VariantState
    }
    
    data class Variant @OptIn(ExperimentalUuidApi::class) constructor(
        val id: Uuid,
        val isCorrect: Boolean,
        val text: String,
        val state: VariantState
    )
}
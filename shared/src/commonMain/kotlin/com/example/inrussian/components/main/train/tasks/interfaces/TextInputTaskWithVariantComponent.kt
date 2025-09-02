package com.example.inrussian.components.main.train.tasks.interfaces

import com.arkivanov.decompose.value.Value
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

interface TextInputTaskWithVariantComponent {
    val state: Value<State>

    data class State(
        val isButtonEnabled: Boolean = false,
        val blocks: InputBlock? = null
    )

    data class InputBlock(
        val words: List<String>,
        val label: String,
        val gaps: List<InputGap>
    )

    data class InputGap @OptIn(ExperimentalUuidApi::class) constructor(
        val id: String = Uuid.Companion.random().toString(),
        val selected: String = "",
        val variants: List<String> = listOf(),
        val answer: String = "",
        val state: GapState = GapState.Entering,
        val pos: Int = -1,
    )

    fun onVariantSelected(blockIndex: Int, gapId: String, selectedVariant: String)

    fun onContinueClick()


    sealed interface GapState {
        data object Error : GapState
        data object Success : GapState
        data object Entering : GapState
    }
}
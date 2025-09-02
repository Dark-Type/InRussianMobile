package com.example.inrussian.components.main.train.tasks

import com.arkivanov.decompose.value.Value
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

interface TextInputTask {
    val state: Value<State>

    data class State(
        val blocks: List<InputBlock>
    )

    data class InputBlock(
        val text: String,
        val label: String,
        val  gaps: List<InputGap>
    )

    data class InputGap @OptIn(ExperimentalUuidApi::class) constructor(
        val id: String = Uuid.random().toString(),
        val input: String = "",
        val answer: String = "",
        val state: GapState = GapState.Entering,
        val pos: Int = -1,
    )

    fun onTextChange(blockIndex: Int, gapPos: Int, newText: String)

    fun onContinueClick()


    sealed interface GapState {
        data object Error : GapState
        data object Success : GapState
        data object Entering : GapState
    }
}
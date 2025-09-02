package com.example.inrussian.components.main.train.tasks.interfaces

import com.arkivanov.decompose.value.Value
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

interface TextInputTaskComponent {
    val state: Value<State>

    data class State(
        val isButtonEnabled: Boolean = false,
        val blocks: List<InputBlock> = listOf()
    )

    data class InputBlock(
        val words: List<String>,
        val label: String,
        val gaps: List<InputGap>
    )

    data class InputGap @OptIn(ExperimentalUuidApi::class) constructor(
        val id: String = Uuid.Companion.random().toString(),
        val input: String = "",
        val answer: String = "",
        val state: GapState = GapState.Entering,
        val pos: Int = -1,
    )

    fun onTextChange(
        blockIndex: Int,
        gapId: String,
        newText: String
    )

    fun onContinueClick()


    sealed interface GapState {
        data object Error : GapState
        data object Success : GapState
        data object Entering : GapState
    }
}
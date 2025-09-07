package com.example.inrussian.components.main.train.tasks.interfaces

import com.arkivanov.decompose.value.Value
import com.example.inrussian.data.client.models.Variant
import com.example.inrussian.repository.main.train.AudioBlocks
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
interface ListenAndSelectComponent {

    val state: Value<State>

    data class State(
        val selectedElementId: Uuid? = null,
        val audioBlocks: List<AudioBlocks> = listOf(),
        val variants: List<Variant> = listOf()
    )


    fun onSelect(variantId: Uuid)
    fun onContinueClick()
}
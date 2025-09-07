package com.example.inrussian.components.main.train.tasks.interfaces

import com.arkivanov.decompose.value.Value
import com.example.inrussian.data.client.models.Variant
import com.example.inrussian.repository.main.train.ImageBlocks
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
interface ImageAndSelectComponent {
    
    val state: Value<State>
    
    data class State(
        val selectedElementId: Uuid? = null,
        val imageBlocks: List<ImageBlocks> = listOf(),
        val variants: List<Variant> = listOf()
    )

    
    fun onSelect(variantId: Uuid)
    fun onContinueClick()
}
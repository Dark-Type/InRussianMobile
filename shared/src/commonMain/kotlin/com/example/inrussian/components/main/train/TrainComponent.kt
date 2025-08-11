package com.example.inrussian.components.main.train

import com.arkivanov.decompose.ComponentContext

interface TrainComponent

sealed class TrainOutput

class DefaultTrainComponent(
    componentContext: ComponentContext,
    private val onOutput: (TrainOutput) -> Unit
) : TrainComponent, ComponentContext by componentContext {

    fun onSomeTrainAction() {
    }
}
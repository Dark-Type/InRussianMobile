package com.example.inrussian.components.main.home

import com.arkivanov.decompose.ComponentContext

interface HomeComponent

sealed class HomeOutput

class DefaultHomeComponent(
    componentContext: ComponentContext,
    private val onOutput: (HomeOutput) -> Unit
) : HomeComponent, ComponentContext by componentContext {

    fun onSomeAction() {
    }
}
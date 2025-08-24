package com.example.inrussian.components.main.train.tasks

import com.arkivanov.decompose.ComponentContext
import com.example.inrussian.components.main.train.tasks.TextConnectTaskComponent

class TextConnectTaskComponentImpl(component: ComponentContext, onClick: (Boolean) -> Unit) :
    TextConnectTaskComponent,
    ComponentContext by component {
    override fun onTaskClick(taskId: String) {
        TODO("Not yet implemented")
    }
}
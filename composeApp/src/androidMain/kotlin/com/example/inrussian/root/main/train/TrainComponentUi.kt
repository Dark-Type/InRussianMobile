package com.example.inrussian.root.main.train

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import androidx.compose.runtime.getValue
import com.example.inrussian.components.main.train.TrainComponent

@Composable
fun TrainComponentUi(component: TrainComponent) {
    val stack by component.childStack.subscribeAsState()
    when (val current = stack.active.instance) {
        is TrainComponent.Child.CoursesChild -> TrainCoursesScreen(current.component)
        is TrainComponent.Child.SectionDetailChild -> SectionDetailHost(current.component)
    }
}



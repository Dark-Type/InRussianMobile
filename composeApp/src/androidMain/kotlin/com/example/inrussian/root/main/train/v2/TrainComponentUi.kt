package com.example.inrussian.root.main.train.v2

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import androidx.compose.runtime.getValue
import com.example.inrussian.components.main.train.TrainComponent

@Composable
fun TrainComponentUi(component: TrainComponent) {
    val stack by component.childStack.subscribeAsState()

    when (val child = stack.active.instance) {
        is TrainComponent.Child.CoursesChild -> {
            TrainCoursesScreen(child.component)
        }
        is TrainComponent.Child.ThemeTasksChild -> {
            ThemeTasksScreen(child.component)
        }
    }
}

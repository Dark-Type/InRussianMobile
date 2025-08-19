package com.example.inrussian.root.main.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.example.inrussian.components.main.home.HomeComponent

@Composable
fun HomeComponentUi(component: HomeComponent) {
    val stack by component.childStack.subscribeAsState()
    val current = stack.active.instance
    when (current) {
        is HomeComponent.Child.CoursesChild -> CoursesListComponentUi(current.component)
        is HomeComponent.Child.CourseDetailsChild -> CourseDetailsComponentUi(current.component)
    }
}


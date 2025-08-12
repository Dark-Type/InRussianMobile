package com.example.inrussian.root.main.home

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.example.inrussian.components.main.home.HomeComponent

@Composable
fun HomeComponentUi(component: HomeComponent) {
    val stack by component.childStack.subscribeAsState()
    val current = stack.active.instance

    when (current) {
        is HomeComponent.Child.CoursesChild -> {
            Column {
                Text("Список курсов:")
                current.component.items.forEach { course ->
                    Button(onClick = { current.component.onItemClick(course.id) }) {
                        Text(course.title)
                    }
                }
            }
        }
        is HomeComponent.Child.CourseDetailsChild -> {
            Column {
                Text("Детали курса: ${current.component.courseId}")
                Button(onClick = { current.component.onBack() }) {
                    Text("Назад")
                }
            }
        }
    }
}
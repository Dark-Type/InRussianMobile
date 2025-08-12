package com.example.inrussian.root.main.train

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.example.inrussian.components.main.train.DefaultSectionDetailComponent
import com.example.inrussian.components.main.train.TrainComponent

@Composable
fun TrainComponentUi(component: TrainComponent) {
    val stack by component.childStack.subscribeAsState()
    val current = stack.active.instance

    when (current) {
        is TrainComponent.Child.CoursesChild -> {
            Column {
                Text("Список курсов")
                current.component.sections.forEach { section ->
                    Button(onClick = { current.component.onSectionClick(section.id) }) {
                        Text(section.title)
                    }
                }
            }
        }
        is TrainComponent.Child.SectionDetailChild -> {
            val sectionDetail = current.component
            val defaultSectionDetail = sectionDetail as? DefaultSectionDetailComponent
            if (defaultSectionDetail != null) {
                val innerStack by defaultSectionDetail.childStack.subscribeAsState()
                val innerCurrent = innerStack.active.instance
                when (innerCurrent) {
                    is DefaultSectionDetailComponent.InnerChild.DetailsChild -> {
                        Column {
                            Text("Детали секции: ${defaultSectionDetail.sectionId}")
                            Button(onClick = { defaultSectionDetail.openTasks(com.example.inrussian.components.main.train.TasksOption.All) }) {
                                Text("Открыть задачи")
                            }
                            Button(onClick = { defaultSectionDetail.onBack() }) {
                                Text("Назад")
                            }
                        }
                    }
                    is DefaultSectionDetailComponent.InnerChild.TasksChild -> {
                        TasksComponentUi(innerCurrent.component)
                    }
                }
            }
        }
    }
}
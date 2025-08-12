package com.example.inrussian.root.main.train

import androidx.compose.runtime.Composable
import androidx.compose.material3.Button
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import com.example.inrussian.components.main.train.TasksComponent

@Composable
fun TasksComponentUi(component: TasksComponent) {
    Column {
        Text("Задачи секции: ${component.sectionId}")
        Text("Фильтр: ${component.option}")
        Button(onClick = { component.onBack() }) {
            Text("Назад")
        }
    }
}
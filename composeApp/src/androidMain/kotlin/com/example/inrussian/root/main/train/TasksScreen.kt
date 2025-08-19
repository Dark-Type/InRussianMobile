package com.example.inrussian.root.main.train

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.example.inrussian.components.main.train.TasksComponent
import com.example.inrussian.components.main.train.TasksOption

@Composable
fun TasksScreen(component: TasksComponent) {
    val state by component.state.subscribeAsState()

    if (state.isLoading) {
        Box(Modifier.Companion.fillMaxSize(), contentAlignment = Alignment.Companion.Center) {
            CircularProgressIndicator()
        }
        return
    }

    when (state.option) {
        TasksOption.Continue -> ContinueQueueTasks(state, component)
        TasksOption.All, TasksOption.Theory, TasksOption.Practice -> FilteredTasksList(state, component)
    }
}
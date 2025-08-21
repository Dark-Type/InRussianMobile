package com.example.inrussian.root.main.train

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arkivanov.decompose.value.MutableValue
import com.example.inrussian.components.main.train.TasksComponent
import com.example.inrussian.components.main.train.TasksOption
import com.example.inrussian.components.main.train.TasksState
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksScreen(component: TasksComponent) {
    val state by component.state.subscribeAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Назад") },
                navigationIcon = {
                    IconButton(onClick = { component.onBack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                }
            )
        }
    ) { padding ->
        if (state.isLoading) {
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }

        when (state.option) {
            TasksOption.Continue ->
                Box(Modifier.padding(padding)) {
                    ContinueQueueTasks(state, component)
                }

            TasksOption.All, TasksOption.Theory, TasksOption.Practice ->
                Box(Modifier.padding(padding)) {
                    FilteredTasksList(state, component)
                }
        }
    }
}

class TasksScreen : TasksComponent {
    override val state = MutableValue(
        TasksState(isLoading = true, option = TasksOption.Continue, sectionId = "sectionId")
    )

    override fun selectOption(optionId: String) {
       state.value = state.value.copy()
    }

    override fun toggleOption(optionId: String) {
        TODO("Not yet implemented")
    }

    override fun reorderWordOrder(newOrder: List<String>) {
        TODO("Not yet implemented")
    }

    override fun updateTextInput(text: String) {
        TODO("Not yet implemented")
    }

    override fun submitAnswer() {
        TODO("Not yet implemented")
    }

    override fun nextAfterResult() {
        TODO("Not yet implemented")
    }

    override fun markCurrentAs(correct: Boolean) {
        TODO("Not yet implemented")
    }

    override fun onBack() {
        TODO("Not yet implemented")
    }

    @Composable
    @Preview
    fun Preview() {
        TasksScreen(this)
    }

}
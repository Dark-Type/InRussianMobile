package com.example.inrussian.root.main.train

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.value.Value
import com.example.inrussian.components.main.train.TasksComponent
import com.example.inrussian.components.main.train.TasksState

@Composable
fun ContinueQueueTasks(state: TasksState, component: TasksComponent) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Очередь задач", style = MaterialTheme.typography.titleLarge)

        LinearProgressIndicator(
            progress = { state.progressPercent / 100f },
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            "Прогресс секции: ${state.progressPercent}% " +
                    "(${state.completedTasks}/${state.totalTasks})",
            style = MaterialTheme.typography.bodySmall
        )

        val full = state.activeFullTask
        if (full == null) {
            Text("Очередь пуста.", style = MaterialTheme.typography.bodyMedium)
        } else {
            TaskCard(
                fullTask = full,
                state = state,
                showQueueMeta = true,
                remainingInQueue = state.remainingInQueue,
                onSelect = component::selectOption,
                onToggle = component::toggleOption,
                onReorderWordOrder = component::reorderWordOrder,
                onWordAdd = { component.selectOption(it) },
                onWordRemove = { /* remove from order */ },
                onTextChange = component::updateTextInput
            )
            Spacer(Modifier.height(8.dp))
            SubmissionArea(state = state, component = component)
        }

        Spacer(Modifier.weight(1f))
        OutlinedButton(
            onClick = { component.onBack() },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Назад") }
    }
}

class ContinueQueueTasks : TasksComponent {
    override val state: Value<TasksState>
        get() = TODO("Not yet implemented")

    override fun selectOption(optionId: String) {
        TODO("Not yet implemented")
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
    @Preview(showBackground = true, showSystemUi = true)
    fun Preview() {
       // ContinueQueueTasks(TasksState(),this)
    }
}
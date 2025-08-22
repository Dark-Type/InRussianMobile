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
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.unit.dp
import com.example.inrussian.components.main.train.TasksComponent
import com.example.inrussian.components.main.train.TasksOption
import com.example.inrussian.components.main.train.TasksState
import com.example.inrussian.ui.theme.Orange

@Composable
fun FilteredTasksList(state: TasksState, component: TasksComponent) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        val title = when (state.option) {
            TasksOption.All -> "Все задачи"
            TasksOption.Theory -> "Теория"
            TasksOption.Practice -> "Практика"
            TasksOption.Continue -> "Очередь"
        }
        Text(title, style = MaterialTheme.typography.titleLarge)

        LinearProgressIndicator(
            progress = { state.progressPercent / 100f },
            modifier = Modifier
                .fillMaxWidth()
                .height(5.dp),
            color = Orange,
            trackColor = White
        )
        Text(
            "Прогресс секции: ${state.progressPercent}% " +
                    "(${state.completedTasks}/${state.totalTasks})",
            style = MaterialTheme.typography.bodySmall
        )

        val current = state.activeFullTask
        if (current == null) {
            Text("Нет задач для отображения", style = MaterialTheme.typography.bodyMedium)
        } else {
            Text(
                "Задача ${state.currentIndex + 1} из ${state.filteredTasks.size}",
                style = MaterialTheme.typography.labelSmall
            )
            TaskCard(
                fullTask = current,
                state = state,
                showQueueMeta = false,
                remainingInQueue = 0,
                onSelect = component::selectOption,
                onToggle = component::toggleOption,
                onReorderWordOrder = component::reorderWordOrder,
                onWordAdd = { component.selectOption(it) },
                onWordRemove = { /* optional */ },
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
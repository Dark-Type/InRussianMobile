package com.example.inrussian.root.main.train

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.inrussian.components.main.train.TaskKind
import com.example.inrussian.components.main.train.TasksComponent
import com.example.inrussian.components.main.train.TasksOption
import com.example.inrussian.components.main.train.TasksState

@Composable
fun FilteredTasksList(state: TasksState, component: TasksComponent) {
    Column(
        Modifier.Companion
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        val title = when (state.option) {
            TasksOption.All -> "Все задачи"
            TasksOption.Theory -> "Теоретические задачи"
            TasksOption.Practice -> "Практические задачи"
            TasksOption.Continue -> "Очередь"
        }
        Text(title, style = MaterialTheme.typography.titleLarge)
        LinearProgressIndicator(
        progress = { state.progressPercent / 100f },
            color = ProgressIndicatorDefaults.linearColor,
        trackColor = ProgressIndicatorDefaults.linearTrackColor,
        strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
        )
        Text("Прогресс: ${state.progressPercent}% (${state.completedTasks}/${state.totalTasks})")

        if (state.filteredTasks.isEmpty()) {
            Text("Нет задач для отображения")
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(state.filteredTasks) { task ->
                    ElevatedCard(Modifier.Companion.fillMaxWidth()) {
                        Column(Modifier.Companion.padding(12.dp)) {
                            Text(task.text, style = MaterialTheme.typography.bodyLarge)
                            Text(
                                if (task.kind == TaskKind.THEORY) "Теория" else "Практика",
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                }
            }
        }

        OutlinedButton(
            onClick = { component.onBack() },
            modifier = Modifier.Companion.fillMaxWidth()
        ) {
            Text("Назад")
        }
    }
}
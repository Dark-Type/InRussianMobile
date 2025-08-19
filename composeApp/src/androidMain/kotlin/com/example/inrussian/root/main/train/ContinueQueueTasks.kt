package com.example.inrussian.root.main.train

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
import com.example.inrussian.components.main.train.TasksState

@Composable
fun ContinueQueueTasks(state: TasksState, component: TasksComponent) {
    Column(
        Modifier.Companion
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Очередь задач", style = MaterialTheme.typography.titleLarge)
        LinearProgressIndicator(
        progress = { state.progressPercent / 100f },
            color = ProgressIndicatorDefaults.linearColor,
        trackColor = ProgressIndicatorDefaults.linearTrackColor,
        strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
        )
        Text("Прогресс секции: ${state.progressPercent}% (${state.completedTasks}/${state.totalTasks})")

        val task = state.currentQueueTask
        if (task == null) {
            Text("Очередь пуста.")
        } else {
            Card(Modifier.Companion.fillMaxWidth()) {
                Column(
                    Modifier.Companion.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        if (task.kind == TaskKind.THEORY) "ТЕОРИЯ" else "ПРАКТИКА",
                        style = MaterialTheme.typography.labelSmall
                    )
                    Text(task.text, style = MaterialTheme.typography.bodyLarge)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = { component.markCurrentAs(true) },
                            modifier = Modifier.Companion.weight(1f)
                        ) { Text("Верно") }
                        OutlinedButton(
                            onClick = { component.markCurrentAs(false) },
                            modifier = Modifier.Companion.weight(1f)
                        ) { Text("Неверно") }
                    }
                }
            }
            Text("В очереди: ${state.remainingInQueue}")
        }

        OutlinedButton(
            onClick = { component.onBack() },
            modifier = Modifier.Companion.fillMaxWidth()
        ) {
            Text("Назад")
        }
    }
}
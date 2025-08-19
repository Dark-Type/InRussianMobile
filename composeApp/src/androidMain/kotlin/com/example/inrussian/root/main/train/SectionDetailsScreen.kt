package com.example.inrussian.root.main.train

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.inrussian.components.main.train.SectionDetailComponent
import com.example.inrussian.components.main.train.SectionDetailState
import com.example.inrussian.components.main.train.TasksOption

@Composable
fun SectionDetailsScreen(
    component: SectionDetailComponent,
    state: SectionDetailState
) {
    val section = state.section
    if (state.isLoading || section == null) {
        Box(Modifier.Companion.fillMaxSize(), contentAlignment = Alignment.Companion.Center) {
            CircularProgressIndicator()
        }
        return
    }
    Column(
        Modifier.Companion
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(section.title, style = MaterialTheme.typography.titleLarge)
        LinearProgressIndicator(
        progress = { section.progressPercent / 100f },
            color = ProgressIndicatorDefaults.linearColor,
        trackColor = ProgressIndicatorDefaults.linearTrackColor,
        strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
        )
        Text("Прогресс: ${section.progressPercent}% (${section.completedTasks}/${section.totalTasks})")
        Text("Теория: ${section.completedTheory}/${section.totalTheory}")
        Text("Практика: ${section.completedPractice}/${section.totalPractice}")

        Spacer(Modifier.Companion.height(16.dp))
        Text("Режимы:", style = MaterialTheme.typography.titleMedium)

        Button(
            onClick = { component.openTasks(TasksOption.Continue) },
            modifier = Modifier.Companion.fillMaxWidth()
        ) {
            Text("Продолжить (очередь)")
        }
        OutlinedButton(
            onClick = { component.openTasks(TasksOption.All) },
            modifier = Modifier.Companion.fillMaxWidth()
        ) {
            Text("Все задачи")
        }
        OutlinedButton(
            onClick = { component.openTasks(TasksOption.Theory) },
            modifier = Modifier.Companion.fillMaxWidth()
        ) {
            Text("Только теория")
        }
        OutlinedButton(
            onClick = { component.openTasks(TasksOption.Practice) },
            modifier = Modifier.Companion.fillMaxWidth()
        ) {
            Text("Только практика")
        }

        Spacer(Modifier.Companion.height(8.dp))
        OutlinedButton(
            onClick = { component.onBack() },
            modifier = Modifier.Companion.fillMaxWidth()
        ) {
            Text("Назад")
        }
    }
}
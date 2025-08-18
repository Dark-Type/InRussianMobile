package com.example.inrussian.root.main.train

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.inrussian.components.main.train.DefaultSectionDetailComponent
import com.example.inrussian.components.main.train.SectionDetailComponent
import com.example.inrussian.components.main.train.SectionDetailState
import com.example.inrussian.components.main.train.TaskKind
import com.example.inrussian.components.main.train.TasksComponent
import com.example.inrussian.components.main.train.TasksOption
import com.example.inrussian.components.main.train.TasksState
import com.example.inrussian.components.main.train.TrainComponent
import com.example.inrussian.components.main.train.TrainCoursesListComponent

@Composable
fun TrainComponentUi(component: TrainComponent) {
    val stack by component.childStack.subscribeAsState()
    when (val current = stack.active.instance) {
        is TrainComponent.Child.CoursesChild -> TrainCoursesScreen(current.component)
        is TrainComponent.Child.SectionDetailChild -> SectionDetailHost(current.component)
    }
}

/* ---------------- Courses Screen ---------------- */

@Composable
private fun TrainCoursesScreen(component: TrainCoursesListComponent) {
    val state by component.state.subscribeAsState()

    if (state.isLoading) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        items(state.courses) { courseWithSections ->
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(courseWithSections.course.name, style = MaterialTheme.typography.titleMedium)
                courseWithSections.sections.forEach { section ->
                    ElevatedCard(
                        onClick = { component.onSectionClick(section.id) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(Modifier.padding(12.dp)) {
                            Text(section.title, style = MaterialTheme.typography.titleSmall)
                            Spacer(Modifier.height(8.dp))
                            LinearProgressIndicator(
                                progress = (section.progressPercent / 100f)
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                "Прогресс: ${section.progressPercent}% (${section.completedTasks}/${section.totalTasks})",
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                }
            }
        }
    }
}

/* ---------------- Section Detail with inner stack ---------------- */

@Composable
private fun SectionDetailHost(component: SectionDetailComponent) {
    val state by component.state.subscribeAsState()

    val default = component as? DefaultSectionDetailComponent
    if (default == null) {
        Text("Unsupported SectionDetailComponent type")
        return
    }

    val innerStack by default.childStack.subscribeAsState()

    when (val inner = innerStack.active.instance) {
        is DefaultSectionDetailComponent.InnerChild.DetailsChild ->
            SectionDetailsScreen(component, state)

        is DefaultSectionDetailComponent.InnerChild.TasksChild ->
            TasksScreen(inner.component)
    }

    if (state.showCompletionDialog) {
        AlertDialog(
            onDismissRequest = { component.onBack() },
            title = { Text("Секция завершена") },
            text = { Text("Вы завершили все задачи секции!") },
            confirmButton = {
                TextButton(onClick = { component.onBack() }) {
                    Text("OK")
                }
            }
        )
    }
}

@Composable
private fun SectionDetailsScreen(
    component: SectionDetailComponent,
    state: SectionDetailState
) {
    val section = state.section
    if (state.isLoading || section == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }
    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(section.title, style = MaterialTheme.typography.titleLarge)
        LinearProgressIndicator(progress = section.progressPercent / 100f)
        Text("Прогресс: ${section.progressPercent}% (${section.completedTasks}/${section.totalTasks})")
        Text("Теория: ${section.completedTheory}/${section.totalTheory}")
        Text("Практика: ${section.completedPractice}/${section.totalPractice}")

        Spacer(Modifier.height(16.dp))
        Text("Режимы:", style = MaterialTheme.typography.titleMedium)

        Button(onClick = { component.openTasks(TasksOption.Continue) }, modifier = Modifier.fillMaxWidth()) {
            Text("Продолжить (очередь)")
        }
        OutlinedButton(onClick = { component.openTasks(TasksOption.All) }, modifier = Modifier.fillMaxWidth()) {
            Text("Все задачи")
        }
        OutlinedButton(onClick = { component.openTasks(TasksOption.Theory) }, modifier = Modifier.fillMaxWidth()) {
            Text("Только теория")
        }
        OutlinedButton(onClick = { component.openTasks(TasksOption.Practice) }, modifier = Modifier.fillMaxWidth()) {
            Text("Только практика")
        }

        Spacer(Modifier.height(8.dp))
        OutlinedButton(onClick = { component.onBack() }, modifier = Modifier.fillMaxWidth()) {
            Text("Назад")
        }
    }
}

/* ---------------- Tasks Screen ---------------- */

@Composable
private fun TasksScreen(component: TasksComponent) {
    val state by component.state.subscribeAsState()

    if (state.isLoading) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    when (state.option) {
        TasksOption.Continue -> ContinueQueueTasks(state, component)
        TasksOption.All, TasksOption.Theory, TasksOption.Practice -> FilteredTasksList(state, component)
    }
}

@Composable
private fun ContinueQueueTasks(state: TasksState, component: TasksComponent) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Очередь задач", style = MaterialTheme.typography.titleLarge)
        LinearProgressIndicator(progress = state.progressPercent / 100f)
        Text("Прогресс секции: ${state.progressPercent}% (${state.completedTasks}/${state.totalTasks})")

        val task = state.currentQueueTask
        if (task == null) {
            Text("Очередь пуста.")
        } else {
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(if (task.kind == TaskKind.THEORY) "ТЕОРИЯ" else "ПРАКТИКА",
                        style = MaterialTheme.typography.labelSmall)
                    Text(task.text, style = MaterialTheme.typography.bodyLarge)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = { component.markCurrentAs(true) },
                            modifier = Modifier.weight(1f)
                        ) { Text("Верно") }
                        OutlinedButton(
                            onClick = { component.markCurrentAs(false) },
                            modifier = Modifier.weight(1f)
                        ) { Text("Неверно") }
                    }
                }
            }
            Text("В очереди: ${state.remainingInQueue}")
        }

        OutlinedButton(onClick = { component.onBack() }, modifier = Modifier.fillMaxWidth()) {
            Text("Назад")
        }
    }
}

@Composable
private fun FilteredTasksList(state: TasksState, component: TasksComponent) {
    Column(
        Modifier
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
        LinearProgressIndicator(progress = state.progressPercent / 100f)
        Text("Прогресс: ${state.progressPercent}% (${state.completedTasks}/${state.totalTasks})")

        if (state.filteredTasks.isEmpty()) {
            Text("Нет задач для отображения")
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(state.filteredTasks) { task ->
                    ElevatedCard(Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(12.dp)) {
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

        OutlinedButton(onClick = { component.onBack() }, modifier = Modifier.fillMaxWidth()) {
            Text("Назад")
        }
    }
}
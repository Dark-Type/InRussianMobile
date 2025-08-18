package com.example.inrussian.root.main.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.example.inrussian.components.main.home.CourseDetailsComponent
import com.example.inrussian.components.main.home.CoursesListComponent
import com.example.inrussian.components.main.home.HomeComponent

@Composable
fun HomeComponentUi(component: HomeComponent) {
    val stack by component.childStack.subscribeAsState()
    val current = stack.active.instance
    when (current) {
        is HomeComponent.Child.CoursesChild -> CoursesListComponentUi(current.component)
        is HomeComponent.Child.CourseDetailsChild -> CourseDetailsComponentUi(current.component)
    }
}

@Composable
fun CoursesListComponentUi(component: CoursesListComponent) {
    val state by component.state.subscribeAsState()
    Column(Modifier.padding(16.dp)) {
        if (state.isLoading) {
            Text("Загрузка...")
        } else {
            if (state.enrolled.isNotEmpty()) {
                Text("Ваши курсы", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))
                state.enrolled.forEach { course ->
                    Button(
                        onClick = { component.onEnrolledCourseClick(course.id) },
                        modifier = Modifier.fillMaxWidth()
                    ) { Text(course.name) }
                    Spacer(Modifier.height(4.dp))
                }
                Spacer(Modifier.height(16.dp))
            }
            Text("Рекомендованные", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            state.recommended.forEach { course ->
                OutlinedButton(
                    onClick = { component.onRecommendedCourseClick(course.id) },
                    modifier = Modifier.fillMaxWidth()
                ) { Text(course.name) }
                Spacer(Modifier.height(4.dp))
            }
        }
    }
}

@Composable
fun CourseDetailsComponentUi(component: CourseDetailsComponent) {
    val state by component.state.subscribeAsState()
    val course = state.course
    Column(Modifier.padding(16.dp)) {
        if (state.isLoading || course == null) {
            Text("Загрузка...")
        } else {
            Text(course.name, style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(8.dp))
            course.description?.let {
                Text(it, style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(8.dp))
            }
            Text("Автор: ${course.authorId}")
            Spacer(Modifier.height(8.dp))
            LinearProgressIndicator(progress = state.progressPercent / 100f)
            Spacer(Modifier.height(4.dp))
            Text("Прогресс: ${state.progressPercent}%")

            Spacer(Modifier.height(16.dp))

            Button(onClick = { component.toggleEnroll() }) {
                Text(if (state.isEnrolled) "Отписаться" else "Записаться")
            }

            Spacer(Modifier.height(24.dp))
            Text("Секции", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            state.sections.forEach { section ->
                val sectionProgress =
                    if (section.totalLessons == 0) 0
                    else (section.completedLessons * 100 / section.totalLessons)
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Text(section.title, style = MaterialTheme.typography.bodyLarge)
                    LinearProgressIndicator(progress = sectionProgress / 100f)
                    Text(
                        "$sectionProgress% (${section.completedLessons}/${section.totalLessons})",
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }
        Spacer(Modifier.height(16.dp))
        OutlinedButton(onClick = { component.onBack() }) {
            Text("Назад")
        }
    }
}
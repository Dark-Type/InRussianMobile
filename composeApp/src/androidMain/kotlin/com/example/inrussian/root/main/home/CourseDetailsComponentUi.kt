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
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arkivanov.decompose.value.MutableValue
import com.example.inrussian.components.main.home.CourseDetailsComponent
import com.example.inrussian.components.main.home.CourseDetailsState

@Composable
fun CourseDetailsComponentUi(component: CourseDetailsComponent) {
    val state by component.state.subscribeAsState()
    val course = state.course
    Column(Modifier.Companion.padding(16.dp)) {
        if (state.isLoading || course == null) {
            Text("Загрузка...")
        } else {
            Text(course.name, style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.Companion.height(8.dp))
            course.description?.let {
                Text(it, style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.Companion.height(8.dp))
            }
            Text("Автор: ${course.authorId}")
            Spacer(Modifier.Companion.height(8.dp))
            LinearProgressIndicator(
                progress = { state.progressPercent / 100f },
                color = ProgressIndicatorDefaults.linearColor,
                trackColor = ProgressIndicatorDefaults.linearTrackColor,
                strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
            )
            Spacer(Modifier.Companion.height(4.dp))
            Text("Прогресс: ${state.progressPercent}%")

            Spacer(Modifier.Companion.height(16.dp))

            Button(onClick = { component.toggleEnroll() }) {
                Text(if (state.isEnrolled) "Отписаться" else "Записаться")
            }

            Spacer(Modifier.Companion.height(24.dp))
            Text("Секции", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.Companion.height(8.dp))
            state.sections.forEach { section ->
                val sectionProgress =
                    if (section.totalLessons == 0) 0
                    else (section.completedLessons * 100 / section.totalLessons)
                Column(
                    Modifier.Companion
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Text(section.title, style = MaterialTheme.typography.bodyLarge)
                    LinearProgressIndicator(
                        progress = { sectionProgress / 100f },
                        color = ProgressIndicatorDefaults.linearColor,
                        trackColor = ProgressIndicatorDefaults.linearTrackColor,
                        strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
                    )
                    Text(
                        "$sectionProgress% (${section.completedLessons}/${section.totalLessons})",
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }
        Spacer(Modifier.Companion.height(16.dp))
        OutlinedButton(onClick = { component.onBack() }) {
            Text("Назад")
        }
    }
}

class CourseDetailsComponentUi() : CourseDetailsComponent {
    override val courseId: String = ""
    override val state = MutableValue(
        CourseDetailsState(
            course = null,
            isEnrolled = false,
            sections = emptyList(),
            progressPercent = 0,
            isLoading = true
        )
    )

    override fun toggleEnroll() {
        TODO("Not yet implemented")
    }

    override fun onBack() {
        TODO("Not yet implemented")
    }

    @Preview(showBackground = true, showSystemUi = true)
    @Composable
    fun Preview() {
        CourseDetailsComponentUi(this)
    }

}
package com.example.inrussian.root.main.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import com.example.inrussian.components.main.home.CoursesListComponent

@Composable
fun CoursesListComponentUi(component: CoursesListComponent) {
    val state by component.state.subscribeAsState()
    Column(Modifier.Companion.padding(16.dp)) {
        if (state.isLoading) {
            Text("Загрузка...")
        } else {
            if (state.enrolled.isNotEmpty()) {
                Text("Ваши курсы", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.Companion.height(8.dp))
                state.enrolled.forEach { course ->
                    Button(
                        onClick = { component.onEnrolledCourseClick(course.id) },
                        modifier = Modifier.Companion.fillMaxWidth()
                    ) { Text(course.name) }
                    Spacer(Modifier.Companion.height(4.dp))
                }
                Spacer(Modifier.Companion.height(16.dp))
            }
            Text("Рекомендованные", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.Companion.height(8.dp))
            state.recommended.forEach { course ->
                OutlinedButton(
                    onClick = { component.onRecommendedCourseClick(course.id) },
                    modifier = Modifier.Companion.fillMaxWidth()
                ) { Text(course.name) }
                Spacer(Modifier.Companion.height(4.dp))
            }
        }
    }
}

class CoursesListComponentUi : CourseDetailsComponent {
    override val courseId: String
        get() = ""
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

    @Composable
    @Preview(showBackground = true, showSystemUi = true)
    fun Preview() {
        CourseDetailsComponentUi(this)
    }

}
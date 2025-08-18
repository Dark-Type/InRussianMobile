package com.example.inrussian.components.main.train

import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Serializable
data class Course(
    val id: String,
    val name: String
)

@Serializable
data class Section(
    val id: String,
    val courseId: String,
    val title: String,
    val totalTheory: Int,
    val totalPractice: Int,
    val completedTheory: Int,
    val completedPractice: Int,
    val themes: List<ThemeMeta>
) {
    val totalTasks: Int get() = totalTheory + totalPractice
    val completedTasks: Int get() = completedTheory + completedPractice
    val progressPercent: Int =
        if (totalTasks == 0) 0 else (completedTasks * 100 / totalTasks)
}

@Serializable
data class ThemeMeta(
    val id: String,
    val order: Int,
    val theoryCount: Int,
    val practiceCount: Int
)

enum class TaskKind { THEORY, PRACTICE }

/* Simplified Task entity */
data class Task(
    val id: String,
    val sectionId: String,
    val themeId: String,
    val kind: TaskKind,
    val text: String
)

data class UserTaskQueueItem @OptIn(ExperimentalTime::class, ExperimentalUuidApi::class) constructor(
    val id: String = Uuid.random().toString(),
    val userId: String,
    val taskId: String,
    val themeId: String,
    val sectionId: String,
    val queuePosition: Int,
    val isOriginalTask: Boolean,
    val isRetryTask: Boolean,
    val originalTaskId: String?,
    val createdAt: String = Clock.System.now().toString()
)

/* UI States */

data class TrainCoursesState(
    val isLoading: Boolean = true,
    val courses: List<CourseWithSections> = emptyList()
)

data class CourseWithSections(
    val course: Course,
    val sections: List<Section>
)

data class SectionDetailState(
    val isLoading: Boolean = true,
    val section: Section? = null,
    val selectedOption: TasksOption? = null,
    val showCompletionDialog: Boolean = false
)

data class TasksState(
    val isLoading: Boolean = true,
    val option: TasksOption,
    val sectionId: String,
    val progressPercent: Int = 0,
    val totalTasks: Int = 0,
    val completedTasks: Int = 0,
    val currentQueueTask: Task? = null,
    val queueSize: Int = 0,
    val remainingInQueue: Int = 0,
    val filteredTasks: List<Task> = emptyList(),
    val completed: Boolean = false,
    val message: String? = null
)
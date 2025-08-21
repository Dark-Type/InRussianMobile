package com.example.inrussian.components.main.train

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/* -----------------------------------------------------------
 * COURSE & SECTION
 * ----------------------------------------------------------- */

@Serializable
data class ShortCourse(
    val id: String,
    val title: String,
    val percent: Float = 0f,
)

@Serializable
data class ThemeMeta(
    val id: String,
    val order: Int,
    val theoryCount: Int,
    val practiceCount: Int
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

/* -----------------------------------------------------------
 * TASK TYPES
 * ----------------------------------------------------------- */

enum class TaskType {
    LISTEN_AND_CHOOSE,
    READ_AND_CHOOSE,
    LOOK_AND_CHOOSE,
    MATCH_AUDIO_TEXT,
    MATCH_TEXT_TEXT,
    WORD_ORDER
}

val THEORY_TASK_TYPES = setOf(TaskType.LISTEN_AND_CHOOSE, TaskType.READ_AND_CHOOSE)
val PRACTICE_TASK_TYPES = setOf(
    TaskType.MATCH_AUDIO_TEXT,
    TaskType.MATCH_TEXT_TEXT,
    TaskType.WORD_ORDER,
    TaskType.LOOK_AND_CHOOSE
)

enum class AnswerType {
    MULTIPLE_CHOICE_SHORT,
    MULTIPLE_CHOICE_LONG,
    SINGLE_CHOICE_SHORT,
    SINGLE_CHOICE_LONG,
    TEXT_INPUT,
    WORD_ORDER,
    WORD_SELECTION
}

/* -----------------------------------------------------------
 * MODULAR TASK ENTITIES
 * ----------------------------------------------------------- */

@Serializable
data class Task(
    val id: String,
    val sectionId: String,
    val themeId: String,
    val type: TaskType,
    val text: String? = null,
    val isTraining: Boolean = false,
    val answer: TaskAnswerItem? = null
) {
    val isTheory: Boolean get() = type in THEORY_TASK_TYPES
}

@Serializable
data class TaskContentItem @OptIn(ExperimentalUuidApi::class) constructor(
    val id: String = Uuid.random().toString(),
    val taskId: String,
    val contentType: ContentType,
    val contentId: String? = null,
    val description: String? = null,
    val transcription: String? = null,
    val translation: String? = null,
    val orderNum: Int
)

enum class ContentType {
    AUDIO, IMAGE, TEXT, VIDEO, AVATAR
}

@Serializable
data class TaskAnswerOptionItem @OptIn(ExperimentalUuidApi::class) constructor(
    val id: String = Uuid.random().toString(),
    val taskId: String,
    val optionText: String? = null,
    val optionAudioId: String? = null,
    val isCorrect: Boolean = false,
    val orderNum: Int
)

@Serializable
data class TaskAnswerItem @OptIn(ExperimentalUuidApi::class) constructor(
    val id: String = Uuid.random().toString(),
    val taskId: String,
    val answerType: AnswerType,
    val correctAnswer: JsonElement
)

/* Aggregated task used in TasksComponent state */
data class FullTask(
    val task: Task,
    val contents: List<TaskContentItem>,
    val options: List<TaskAnswerOptionItem>,
    val answer: TaskAnswerItem? = task.answer
)

/* -----------------------------------------------------------
 * QUEUE
 * ----------------------------------------------------------- */

data class UserTaskQueueItem @OptIn(ExperimentalUuidApi::class) constructor(
    val id: String = Uuid.random().toString(),
    val userId: String,
    val taskId: String,
    val themeId: String,
    val sectionId: String,
    val queuePosition: Int,
    val isOriginalTask: Boolean,
    val isRetryTask: Boolean,
    val originalTaskId: String?
)

/* -----------------------------------------------------------
 * UI STATES
 * (Only those that are shared; keep others near their components if preferred)
 * ----------------------------------------------------------- */

data class TrainCoursesState(
    val isLoading: Boolean = true,
    val courses: List<CourseWithSections> = emptyList()
)

data class CourseWithSections(
    val course: ShortCourse,
    val sections: List<Section>
)

data class SectionDetailState(
    val isLoading: Boolean = true,
    val section: Section? = null,
    val selectedOption: TasksOption? = null,
    val showCompletionDialog: Boolean = false
)

/* High-level tasks list / queue state (lightweight).
 * The richer per-task interaction state lives in the TasksComponent.
 */
data class TasksListState(
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

enum class TasksOption {
    All, Theory, Practice, Continue
}
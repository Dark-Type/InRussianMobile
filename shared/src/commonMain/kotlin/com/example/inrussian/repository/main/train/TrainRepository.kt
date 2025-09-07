package com.example.inrussian.repository.main.train


import com.example.inrussian.models.models.task.TaskModel
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import kotlinx.serialization.Serializable

@Serializable
data class UserCourseEnrollmentItem(
    val userId: String,
    val courseId: String,
    val enrolledAt: String,
    val completedAt: String?,
    val progress: Double
)

@Serializable
data class Course(
    val id: String,
    val name: String,
    val posterId: String?,
    val description: String?,
    val authorId: String,
    val authorUrl: String?,
    val language: String,
    val isPublished: Boolean,
    val createdAt: String,
    val updatedAt: String
)

@Serializable
data class CourseAverageStatsDTO @OptIn(ExperimentalUuidApi::class) constructor(
    val courseId: Uuid,
    val courseAverage: AverageProgressDTO?,
    val themesAverage: List<ThemeAverageDTO>
)

@Serializable
data class ThemeAverageDTO @OptIn(ExperimentalUuidApi::class) constructor(
    val themeId: Uuid,
    val courseId: Uuid,
    val solvedTasksAvg: Double,
    val totalTasksAvg: Double,
    val percentAvg: Double,
    val averageTimeMsAvg: Double,
    val participants: Int,
    val lastUpdatedAt: String?
)

@Serializable
data class AverageProgressDTO(
    val solvedTasksAvg: Double,
    val totalTasksAvg: Double,
    val percentAvg: Double,
    val averageTimeMsAvg: Double,
    val participants: Int,
    val lastUpdatedAt: String?
)

@Serializable
data class ThemeTreeNode(
    val theme: Theme,
    val children: List<ThemeTreeNode>
)

@Serializable
data class ThemeContents(
    val theme: Theme,
    val childThemes: List<Theme>,
    val tasks: List<TaskModel>
)

@Serializable
data class Theme(
    val id: String,
    val courseId: String,
    val parentThemeId: String?,
    val name: String,
    val description: String?,
    val position: Int,
    val createdAt: String
)


@Serializable
enum class TaskType {
    LISTEN_AND_CHOOSE,
    READ_AND_CHOOSE,
    LOOK_AND_CHOOSE,
    MATCH_AUDIO_TEXT,
    MATCH_TEXT_TEXT,
    WRITE, LISTEN, READ, SPEAK, REMIND, MARK, FILL, CONNECT_AUDIO, CONNECT_IMAGE, CONNECT_TRANSLATE, SELECT
}

@Serializable
sealed interface TaskBody {
    @Serializable
    data class TextConnectTask(val variant: List<Pair<String, String>>) : TaskBody

    @Serializable
    data class AudioConnectTask(val variant: List<Pair<String, String>>) : TaskBody

    @Serializable
    data class ImageConnectTask(val variant: List<Pair<String, String>>) : TaskBody

    @Serializable
    data class TextInputTask(val task: List<TextInputModel>) : TaskBody

    @Serializable
    data class TextInputWithVariantTask(val task: TextInputWithVariantModel) : TaskBody

    @Serializable
    data class ListenAndSelect(val task: ListenAndSelectModel) : TaskBody

    @Serializable
    data class ImageAndSelect(val task: ImageAndSelectModel) : TaskBody

    @Serializable
    data class ConstructSentenceTask(val task: ConstructSentenceModel) : TaskBody

    @Serializable
    data class SelectWordsTask(val task: SelectWordsModel) : TaskBody
}

@Serializable
data class TextInputModel(val label: String, val text: String, val gaps: List<Gap>)

@Serializable
data class Gap(val correctWord: String, val indexWord: Int)

@Serializable
data class TextInputWithVariantModel(
    val label: String,
    val text: String,
    val gaps: List<GapWithVariantModel>
)

@Serializable
data class GapWithVariantModel(
    val indexWord: Int,
    val variants: List<String>,
    val correctVariant: String
)

@Serializable
data class ListenAndSelectModel(
    val audioBlocks: List<AudioBlocks>,
    val variants: List<Pair<String, Boolean>>
)

@Serializable
data class AudioBlocks(
    val name: String,
    val description: String?,
    val audio: String,
    val descriptionTranslate: String?,
)

@Serializable
data class ImageAndSelectModel(
    val imageBlocks: List<ImageBlocks>,
    val variants: List<Pair<String, Boolean>>
)

@Serializable
data class ConstructSentenceModel(val audio: String?, val variants: List<String>)

@Serializable
data class SelectWordsModel(val audio: String, val variants: List<Pair<String, Boolean>>)

@Serializable
data class ImageBlocks(
    val name: String,
    val description: String?,
    val image: String,
    val descriptionTranslate: String?,
)

@Serializable
data class NextTaskResult @OptIn(ExperimentalUuidApi::class) constructor(
    val taskId: Uuid,
    val themeId: Uuid
)

@Serializable
data class AttemptRequest @OptIn(ExperimentalUuidApi::class) constructor(
    val attemptId: Uuid,
    val taskId: Uuid,
    val attemptsCount: Int,
    val timeSpentMs: Long
)

@Serializable
data class UserAttemptDTO @OptIn(ExperimentalUuidApi::class) constructor(
    val attemptId: Uuid,
    val taskId: Uuid,
    val taskQuestion: String,
    val taskBody: TaskBody,
    val attemptsCount: Int,
    val timeSpentMs: Long,
    val createdAt: String
)

/* ============================================================
   DOMAIN FACADE FOR STORE (TaskResponse / FullTaskModel)
   Keep shape consistent with existing TrainStore usage.
   ============================================================ */

data class FullTaskModel(
    val id: String,
    val themeId: String,
    val question: String?,
    val body: TaskBody,
    val types: List<TaskType>,
    val createdAt: String,
    val updatedAt: String
)

data class TaskResponse(
    val percent: Float,
    val task: FullTaskModel?
)

/* ============================================================
   TRAIN REPOSITORY CONTRACT (used by TrainStoreFactory)
   ============================================================ */

interface TrainRepository {
    suspend fun userCourseEnrollments(): List<UserCourseEnrollmentItem>
    suspend fun course(courseId: String): Course
    suspend fun themeTree(courseId: String): List<ThemeTreeNode>
    suspend fun themeContents(themeId: String): ThemeContents
    suspend fun nextTask(themeId: String): TaskModel?
    suspend fun refresh()
    suspend fun submitAttempt(request: AttemptRequest): AttemptProgressResult
    suspend fun attemptsHistory(themeId: String): List<UserAttemptDTO>
    suspend fun courseAverageStats(courseId: String): CourseAverageStatsDTO
}

/* Progress info after attempt */
data class AttemptProgressResult(
    val themeId: String,
    val taskId: String,
    val solved: Int,
    val total: Int
)



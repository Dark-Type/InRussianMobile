package com.example.inrussian.repository.main.train

import com.example.inrussian.models.models.task.TaskModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlin.random.Random
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class MockTrainRepository(
    private val seed: Int = 77
) : TrainRepository {
    
    private val rnd = Random(seed)
    private val themesByCourse = mutableMapOf<String, List<ThemeTreeNode>>()
    private val tasksPerTheme = mutableMapOf<String, MutableList<TaskModel>>()
    private val solvedPerTheme = mutableMapOf<String, MutableSet<String>>()
    private val tasks = listOf<TaskModel>(
        TaskModel(
            taskType = listOf(),
            taskBody = TaskBody.TextInputWithVariantTask(
                task = TextInputWithVariantModel(
                    label = "Спикер 0",
                    text = "God, it's was really bad day",
                    gaps = listOf(
                        GapWithVariantModel(
                            indexWord = 0,
                            variants = listOf("bad", "amazing", "awesome"),
                            correctVariant = "bad"
                        ), GapWithVariantModel(
                            indexWord = 4,
                            variants = listOf("amazing", "bad", "great"),
                            correctVariant = "great"
                        )
                    )
                )
            ),
            question = "тем временем был уже 4 час утра"
        ),
        TaskModel(
            taskType = listOf(TaskType.LISTEN),
            taskBody = TaskBody.TextInputTask(
                task = listOf(
                    TextInputModel(
                        label = "Спикер 0",
                        text = "Ехал грека через реку видит сидит рак",
                        gaps = listOf(Gap("Greka", 5))
                    ),
                    TextInputModel(
                        label = "Спикер 1",
                        text = "Ехал грека через реку видит сидит рак",
                        gaps = listOf(Gap("Greka", 5))
                    ),
                    TextInputModel(
                        label = "Спикер 2",
                        text = "Ехал грека через реку видит сидит рак",
                        gaps = listOf(Gap("Greka", 5))
                    ),
                    
                    )
            ),
            question = "Какое слово прорущено?"
        ),
        TaskModel(
            taskType = listOf(TaskType.LISTEN),
            taskBody = TaskBody.AudioConnectTask(
                variant = listOf("https://ru-d1.drivemusic.me/dl/aUXUtDqRsTEr0AyF_pBYhA/1756874921/download_music/2019/11/the-weeknd-blinding-lights.mp3" to "Blinding Lights")
            ),
            question = "что это за песня?"
        ),
        
        TaskModel(
            taskType = listOf(TaskType.SELECT, TaskType.LISTEN),
            taskBody = TaskBody.ListenAndSelect(
                task = ListenAndSelectModel(
                    audioBlocks = listOf(),
                    variants = listOf()
                )
            ),
            question = "Послушайте и скажите, что здесь не так"
        ),
        TaskModel(
            taskType = listOf(TaskType.READ, TaskType.READ),
            taskBody = TaskBody.TextConnectTask(
                variant = listOf("Left1 " to "Right1")
            ),
            question = "to be or not to be, that's the question question"
        ),
        TaskModel(
            taskType = listOf(TaskType.READ, TaskType.WRITE),
            taskBody = TaskBody.TextConnectTask(
                variant = listOf(
                    "Left1 " to "Right1",
                    "Left2 " to "Right2",
                    "Left3 " to "Right3",
                ),
            ),
            question = "to be or not to be, that's the question"
        ),
        TaskModel(
            taskType = listOf(TaskType.CONNECT_AUDIO, TaskType.SELECT),
            taskBody = TaskBody.TextConnectTask(
                variant = listOf("Connect1 " to "Match1")
            ),
            question = "to be or not to be, that's the question"
        ),
        TaskModel(
            id = Uuid.random().toString(),
            taskType = listOf(TaskType.SELECT),
            taskBody = TaskBody.ImageAndSelect(
                ImageAndSelectModel(
                    imageBlocks = listOf(
                        ImageBlocks(
                            name = "Matreshka",
                            image = "https://i.pinimg.com/originals/5d/af/6b/5daf6b907517384b8a28fdf3e4ed6e71.png"
                        ),
                        ImageBlocks(
                            name = "Will this work?",
                            description = "It's working",
                            descriptionTranslate = "It's working",
                            image = "https://selcdn.fedsp.com/teth/9/28056/1c65a044000abaa2.jpg"
                        )
                    ),
                    variants = listOf("Incorrect" to false, "Wrong" to false, "Correct" to true)
                )
            ),
            question = "",
            createdAt = "2024-01-01T00:00:00Z",
            updatedAt = "2024-01-01T00:00:00Z"
        ),
    )
    
    override suspend fun userCourseEnrollments(): List<UserCourseEnrollmentItem> =
        withContext(Dispatchers.IO) {
            delay(80)
            (1..3).map { idx ->
                UserCourseEnrollmentItem(
                    userId = "user",
                    courseId = "course_$idx",
                    enrolledAt = "2024-01-01T00:00:00Z",
                    completedAt = null,
                    progress = rnd.nextDouble(0.0, 0.85)
                )
            }
        }
    
    override suspend fun course(courseId: String): Course = withContext(Dispatchers.IO) {
        delay(50)
        Course(
            id = courseId,
            name = "Course $courseId",
            posterId = null,
            description = "Description for $courseId",
            authorId = "author_1",
            authorUrl = null,
            language = "en",
            isPublished = true,
            createdAt = "2024-01-01T00:00:00Z",
            updatedAt = "2024-01-01T00:00:00Z"
        )
    }
    
    override suspend fun themeTree(courseId: String): List<ThemeTreeNode> =
        withContext(Dispatchers.IO) {
            delay(70)
            themesByCourse.getOrPut(courseId) {
                generateThemeTree(courseId)
            }
        }
    
    override suspend fun themeContents(themeId: String): ThemeContents =
        withContext(Dispatchers.IO) {
            delay(60)
            val tasks = tasksPerTheme.getOrPut(themeId) {
                MutableList(rnd.nextInt(4, 8)) { i ->
                    mockTask(themeId, i)
                }
            }
            ThemeContents(
                theme = Theme(
                    id = themeId,
                    courseId = "unknown",
                    parentThemeId = null,
                    name = "Theme $themeId",
                    description = "Leaf theme $themeId",
                    position = 0,
                    createdAt = "2024-01-01T00:00:00Z"
                ),
                childThemes = emptyList(),
                tasks = tasks
            )
        }
    
    override suspend fun nextTask(themeId: String): TaskModel? =
        withContext(Dispatchers.IO) {
            delay(40)
            val tasks = tasksPerTheme.getOrPut(themeId) {
                MutableList(rnd.nextInt(4, 8)) { i -> mockTask(themeId, i) }
            }
            val solved = solvedPerTheme[themeId] ?: emptySet()
            tasks.firstOrNull { it.id !in solved }
        }
    
    override suspend fun refresh() {
        TODO("Not yet implemented")
    }
    
    @OptIn(ExperimentalUuidApi::class)
    override suspend fun submitAttempt(request: AttemptRequest): AttemptProgressResult =
        withContext(Dispatchers.IO) {
            delay(50)
            val themeId = findThemeIdByTask(request.taskId)
            val set = solvedPerTheme.getOrPut(themeId) { mutableSetOf() }
            val correct = request.attemptsCount == 1
            if (correct) set += request.taskId.toString()
            val total = tasksPerTheme[themeId]?.size ?: set.size
            AttemptProgressResult(
                themeId = themeId,
                taskId = request.taskId.toString(),
                solved = set.size,
                total = total
            )
        }
    
    @OptIn(ExperimentalUuidApi::class)
    override suspend fun attemptsHistory(themeId: String): List<UserAttemptDTO> =
        withContext(Dispatchers.IO) {
            delay(40)
            val solved = solvedPerTheme[themeId].orEmpty()
            solved.mapIndexed { idx, tId ->
                UserAttemptDTO(
                    attemptId = Uuid.random(),
                    taskId = Uuid.parse(tId),
                    taskQuestion = "Question for $tId",
                    taskBody = TaskBody.TextInputTask(emptyList()),
                    attemptsCount = 1,
                    timeSpentMs = 1500,
                    createdAt = "2024-01-01T00:00:00Z"
                )
            }
        }
    
    @OptIn(ExperimentalUuidApi::class)
    override suspend fun courseAverageStats(courseId: String): CourseAverageStatsDTO =
        withContext(Dispatchers.IO) {
            delay(40)
            CourseAverageStatsDTO(
                courseId = Uuid.random(),
                courseAverage = AverageProgressDTO(
                    3.0,
                    10.0,
                    30.0,
                    120000.0,
                    42,
                    "2024-01-01T00:00:00Z"
                ),
                themesAverage = emptyList()
            )
        }
    
    /* ---------------- Internal helpers ---------------- */
    
    private fun generateThemeTree(courseId: String): List<ThemeTreeNode> =
        List(rnd.nextInt(3, 5)) { idx ->
            buildNode(courseId, depth = 0, index = idx)
        }
    
    private fun buildNode(courseId: String, depth: Int, index: Int): ThemeTreeNode {
        val id = "${courseId}_theme_${depth}_$index"
        val hasChildren = depth < 2 && rnd.nextFloat() < 0.5f
        val children = if (hasChildren) {
            List(rnd.nextInt(2, 4)) { c -> buildNode(courseId, depth + 1, c) }
        } else emptyList()
        return ThemeTreeNode(
            theme = Theme(
                id = id,
                courseId = courseId,
                parentThemeId = null,
                name = "Theme $id",
                description = if (rnd.nextFloat() < 0.5f) "Description $id" else null,
                position = index,
                createdAt = "2024-01-01T00:00:00Z"
            ),
            children = children
        )
    }
    
    @OptIn(ExperimentalUuidApi::class)
    private fun mockTask(themeId: String, index: Int): TaskModel =
        tasks.shuffled().first()
    
    
    @OptIn(ExperimentalUuidApi::class)
    private fun findThemeIdByTask(taskId: Uuid): String {
        tasksPerTheme.forEach { (themeId, list) ->
            if (list.any { it.id == taskId.toString() }) return themeId
        }
        // fallback naive
        return tasksPerTheme.keys.firstOrNull() ?: "unknown_theme"
    }
}

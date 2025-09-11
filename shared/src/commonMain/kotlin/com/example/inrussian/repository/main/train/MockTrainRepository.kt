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
    private val tasks = listOf<TaskModel>(  //TODO ПОЧИНИ СУКА КНОПКУ
        // 1) Заполнение пропусков с вариантами (WRITE)
        TaskModel(
            taskType = listOf(TaskType.WRITE),
            taskBody = TaskBody.TextInputWithVariantTask(
                task = TextInputWithVariantModel(
                    label = "Выберите подходящие слова",
                    text = "Сегодня очень день, я на работу.", //++++
                    gaps = listOf(
                        GapWithVariantModel(
                            indexWord = 2, // «___» после «очень»
                            variants = listOf("хороший", "плохая", "тёплое"),
                            correctVariant = "хороший"
                        ),
                        GapWithVariantModel(
                            indexWord = 5, // «___» перед «на работу»
                            variants = listOf("иду", "поем", "сяду"),
                            correctVariant = "иду"
                        )
                    )
                )
            ),
            question = "Заполните пропуски, выбрав правильные варианты."
        ),

        // 2) Диктант: прослушай и впиши пропущенное слово (LISTEN)
        TaskModel(
            taskType = listOf(TaskType.LISTEN),
            taskBody = TaskBody.TextInputTask(
                task = listOf(
                    TextInputModel(
                        label = "Фраза 1",
                        text = "Вчера я купил свежие на рынке", //+++++
                        gaps = listOf(Gap("яблоки", 4))
                    ),
                    TextInputModel(
                        label = "Фраза 2",
                        text = "Завтра мы поедем в с друзьями",
                        gaps = listOf(Gap("музей", 4))
                    )
                )
            ),
            question = "Прослушайте фразы и впишите пропущенные слова."
        ),

        // 3) Соотнеси аудио и фразу (CONNECT_AUDIO)
        TaskModel(
            taskType = listOf(TaskType.CONNECT_AUDIO),
            taskBody = TaskBody.AudioConnectTask(
                variant = listOf(
                    "https://example.com/audio/privet.mp3" to "Привет",  //TODO звук вставить
                    "https://example.com/audio/spasibo.mp3" to "Спасибо",
                    "https://example.com/audio/do_svidaniya.mp3" to "До свидания"
                )
            ),
            question = "Соотнесите аудио и соответствующую фразу."
        ),

        // 4) Чтение: сопоставь фразу и перевод (READ)
        TaskModel(
            taskType = listOf(TaskType.READ),
            taskBody = TaskBody.TextConnectTask(
                variant = listOf(
                    "Здравствуйте!" to "Hello!",  //++++++
                    "Как вас зовут?" to "What is your name?",
                    "Мне нравится этот город" to "I like this city"
                )
            ),
            question = "Прочитайте и сопоставьте фразы с переводом."
        ),

        // 5) Чтение и письмо: соотнеси начало и конец фразы (READ, WRITE)
        TaskModel(
            taskType = listOf(TaskType.READ, TaskType.WRITE), //+++++
            taskBody = TaskBody.TextConnectTask(
                variant = listOf(
                    "Я живу" to "в центре города.",
                    "По выходным" to "мы встречаемся с друзьями.",
                    "Завтра" to "я пойду гулять."
                )
            ),
            question = "Сопоставьте части предложений, чтобы получить корректные фразы."
        ),

        // 6) Выбор по картинке (SELECT)
        TaskModel(
            taskType = listOf(TaskType.SELECT),
            taskBody = TaskBody.ImageAndSelect(
                ImageAndSelectModel(
                    imageBlocks = listOf(
                        ImageBlocks(
                            name = "Кошка",
                            image = "https://fastly.picsum.photos/id/40/4106/2806.jpg?hmac=MY3ra98ut044LaWPEKwZowgydHZ_rZZUuOHrc3mL5mI"
                        ),
                        ImageBlocks(
                            name = "Собака",
                            image = "https://picsum.photos/id/237/200/300" // я сдаюсь искать рабочие картинки
                        )
                    ),
                    variants = listOf(
                        "Кошка" to true,
                        "Собака" to false,
                        "Дом" to false
                    )
                )
            ),
            question = "Выберите подходящую подпись к изображению."
        )
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

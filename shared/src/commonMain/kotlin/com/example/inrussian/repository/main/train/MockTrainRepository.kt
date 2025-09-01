package com.example.inrussian.repository.main.train

/*

class MockTrainRepository(
    private val scope: CoroutineScope, private val userId: String = "u_1"
) : TrainRepository {
    @OptIn(ExperimentalUuidApi::class)
    private val tasks = listOf<FullTaskMode>(
        FullTaskMode(
            id = "efc6b249-7b7f-4be5-a8bd-960283668284",
            taskText = "Прослушайте диалог и ответьте на вопрос:\u2028На каком направлении может учиться спикер 2?",
            taskType = listOf(TaskType.WRITE),
            taskBody = TaskBody.TextTask(
                variant = listOf(
                    Pair("зонт1", "雨伞1"),
                    Pair("зонт2", "雨伞2"),
                    Pair("зонт3", "雨伞3"),
                    Pair("зонт4", "雨伞4"),
                )
            ),
        ),


        FullTaskMode(
            id = "a4049d03-1ed6-4ade-9de8-15391acca01e",
            taskType = listOf(TaskType.CONNECT_IMAGE),
            taskText = "Прослушайте диалог и ответьте на вопрос:\u2028На каком направлении может учиться спикер 2?",
            taskBody = TaskBody.ImageTask(
                variant = listOf(
                    Pair(
                        "зонт1",
                        "https://avatars.mds.yandex.net/i?id=02acd4f4c4fed4c3ea617be5d54b5d63921bed98-5147227-images-thumbs&n=13"
                    ),
                    Pair(
                        "зонт2",
                        "https://avatars.mds.yandex.net/i?id=02acd4f4c4fed4c3ea617be5d54b5d63921bed98-5147227-images-thumbs&n=13"
                    ),
                    Pair(
                        "зонт3",
                        "https://avatars.mds.yandex.net/i?id=02acd4f4c4fed4c3ea617be5d54b5d63921bed98-5147227-images-thumbs&n=13"
                    ),
                    Pair(
                        "зонт4",
                        "https://avatars.mds.yandex.net/i?id=02acd4f4c4fed4c3ea617be5d54b5d63921bed98-5147227-images-thumbs&n=13"
                    ),
                )
            ),
        ),
    )
    private val json = Json

    */
/* Courses *//*

    private val courses = listOf(
        ShortCourse("kc1", "KMP Practice"), ShortCourse("kc2", "Compose Practice")
    )

    */
/* Sections per course *//*

    private val sectionsState = MutableStateFlow(
        courses.associate { c -> c.id to generateSections(c.id) })

    */
/* Tasks per section *//*

    private val tasksPerSection = mutableMapOf<String, MutableStateFlow<List<Task>>>()

    */
/* Modular assets *//*

    private val contentsPerTask = mutableMapOf<String, MutableStateFlow<List<TaskContentItem>>>()
    private val optionsPerTask =
        mutableMapOf<String, MutableStateFlow<List<TaskAnswerOptionItem>>>()
    private val answerPerTask = mutableMapOf<String, MutableStateFlow<TaskAnswerItem?>>()

    */
/* Queue per section *//*

    private val queuePerSection = mutableMapOf<String, MutableStateFlow<List<UserTaskQueueItem>>>()

    */
/* Completed tasks *//*

    private val completedTasks = mutableMapOf<String, MutableSet<String>>()

    init {
        sectionsState.value.values.flatten().forEach { section ->
            val generated = generateTasksAndAssets(section)
            tasksPerSection[section.id] = MutableStateFlow(generated.tasks)
            generated.contents.forEach { (t, list) -> contentsPerTask[t] = MutableStateFlow(list) }
            generated.options.forEach { (t, list) -> optionsPerTask[t] = MutableStateFlow(list) }
            generated.answers.forEach { (t, ans) -> answerPerTask[t] = MutableStateFlow(ans) }
            queuePerSection[section.id] = MutableStateFlow(emptyList())
            completedTasks[section.id] = mutableSetOf()
            scope.launch { ensureQueuePopulated(section.id, 5) }
        }
    }


    override fun userCourses(): Flow<List<ShortCourse>> = flowOf(courses)

    override fun sectionsForCourse(courseId: String): Flow<List<SectionModel>> =
        sectionsState.map { it[courseId] ?: emptyList() }

    override fun section(sectionId: String): Flow<SectionModel?> =
        sectionsState.map { it.values.flatten().firstOrNull { s -> s.id == sectionId } }

    override fun tasksForSection(sectionId: String): Flow<List<Task>> =
        tasksPerSection[sectionId] ?: MutableStateFlow(emptyList())

    override fun contentItemsForTask(taskId: String): Flow<List<TaskContentItem>> =
        contentsPerTask[taskId] ?: MutableStateFlow(emptyList())

    override fun answerOptionsForTask(taskId: String): Flow<List<TaskAnswerOptionItem>> =
        optionsPerTask[taskId] ?: MutableStateFlow(emptyList())

    override fun answerForTask(taskId: String): Flow<TaskAnswerItem?> =
        answerPerTask[taskId] ?: MutableStateFlow(null)

    override fun userQueue(sectionId: String): Flow<List<UserTaskQueueItem>> =
        queuePerSection[sectionId] ?: MutableStateFlow(emptyList())

    override suspend fun ensureQueuePopulated(sectionId: String, minSize: Int) {
        val queueFlow = queuePerSection[sectionId] ?: return
        val tasksFlow = tasksPerSection[sectionId] ?: return
        val current = queueFlow.value
        if (current.size >= minSize) return

        val tasks = tasksFlow.value
        val completed = completedTasks[sectionId] ?: emptySet()

        val presentThemes = current.map { it.themeId }.toSet()
        val orderedThemes = tasks.map { it.themeId }.distinct()
        val neededThemes = orderedThemes.filterNot { it in presentThemes }

        val additions = mutableListOf<UserTaskQueueItem>()
        var pos = current.size
        for (theme in neededThemes) {
            val themeTasks = tasks.filter { it.themeId == theme && it.id !in completed }
            if (themeTasks.isEmpty()) continue
            themeTasks.forEach { task ->
                additions += UserTaskQueueItem(
                    userId = userId,
                    taskId = task.id,
                    themeId = theme,
                    sectionId = sectionId,
                    queuePosition = pos++,
                    isOriginalTask = true,
                    isRetryTask = false,
                    originalTaskId = null
                )
            }
            if (current.size + additions.size >= minSize) break
        }
        if (additions.isNotEmpty()) {
            queueFlow.value = (current + additions).reindex()
        }
    }

    override suspend fun consumeCurrentQueueTask(sectionId: String, correct: Boolean) {
        val queueFlow = queuePerSection[sectionId] ?: return
        val queue = queueFlow.value
        val head = queue.firstOrNull() ?: return
        recordTaskResult(sectionId, head.taskId, correct)
        queueFlow.value = queue.drop(1).reindex()
        ensureQueuePopulated(sectionId, 3)
    }

    override suspend fun selectOption(sectionId: String, option: TasksOption) {
        if (option == TasksOption.Continue) ensureQueuePopulated(sectionId, 3)
    }

    override suspend fun recordTaskResult(sectionId: String, taskId: String, correct: Boolean) {
        if (correct) {
            completedTasks[sectionId]?.add(taskId)
        } else {
            scheduleRetry(sectionId, taskId)
        }
        recomputeSection(sectionId)
    }

    override suspend fun refreshSection(sectionId: String) {
        recomputeSection(sectionId)
    }

    override suspend fun getTasksByCourseId(courseId: String) =
        tasks


    */
/* ---------------- Generation ---------------- *//*


    private fun generateSections(courseId: String): List<SectionModel> = (1..3).map { idx ->
        val themes = (1..Random.nextInt(2, 4)).map { tIdx ->
            ThemeMeta(
                id = "th_${courseId}_$idx$tIdx",
                order = tIdx,
                theoryCount = Random.nextInt(2, 4),
                practiceCount = Random.nextInt(2, 4)
            )
        }
        val totalTheory = themes.sumOf { it.theoryCount }
        val totalPractice = themes.sumOf { it.practiceCount }
        SectionModel(
            id = "sec_${courseId}_$idx",
            courseId = courseId,
            title = "Section $idx of ${courseId.uppercase()}",
            totalTheory = totalTheory,
            totalPractice = totalPractice,
            completedTheory = 0,
            completedPractice = 0,
            themes = themes
        )
    }

    private data class Generated(
        val tasks: List<Task>,
        val contents: Map<String, List<TaskContentItem>>,
        val options: Map<String, List<TaskAnswerOptionItem>>,
        val answers: Map<String, TaskAnswerItem?>
    )

    private fun generateTasksAndAssets(section: SectionModel): Generated {
        val tasks = mutableListOf<Task>()
        val contentsMap = mutableMapOf<String, List<TaskContentItem>>()
        val optionsMap = mutableMapOf<String, List<TaskAnswerOptionItem>>()
        val answersMap = mutableMapOf<String, TaskAnswerItem?>()

        section.themes.sortedBy { it.order }.forEach { theme ->
            repeat(theme.theoryCount) { i ->
                val type = listOf(TaskType.LISTEN_AND_CHOOSE, TaskType.READ_AND_CHOOSE).random()
                val id = "t_${theme.id}_TH_$i"
                val (contents, options, answer) = buildAssets(id, section.id, theme.id, type)
                tasks += Task(
                    id, section.id, theme.id, type, text = "Theory ${i + 1}", answer = answer
                )
                contentsMap[id] = contents
                optionsMap[id] = options
                answersMap[id] = answer
            }
            repeat(theme.practiceCount) { i ->
                val type = listOf(
                    TaskType.MATCH_AUDIO_TEXT,
                    TaskType.MATCH_TEXT_TEXT,
                    TaskType.WORD_ORDER,
                    TaskType.LOOK_AND_CHOOSE
                ).random()
                val id = "t_${theme.id}_PR_$i"
                val (contents, options, answer) = buildAssets(id, section.id, theme.id, type)
                tasks += Task(
                    id, section.id, theme.id, type, text = "Practice ${i + 1}", answer = answer
                )
                contentsMap[id] = contents
                optionsMap[id] = options
                answersMap[id] = answer
            }
        }

        return Generated(tasks, contentsMap, optionsMap, answersMap)
    }

    private data class AssetBundle(
        val contents: List<TaskContentItem>,
        val options: List<TaskAnswerOptionItem>,
        val answer: TaskAnswerItem?
    )

    private fun buildAssets(
        taskId: String, sectionId: String, themeId: String, type: TaskType
    ): AssetBundle {
        val contents = mutableListOf<TaskContentItem>()
        val options = mutableListOf<TaskAnswerOptionItem>()
        var answer: TaskAnswerItem? = null

        when (type) {
            TaskType.LISTEN_AND_CHOOSE -> {
                val audioCount = Random.nextInt(1, 3)
                repeat(audioCount) { idx ->
                    contents += TaskContentItem(
                        taskId = taskId,
                        contentType = ContentType.AUDIO,
                        contentId = "audio_${taskId}_$idx",
                        description = "Audio snippet ${idx + 1}",
                        transcription = "Transcription ${idx + 1}",
                        translation = "Translation ${idx + 1}",
                        orderNum = idx
                    )
                }
                val optCount = 4
                val correct = Random.nextInt(optCount)
                repeat(optCount) { i ->
                    options += TaskAnswerOptionItem(
                        taskId = taskId,
                        optionText = "Option ${i + 1}",
                        isCorrect = i == correct,
                        orderNum = i
                    )
                }
                answer = TaskAnswerItem(
                    taskId = taskId,
                    answerType = AnswerType.SINGLE_CHOICE_SHORT,
                    correctAnswer = JsonPrimitive(options.first { it.isCorrect }.id)
                )
            }

            TaskType.READ_AND_CHOOSE -> {
                contents += TaskContentItem(
                    taskId = taskId,
                    contentType = ContentType.TEXT,
                    description = "Read & choose the correct answer.",
                    orderNum = 0
                )
                val optCount = 4
                val correct = Random.nextInt(optCount)
                repeat(optCount) { i ->
                    options += TaskAnswerOptionItem(
                        taskId = taskId,
                        optionText = "Statement ${i + 1}",
                        isCorrect = i == correct,
                        orderNum = i
                    )
                }
                answer = TaskAnswerItem(
                    taskId = taskId,
                    answerType = AnswerType.SINGLE_CHOICE_SHORT,
                    correctAnswer = JsonPrimitive(options.first { it.isCorrect }.id)
                )
            }

            TaskType.LOOK_AND_CHOOSE -> {
                contents += TaskContentItem(
                    taskId = taskId,
                    contentType = ContentType.IMAGE,
                    contentId = "image_$taskId",
                    description = "Look & choose",
                    orderNum = 0
                )
                val optCount = 3
                val correct = Random.nextInt(optCount)
                repeat(optCount) { i ->
                    options += TaskAnswerOptionItem(
                        taskId = taskId,
                        optionText = "Label ${i + 1}",
                        isCorrect = i == correct,
                        orderNum = i
                    )
                }
                answer = TaskAnswerItem(
                    taskId = taskId,
                    answerType = AnswerType.SINGLE_CHOICE_SHORT,
                    correctAnswer = JsonPrimitive(options.first { it.isCorrect }.id)
                )
            }

            TaskType.MATCH_AUDIO_TEXT -> {
                val pairs = 4
                repeat(pairs) { i ->
                    contents += TaskContentItem(
                        taskId = taskId,
                        contentType = ContentType.AUDIO,
                        contentId = "aud_${taskId}_$i",
                        description = "Audio word ${i + 1}",
                        transcription = "Word ${i + 1}",
                        translation = "Перевод ${i + 1}",
                        orderNum = i
                    )
                }
                contents.sortedBy { it.orderNum }.forEachIndexed { idx, c ->
                    options += TaskAnswerOptionItem(
                        taskId = taskId,
                        optionText = c.transcription,
                        optionAudioId = c.contentId,
                        isCorrect = true,
                        orderNum = idx
                    )
                }
                val correctArray = buildJsonArray {
                    options.forEach { add(JsonPrimitive(it.id)) }
                }
                answer = TaskAnswerItem(
                    taskId = taskId,
                    answerType = AnswerType.MULTIPLE_CHOICE_SHORT,
                    correctAnswer = correctArray
                )
            }

            TaskType.MATCH_TEXT_TEXT -> {
                contents += TaskContentItem(
                    taskId = taskId,
                    contentType = ContentType.TEXT,
                    description = "Match the related terms",
                    orderNum = 0
                )
                val terms = 4
                repeat(terms) { i ->
                    options += TaskAnswerOptionItem(
                        taskId = taskId,
                        optionText = "Term ${i + 1}",
                        isCorrect = true,
                        orderNum = i
                    )
                }
                val correctArray = buildJsonArray {
                    options.forEach { add(JsonPrimitive(it.id)) }
                }
                answer = TaskAnswerItem(
                    taskId = taskId,
                    answerType = AnswerType.MULTIPLE_CHOICE_LONG,
                    correctAnswer = correctArray
                )
            }

            TaskType.WORD_ORDER -> {
                val words = listOf("Kotlin", "makes", "multiplatform", "fun")
                contents += TaskContentItem(
                    taskId = taskId,
                    contentType = ContentType.TEXT,
                    description = "Arrange words",
                    orderNum = 0
                )
                words.forEachIndexed { i, w ->
                    options += TaskAnswerOptionItem(
                        taskId = taskId, optionText = w, isCorrect = true, orderNum = i
                    )
                }
                val correctArray = buildJsonArray {
                    options.sortedBy { it.orderNum }.forEach { add(JsonPrimitive(it.id)) }
                }
                answer = TaskAnswerItem(
                    taskId = taskId,
                    answerType = AnswerType.WORD_ORDER,
                    correctAnswer = correctArray
                )
            }

            TaskType.WRITE -> TODO()
            TaskType.LISTEN -> TODO()
            TaskType.READ -> TODO()
            TaskType.SPEAK -> TODO()
            TaskType.REMIND -> TODO()
            TaskType.MARK -> TODO()
            TaskType.FILL -> TODO()
            TaskType.CONNECT_AUDIO -> TODO()
            TaskType.CONNECT_IMAGE -> TODO()
            TaskType.CONNECT_TRANSLATE -> TODO()
            TaskType.SELECT -> TODO()
        }

        return AssetBundle(
            contents = contents.sortedBy { it.orderNum },
            options = options.sortedBy { it.orderNum },
            answer = answer
        )
    }

    */
/* ---------------- Retry & Progress ---------------- *//*


    override suspend fun scheduleReinforcement(
        sectionId: String, taskId: String, offsetRange: IntRange
    ) {
        val queueFlow = queuePerSection[sectionId] ?: return
        val tasks = tasksPerSection[sectionId]?.value ?: return
        val task = tasks.firstOrNull { it.id == taskId } ?: return
        val currentQueue = queueFlow.value

        if (currentQueue.any { it.taskId == taskId }) return

        val offset = offsetRange.random()
        val insertionIndex = minOf(offset, currentQueue.size)

        val reinforcement = UserTaskQueueItem(
            userId = userId,
            taskId = task.id,
            themeId = task.themeId,
            sectionId = sectionId,
            queuePosition = insertionIndex,
            isOriginalTask = false,
            isRetryTask = true,
            originalTaskId = task.id
        )

        val newQueue = currentQueue.toMutableList()
        newQueue.add(insertionIndex, reinforcement)
        queueFlow.value = newQueue.reindex()
    }

    private fun scheduleRetry(sectionId: String, taskId: String) {
        val queueFlow = queuePerSection[sectionId] ?: return
        val queue = queueFlow.value
        val offset = (3..5).random()
        val insertionIndex = minOf(offset, queue.size)
        val task = tasksPerSection[sectionId]?.value?.firstOrNull { it.id == taskId } ?: return
        val newItem = UserTaskQueueItem(
            userId = userId,
            taskId = task.id,
            themeId = task.themeId,
            sectionId = sectionId,
            queuePosition = insertionIndex,
            isOriginalTask = false,
            isRetryTask = true,
            originalTaskId = task.id
        )
        val newQueue = queue.toMutableList()
        newQueue.add(insertionIndex, newItem)
        queueFlow.value = newQueue.reindex()
    }

    private fun recomputeSection(sectionId: String) {
        val allSections = sectionsState.value.values.flatten()
        val target = allSections.firstOrNull { it.id == sectionId } ?: return
        val tasks = tasksPerSection[sectionId]?.value ?: emptyList()
        val completed = completedTasks[sectionId] ?: emptySet()

        val theoryIds = tasks.filter { it.type in THEORY_TASK_TYPES }.map { it.id }
        val practiceIds = tasks.filter { it.type in PRACTICE_TASK_TYPES }.map { it.id }

        val completedTheory = theoryIds.count { it in completed }
        val completedPractice = practiceIds.count { it in completed }

        val updated = target.copy(
            completedTheory = completedTheory, completedPractice = completedPractice
        )

        sectionsState.value = sectionsState.value.mapValues { (_, list) ->
            list.map { if (it.id == sectionId) updated else it }
        }
    }

    */
/* ---------------- Utilities ---------------- *//*


    private fun List<UserTaskQueueItem>.reindex(): List<UserTaskQueueItem> =
        mapIndexed { idx, it -> it.copy(queuePosition = idx) }
}*/

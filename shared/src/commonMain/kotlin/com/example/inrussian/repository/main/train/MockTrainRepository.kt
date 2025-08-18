package com.example.inrussian.repository.main.train

import com.example.inrussian.components.main.train.Course
import com.example.inrussian.components.main.train.Section
import com.example.inrussian.components.main.train.Task
import com.example.inrussian.components.main.train.TaskKind
import com.example.inrussian.components.main.train.TasksOption
import com.example.inrussian.components.main.train.ThemeMeta
import com.example.inrussian.components.main.train.UserTaskQueueItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlin.random.Random

class MockTrainRepository(
    private val scope: CoroutineScope,
    private val userId: String = "u_1"
) : TrainRepository {

    private val courses = listOf(
        Course("kc1", "KMP Practice"),
        Course("kc2", "Compose Practice")
    )

    // Generate sections per course
    private val sectionsState = MutableStateFlow(
        courses.associate { c ->
            c.id to generateSections(c.id)
        }
    )

    // Tasks pool per section
    private val tasksPerSection: MutableMap<String, MutableStateFlow<List<Task>>> = mutableMapOf()

    // Queue per section
    private val queuePerSection: MutableMap<String, MutableStateFlow<List<UserTaskQueueItem>>> =
        mutableMapOf()

    private val completedTasks: MutableMap<String, MutableSet<String>> = mutableMapOf()

    init {
        sectionsState.value.values.flatten().forEach { section ->
            val tasks = generateTasks(section)
            tasksPerSection[section.id] = MutableStateFlow(tasks)
            queuePerSection[section.id] = MutableStateFlow(emptyList())
            completedTasks[section.id] = mutableSetOf()
            scope.launch { ensureQueuePopulated(section.id, 5) }
        }
    }

    override fun userCourses(): Flow<List<Course>> =
        flowOf(courses)

    override fun sectionsForCourse(courseId: String): Flow<List<Section>> =
        sectionsState.map { it[courseId] ?: emptyList() }

    override fun section(sectionId: String): Flow<Section?> =
        sectionsState.map { map -> map.values.flatten().firstOrNull { it.id == sectionId } }

    override fun tasksForSection(sectionId: String): Flow<List<Task>> =
        tasksPerSection[sectionId] ?: MutableStateFlow(emptyList())

    override fun userQueue(sectionId: String): Flow<List<UserTaskQueueItem>> =
        queuePerSection[sectionId] ?: MutableStateFlow(emptyList())

    override suspend fun recordTaskResult(sectionId: String, taskId: String, correct: Boolean) {
        if (correct) {
            completedTasks[sectionId]?.add(taskId)
        } else {
            scheduleRetry(sectionId, taskId)
        }
        recomputeSection(sectionId)
    }

    override suspend fun ensureQueuePopulated(sectionId: String, minSize: Int) {
        val queueFlow = queuePerSection[sectionId] ?: return
        val tasksFlow = tasksPerSection[sectionId] ?: return
        val currentQueue = queueFlow.value
        if (currentQueue.size >= minSize) return

        val tasks = tasksFlow.value
        val completed = completedTasks[sectionId] ?: emptySet()

        // Determine which themes are already represented
        val presentThemeIds = currentQueue.map { it.themeId }.toSet()
        val themesOrder = tasks.groupBy { it.themeId }.keys.sorted()

        val neededThemes = themesOrder.filterNot { it in presentThemeIds }

        val newQueueItems = mutableListOf<UserTaskQueueItem>()

        val basePosition = currentQueue.size
        var pos = basePosition
        for (themeId in neededThemes) {
            val themeTasks = tasks.filter { it.themeId == themeId && it.id !in completed }
            if (themeTasks.isEmpty()) continue
            themeTasks.forEach { task ->
                newQueueItems += UserTaskQueueItem(
                    userId = userId,
                    taskId = task.id,
                    themeId = themeId,
                    sectionId = sectionId,
                    queuePosition = pos++,
                    isOriginalTask = true,
                    isRetryTask = false,
                    originalTaskId = null
                )
            }
            if (queueFlow.value.size + newQueueItems.size >= minSize) break
        }

        if (newQueueItems.isNotEmpty()) {
            queueFlow.value = (currentQueue + newQueueItems).reindex()
        }
    }

    override suspend fun consumeCurrentQueueTask(sectionId: String, correct: Boolean) {
        val queueFlow = queuePerSection[sectionId] ?: return
        val queue = queueFlow.value
        if (queue.isEmpty()) return
        val head = queue.first()
        recordTaskResult(sectionId, head.taskId, correct)

        val remaining = queue.drop(1)
        queueFlow.value = remaining.reindex()

        if (!correct) {
            // Already scheduled inside recordTaskResult
        }

        ensureQueuePopulated(sectionId, 3)
    }

    override suspend fun selectOption(sectionId: String, option: TasksOption) {
        if (option == TasksOption.Continue) {
            ensureQueuePopulated(sectionId, 3)
        }
    }

    override suspend fun refreshSection(sectionId: String) {
        recomputeSection(sectionId)
    }

    /* Helpers */

    private fun generateSections(courseId: String): List<Section> =
        (1..3).map { idx ->
            val themes = (1..Random.nextInt(2, 5)).map { tIdx ->
                ThemeMeta(
                    id = "th_$courseId${idx}_$tIdx",
                    order = tIdx,
                    theoryCount = Random.nextInt(2, 5),
                    practiceCount = Random.nextInt(2, 5)
                )
            }
            val totalTheory = themes.sumOf { it.theoryCount }
            val totalPractice = themes.sumOf { it.practiceCount }
            Section(
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

    private fun generateTasks(section: Section): List<Task> {
        val tasks = mutableListOf<Task>()
        section.themes.sortedBy { it.order }.forEach { theme ->
            repeat(theme.theoryCount) { i ->
                tasks += Task(
                    id = "t_${theme.id}_TH_$i",
                    sectionId = section.id,
                    themeId = theme.id,
                    kind = TaskKind.THEORY,
                    text = "Теория ${i + 1} (${theme.id})"
                )
            }
            repeat(theme.practiceCount) { i ->
                tasks += Task(
                    id = "t_${theme.id}_PR_$i",
                    sectionId = section.id,
                    themeId = theme.id,
                    kind = TaskKind.PRACTICE,
                    text = "Практика ${i + 1} (${theme.id})"
                )
            }
        }
        return tasks
    }

    private fun scheduleRetry(sectionId: String, taskId: String) {
        val tasksFlow = tasksPerSection[sectionId] ?: return
        val queueFlow = queuePerSection[sectionId] ?: return
        val tasks = tasksFlow.value
        val queue = queueFlow.value
        val task = tasks.firstOrNull { it.id == taskId } ?: return
        val offset = 3
        val insertionIndex = minOf(offset, queue.size)
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

        val completedTheory = tasks.count { it.kind == TaskKind.THEORY && it.id in completed }
        val completedPractice = tasks.count { it.kind == TaskKind.PRACTICE && it.id in completed }

        val updated = target.copy(
            completedTheory = completedTheory,
            completedPractice = completedPractice
        )

        val newMap = sectionsState.value.mapValues { (_, list) ->
            list.map { if (it.id == sectionId) updated else it }
        }
        sectionsState.value = newMap
    }

    private fun List<UserTaskQueueItem>.reindex(): List<UserTaskQueueItem> =
        mapIndexed { idx, it -> it.copy(queuePosition = idx) }
}
package com.example.inrussian.repository.main.home


import com.example.inrussian.components.main.home.CourseModel
import com.example.inrussian.components.main.home.CourseSection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.random.Random
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

/*
class MockHomeRepository(
    private val scope: CoroutineScope
) : HomeRepository {

    @OptIn(ExperimentalTime::class)
    private val nowIso: String
        get() = Clock.System.now().toString()

    private val allCourses: List<CourseModel> = listOf(
        CourseModel(
            id = "c1",
            name = "KMP Быстрый старт",
            posterId = null,
            description = "Основы Kotlin Multiplatform.",
            authorId = "a1",
            authorUrl = null,
            language = "ru",
            isPublished = true,
            createdAt = nowIso,
            updatedAt = nowIso
        ),
        CourseModel(
            id = "c2",
            name = "Compose Multiplatform Глубже",
            posterId = null,
            description = "Продвинутые паттерны UI.",
            authorId = "a2",
            authorUrl = null,
            language = "ru",
            isPublished = true,
            createdAt = nowIso,
            updatedAt = nowIso
        ),
        CourseModel(
            id = "c3",
            name = "Coroutines Практика",
            posterId = null,
            description = "Асинхронность и structured concurrency.",
            authorId = "a1",
            authorUrl = null,
            language = "ru",
            isPublished = false,
            createdAt = nowIso,
            updatedAt = nowIso
        ),
        CourseModel(
            id = "c4",
            name = "Архитектура KMP",
            posterId = null,
            description = "Подходы к модульности и DI.",
            authorId = "a3",
            authorUrl = null,
            language = "en",
            isPublished = true,
            createdAt = nowIso,
            updatedAt = nowIso
        ),
        CourseModel(
            id = "c5",
            name = "Тестирование KMP",
            posterId = null,
            description = "Обзор стратегий тестирования.",
            authorId = "a2",
            authorUrl = null,
            language = "en",
            isPublished = true,
            createdAt = nowIso,
            updatedAt = nowIso
        )
    )

    private val recommendedFlow = MutableStateFlow(allCourses)

    private val enrolledIdsFlow = MutableStateFlow<Set<String>>(setOf("c1"))

    private val sectionsFlows: MutableMap<String, MutableStateFlow<List<CourseSection>>> =
        mutableMapOf()

    init {
        allCourses.forEach { course ->
            sectionsFlows[course.id] = MutableStateFlow(generateSections(course.id))
        }
        simulateProgress()
    }

    val recommendedCourses: Flow<List<CourseModel>> = recommendedFlow

    val enrolledCourseIds: Flow<Set<String>> = enrolledIdsFlow

    override suspend fun enroll(courseId: String) {
        enrolledIdsFlow.value =
            if (courseId in enrolledIdsFlow.value) enrolledIdsFlow.value
            else enrolledIdsFlow.value + courseId
    }

    override suspend fun unenroll(courseId: String) {
        enrolledIdsFlow.value =
            if (courseId in enrolledIdsFlow.value) enrolledIdsFlow.value - courseId
            else enrolledIdsFlow.value
    }

    override suspend fun courseById(courseId: String): Flow<CourseModel?> =
        recommendedFlow.map { list -> list.firstOrNull { it.id == courseId } }

    override suspend fun courseSections(courseId: String): Flow<List<CourseSection>> =
        sectionsFlows[courseId] ?: MutableStateFlow(emptyList())

    override suspend fun courseProgressPercent(courseId: String): Flow<Int> =
        courseSections(courseId).map { sections ->
            val totalLessons = sections.sumOf { it.totalLessons }
            val completed = sections.sumOf { it.completedLessons }
            if (totalLessons == 0) 0 else (completed * 100 / totalLessons)
        }

    private fun generateSections(courseId: String): List<CourseSection> {
        val number = Random.nextInt(3, 6)
        return (1..number).map { index ->
            val total = Random.nextInt(4, 10)
            val completed = Random.nextInt(0, total + 1)
            CourseSection(
                id = "$courseId-s$index",
                title = "Секция $index",
                totalLessons = total,
                completedLessons = completed
            )
        }
    }

    private fun simulateProgress() {
        scope.launch(Dispatchers.Default) {
        }
    }
}*/

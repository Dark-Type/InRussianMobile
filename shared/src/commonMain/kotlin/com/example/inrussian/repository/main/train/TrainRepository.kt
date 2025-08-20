package com.example.inrussian.repository.main.train

import com.example.inrussian.components.main.train.Section
import com.example.inrussian.components.main.train.Task
import com.example.inrussian.components.main.train.TaskAnswerItem
import com.example.inrussian.components.main.train.TaskAnswerOptionItem
import com.example.inrussian.components.main.train.TaskContentItem
import com.example.inrussian.components.main.train.TasksOption
import com.example.inrussian.components.main.train.UserTaskQueueItem
import kotlinx.coroutines.flow.Flow

interface TrainRepository {
    fun userCourses(): Flow<List<com.example.inrussian.components.main.train.Course>>
    fun sectionsForCourse(courseId: String): Flow<List<Section>>
    fun section(sectionId: String): Flow<Section?>
    fun tasksForSection(sectionId: String): Flow<List<Task>>

    fun contentItemsForTask(taskId: String): Flow<List<TaskContentItem>>
    fun answerOptionsForTask(taskId: String): Flow<List<TaskAnswerOptionItem>>
    fun answerForTask(taskId: String): Flow<TaskAnswerItem?>

    fun userQueue(sectionId: String): Flow<List<UserTaskQueueItem>>

    suspend fun ensureQueuePopulated(sectionId: String, minSize: Int)
    suspend fun consumeCurrentQueueTask(sectionId: String, correct: Boolean)

    suspend fun scheduleReinforcement(sectionId: String, taskId: String, offsetRange: IntRange = 3..5)

    suspend fun selectOption(sectionId: String, option: TasksOption)
    suspend fun recordTaskResult(sectionId: String, taskId: String, correct: Boolean)
    suspend fun refreshSection(sectionId: String)
}
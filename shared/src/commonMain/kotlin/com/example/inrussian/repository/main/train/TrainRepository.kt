package com.example.inrussian.repository.main.train

import com.example.inrussian.components.main.train.SectionModel
import com.example.inrussian.components.main.train.ShortCourse
import com.example.inrussian.components.main.train.Task
import com.example.inrussian.components.main.train.TaskAnswerItem
import com.example.inrussian.components.main.train.TaskAnswerOptionItem
import com.example.inrussian.components.main.train.TaskContentItem
import com.example.inrussian.components.main.train.TasksOption
import com.example.inrussian.components.main.train.UserTaskQueueItem
import com.example.inrussian.models.models.FullTaskMode
import com.example.inrussian.models.models.TaskModel
import kotlinx.coroutines.flow.Flow

interface TrainRepository {
    suspend fun userCourses(): List<ShortCourse>
    suspend fun sectionsForCourse(courseId: String): List<SectionModel>
    suspend fun section(sectionId: String): SectionModel?
    suspend fun tasksForSection(sectionId: String):  List<TaskModel>

    suspend fun contentItemsForTask(taskId: String): List<TaskContentItem>
    suspend fun answerOptionsForTask(taskId: String): List<TaskAnswerOptionItem>
    suspend fun answerForTask(taskId: String): TaskAnswerItem?

    suspend fun userQueue(sectionId: String): List<UserTaskQueueItem>

    suspend fun ensureQueuePopulated(sectionId: String, minSize: Int)
    suspend fun consumeCurrentQueueTask(sectionId: String, correct: Boolean)

    suspend fun scheduleReinforcement(
        sectionId: String,
        taskId: String,
        offsetRange: IntRange = 3..5
    )

    suspend fun selectOption(sectionId: String, option: TasksOption)
    suspend fun recordTaskResult(sectionId: String, taskId: String, correct: Boolean)
    suspend fun refreshSection(sectionId: String)

    suspend fun getTasksByCourseId(courseId: String): List<FullTaskMode>

}
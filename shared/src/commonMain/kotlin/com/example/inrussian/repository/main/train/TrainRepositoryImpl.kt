package com.example.inrussian.repository.main.train

import com.example.inrussian.components.main.train.SectionModel
import com.example.inrussian.components.main.train.ShortCourse
import com.example.inrussian.components.main.train.Task
import com.example.inrussian.components.main.train.TaskAnswerItem
import com.example.inrussian.components.main.train.TaskAnswerOptionItem
import com.example.inrussian.components.main.train.TaskContentItem
import com.example.inrussian.components.main.train.TasksOption
import com.example.inrussian.components.main.train.UserTaskQueueItem
import com.example.inrussian.data.client.apis.DefaultApi
import com.example.inrussian.models.models.FullTaskMode
import com.example.inrussian.utils.errorHandle
import kotlinx.coroutines.flow.Flow
import org.openapitools.client.models.Section

class TrainRepositoryImpl(private val api: DefaultApi) : TrainRepository {
    override suspend fun userCourses(): List<ShortCourse> {
        /*api.contentCoursesGet().errorHandle().map {
            api.contentCoursesCourseIdGet()
            api.studentSectionsSectionIdProgressGet()
        }*/
        TODO("Not yet implemented")
    }

    override suspend fun sectionsForCourse(courseId: String): List<SectionModel> {
        api.contentSectionsByCourseCourseIdGet(courseId).errorHandle().map { it }
        TODO("Not yet implemented")
    }

    override suspend fun section(sectionId: String):SectionModel? {
       api
        TODO("Not yet implemented")
    }

    override suspend fun tasksForSection(sectionId: String): List<Task> {
        TODO("Not yet implemented")
    }

    override suspend fun contentItemsForTask(taskId: String): List<TaskContentItem> {
        TODO("Not yet implemented")
    }

    override suspend fun answerOptionsForTask(taskId: String): List<TaskAnswerOptionItem> {
        TODO("Not yet implemented")
    }

    override suspend fun answerForTask(taskId: String):TaskAnswerItem? {
        TODO("Not yet implemented")
    }

    override suspend fun userQueue(sectionId: String): List<UserTaskQueueItem> {
        TODO("Not yet implemented")
    }

    override suspend fun ensureQueuePopulated(sectionId: String, minSize: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun consumeCurrentQueueTask(
        sectionId: String,
        correct: Boolean
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun scheduleReinforcement(
        sectionId: String,
        taskId: String,
        offsetRange: IntRange
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun selectOption(
        sectionId: String,
        option: TasksOption
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun recordTaskResult(
        sectionId: String,
        taskId: String,
        correct: Boolean
    ) {

    }

    override suspend fun refreshSection(sectionId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun getTasksByCourseId(courseId: String): List<FullTaskMode> {
        TODO("Not yet implemented")
    }
}


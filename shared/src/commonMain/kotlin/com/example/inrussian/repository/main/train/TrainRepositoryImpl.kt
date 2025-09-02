package com.example.inrussian.repository.main.train

import co.touchlab.kermit.Logger
import com.example.inrussian.components.main.train.SectionModel
import com.example.inrussian.components.main.train.ShortCourse
import com.example.inrussian.components.main.train.TaskType
import com.example.inrussian.components.main.train.ThemeMeta
import com.example.inrussian.data.client.apis.DefaultApi
import com.example.inrussian.models.models.FullTaskModel
import com.example.inrussian.models.models.TaskBody
import com.example.inrussian.models.models.TaskModel
import com.example.inrussian.models.models.TaskResponse
import com.example.inrussian.utils.errorHandle
import kotlin.random.Random

class TrainRepositoryImpl(private val api: DefaultApi) : TrainRepository {
    private val tasks = listOf<TaskModel>(
        TaskModel(
            taskType = listOf(TaskType.READ, TaskType.LISTEN_AND_CHOOSE),
            taskBody = TaskBody.TextTask(
                variant = listOf("BudetMir, goyda " to "Гойда")
            ),
            question = "live or die, that is question"
        )
    )

    override suspend fun userCourses(): List<ShortCourse> {
        return api.contentCoursesGet().errorHandle().map {
            ShortCourse(it.id, it.name)
        }
    }

    override suspend fun sectionsForCourse(courseId: String): List<SectionModel> {

        return api.contentSectionsByCourseCourseIdGet(courseId).errorHandle().map {

            Logger.i { it.id }
            SectionModel(
                it.id,
                courseId,
                title = it.name,
                totalTheory = 0,
                totalPractice = api.contentStatsSectionSectionIdTasksCountGet(it.id).errorHandle()
                    .count.toInt(),
                completedTheory = 0,
                completedPractice = 0,
                themes = api.contentThemesBySectionSectionIdGet(it.id).errorHandle().map {
                    ThemeMeta(
                        it.id, it.orderNum, 0, api
                            .contentStatsThemeThemeIdTasksCountGet(it.id)
                            .errorHandle().count.toInt()
                    )
                }
            )
        }
    }

    override suspend fun section(sectionId: String): SectionModel? {
        val section = api.contentSectionsSectionIdGet(sectionId).errorHandle()
        return SectionModel(
            section.id,
            section.courseId,
            title = section.name,
            totalTheory = 0,
            totalPractice = api.contentStatsSectionSectionIdTasksCountGet(section.id).errorHandle()
                .count.toInt(),
            completedTheory = 0,
            completedPractice = 0,
            themes = api.contentThemesBySectionSectionIdGet(section.id).errorHandle().map {
                ThemeMeta(
                    it.id, it.orderNum, 0, api
                        .contentStatsThemeThemeIdTasksCountGet(it.id).errorHandle().count.toInt()
                )
            }
        )
    }

    override suspend fun getNextTask(): TaskResponse {
        val task = tasks.shuffled().first()
        return TaskResponse(
            Random(1).nextFloat() % 1,
            FullTaskModel(
                id = task.id,
                taskText = task.question.toString(),
                taskType = task.taskType,
                taskBody = task.taskBody
            )
        )
    }

    override suspend fun sendResultAndGetNextTask() {
        TODO("Not yet implemented")
    }


}


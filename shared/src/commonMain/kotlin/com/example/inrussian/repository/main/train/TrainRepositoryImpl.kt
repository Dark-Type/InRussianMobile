package com.example.inrussian.repository.main.train

//import co.touchlab.kermit.Logger
//import com.example.inrussian.components.main.train.SectionModel
//import com.example.inrussian.components.main.train.ShortCourse
//import com.example.inrussian.components.main.train.TaskType
//import com.example.inrussian.components.main.train.ThemeMeta
//import com.example.inrussian.data.client.apis.DefaultApi
//import com.example.inrussian.models.models.task.FullTaskModel
//import com.example.inrussian.models.models.task.body.GapWithVariantModel
//import com.example.inrussian.models.models.task.TaskBody
//import com.example.inrussian.models.models.task.TaskModel
//import com.example.inrussian.models.models.task.TaskResponse
//import com.example.inrussian.models.models.task.body.ListenAndSelectModel
//import com.example.inrussian.models.models.task.body.TextInputWithVariantModel
//import com.example.inrussian.utils.errorHandle
//import kotlin.math.abs
//import kotlin.random.Random
//
//class TrainRepositoryImpl(private val api: DefaultApi) : TrainRepository {
//
//
//    override suspend fun userCourses(): List<ShortCourse> {
//        return api.contentCoursesGet().errorHandle().map {
//            ShortCourse(it.id, it.name)
//        }
//    }
//
//    override suspend fun sectionsForCourse(courseId: String): List<SectionModel> {
//
//        return api.contentSectionsByCourseCourseIdGet(courseId).errorHandle().map {
//
//            Logger.i { it.id }
//            SectionModel(
//                it.id,
//                courseId,
//                title = it.name,
//                totalTheory = 0,
//                totalPractice = api.contentStatsSectionSectionIdTasksCountGet(it.id).errorHandle()
//                    .count.toInt(),
//                completedTheory = 0,
//                completedPractice = 0,
//                themes = api.contentThemesBySectionSectionIdGet(it.id).errorHandle().map {
//                    ThemeMeta(
//                        it.id, it.orderNum, 0, api
//                            .contentStatsThemeThemeIdTasksCountGet(it.id)
//                            .errorHandle().count.toInt()
//                    )
//                }
//            )
//        }
//    }
//
//    override suspend fun section(sectionId: String): SectionModel? {
//        val section = api.contentSectionsSectionIdGet(sectionId).errorHandle()
//        return SectionModel(
//            section.id,
//            section.courseId,
//            title = section.name,
//            totalTheory = 0,
//            totalPractice = api.contentStatsSectionSectionIdTasksCountGet(section.id).errorHandle()
//                .count.toInt(),
//            completedTheory = 0,
//            completedPractice = 0,
//            themes = api.contentThemesBySectionSectionIdGet(section.id).errorHandle().map {
//                ThemeMeta(
//                    it.id, it.orderNum, 0, api
//                        .contentStatsThemeThemeIdTasksCountGet(it.id).errorHandle().count.toInt()
//                )
//            }
//        )
//    }
//
//    override suspend fun getNextTask(sectionId: String): TaskResponse {
//        val task = tasks.shuffled().first()
//        return TaskResponse(
//            abs(Random(1231).nextInt()) % 100 / 100f,
//            FullTaskModel(
//                id = task.id,
//                taskText = task.question.toString(),
//                taskType = task.taskType,
//                taskBody = task.taskBody
//            )
//        )
//    }
//
//    override suspend fun sendResultAndGetNextTask() {
//
//    }
//
//
//}


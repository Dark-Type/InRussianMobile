package com.example.inrussian.repository.main.home

import com.example.inrussian.components.main.home.CourseModel
import com.example.inrussian.components.main.home.CourseSection
import com.example.inrussian.data.client.apis.DefaultApi
import com.example.inrussian.platformInterfaces.UserConfigurationStorage
import com.example.inrussian.repository.auth.AuthRepository
import com.example.inrussian.utils.errorHandle
import org.openapitools.client.models.Course
import org.openapitools.client.models.SectionProgressItem

class HomeRepositoryImpl(
    private val api: DefaultApi,
    private val userConfigurationStorage: UserConfigurationStorage,
    private val authRepository: AuthRepository,
) : HomeRepository {
    override suspend fun enroll(courseId: String) {
        api.studentCoursesCourseIdEnrollPost(courseId).errorHandle(userConfigurationStorage ,authRepository)
    }

    override suspend fun unenroll(courseId: String) {
        api.studentCoursesCourseIdEnrollDelete(courseId).errorHandle(userConfigurationStorage ,authRepository)
    }

    override suspend fun getMyCourses(): List<CourseModel> =
        api.studentEnrollmentsGet().errorHandle(userConfigurationStorage ,authRepository)
            .map { api.contentCoursesCourseIdGet(it.courseId).errorHandle(userConfigurationStorage ,authRepository).toDomain() }


    override suspend fun getRecommendedCourses(): List<CourseModel> =
        api.studentCoursesGet().errorHandle(userConfigurationStorage ,authRepository).map { it.toDomain() }


    override suspend fun courseById(courseId: String): CourseModel? =
        api.contentCoursesCourseIdGet(courseId).errorHandle(userConfigurationStorage ,authRepository).toDomain()

    override suspend fun courseSections(courseId: String): List<CourseSection> =
        api.contentSectionsByCourseCourseIdGet(courseId)
            .errorHandle(userConfigurationStorage ,authRepository)
            .map {
                api.studentSectionsSectionIdProgressGet(it.courseId).errorHandle(userConfigurationStorage ,authRepository)
                    .toCourseSection(it.name)
            }


    override suspend fun courseProgressPercent(courseId: String): Int {
        val sections = courseSections(courseId)
        val totalLessons = sections.sumOf { it.totalLessons }
        val completed = sections.sumOf { it.completedLessons }
        return if (totalLessons == 0) 0 else (completed * 100 / totalLessons)
    }
}

fun SectionProgressItem.toCourseSection(title: String = ""): CourseSection {
    return CourseSection(
        id = sectionId,
        title = title,
        totalLessons = totalTasks,
        completedLessons = completedTasks
    )
}

fun Course.toDomain(): CourseModel {
    return CourseModel(
        id = this.id,
        name = this.name,
        posterId = this.posterId,
        description = this.description,
        authorId = this.authorId,
        authorUrl = this.authorUrl,
        language = this.language,
        isPublished = this.isPublished,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}
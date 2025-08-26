package com.example.inrussian.repository.auth

import com.example.inrussian.data.client.apis.DefaultApi

class CourseRepository(private val api: DefaultApi) {
    suspend fun getCourses() = api.contentCoursesGet().body()
    suspend fun getCoursesById(courseId: String) = api.contentCoursesCourseIdGet(courseId).body()
    suspend fun subscribeOnCourse(courseId: String) =
        api.studentCoursesCourseIdEnrollPost(courseId).body()

    suspend fun unsubscribeOnCourse(courseId: String) =
        api.studentCoursesCourseIdEnrollDelete(courseId).body()

}
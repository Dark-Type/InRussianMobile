package com.example.inrussian.repository.main.home

import com.example.inrussian.components.main.home.CourseModel
import com.example.inrussian.components.main.home.CourseSection

interface HomeRepository {
    suspend fun enroll(courseId: String)
    suspend fun unenroll(courseId: String)

    suspend fun courseById(courseId: String): CourseModel?
    suspend fun courseSections(courseId: String): List<CourseSection>
    suspend fun courseProgressPercent(courseId: String): Int
}
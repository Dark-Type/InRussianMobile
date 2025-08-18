package com.example.inrussian.repository.main.home

import com.example.inrussian.components.main.home.Course
import com.example.inrussian.components.main.home.CourseSection
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    val recommendedCourses: Flow<List<Course>>
    val enrolledCourseIds: Flow<Set<String>>

    fun enroll(courseId: String)
    fun unenroll(courseId: String)

    fun courseById(courseId: String): Flow<Course?>
    fun courseSections(courseId: String): Flow<List<CourseSection>>
    fun courseProgressPercent(courseId: String): Flow<Int>
}
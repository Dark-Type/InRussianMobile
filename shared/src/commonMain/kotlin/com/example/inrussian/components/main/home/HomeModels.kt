package com.example.inrussian.components.main.home

import kotlinx.serialization.Serializable

@Serializable
data class CourseModel(
    val id: String = "",
    val name: String = "",
    val posterId: String? = null,
    val description: String? = null,
    val authorId: String = "",
    val authorUrl: String? = null,
    val language: String = "",
    val isPublished: Boolean = false,
    val createdAt: String = "",
    val updatedAt: String = ""
)


@Serializable
data class CourseSection(
    val id: String,
    val title: String,
    val totalLessons: Int,
    val completedLessons: Int
)

data class CoursesListState(
    val recommended: List<CourseModel>,
    val enrolled: List<CourseModel>,
    val isLoading: Boolean = false
)


data class CourseDetailsState(
    val course: CourseModel?,
    val isEnrolled: Boolean,
    val sections: List<CourseSection>,
    val progressPercent: Int,
    val isLoading: Boolean
)
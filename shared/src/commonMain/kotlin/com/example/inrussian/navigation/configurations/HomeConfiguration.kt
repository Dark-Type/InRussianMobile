package com.example.inrussian.navigation.configurations
import kotlinx.serialization.Serializable
@Serializable
sealed class HomeConfiguration {
    @Serializable
    data object Courses : HomeConfiguration()

    @Serializable
    data class CourseDetails(val courseId: String) : HomeConfiguration()
}
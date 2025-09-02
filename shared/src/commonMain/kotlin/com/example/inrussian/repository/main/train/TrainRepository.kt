package com.example.inrussian.repository.main.train

import com.example.inrussian.components.main.train.SectionModel
import com.example.inrussian.components.main.train.ShortCourse
import com.example.inrussian.models.models.FullTaskModel
import com.example.inrussian.models.models.TaskModel
import com.example.inrussian.models.models.TaskResponse

interface TrainRepository {
    suspend fun userCourses(): List<ShortCourse>
    suspend fun sectionsForCourse(courseId: String): List<SectionModel>
    suspend fun section(sectionId: String): SectionModel?
    suspend fun getNextTask(): TaskResponse
    suspend fun sendResultAndGetNextTask()

}
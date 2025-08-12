package com.example.inrussian.navigation.configurations

import kotlinx.serialization.Serializable

@Serializable
sealed class TrainConfiguration {
    @Serializable
    data object Courses : TrainConfiguration()

    @Serializable
    data class SectionDetail(val sectionId: String) : TrainConfiguration()
}
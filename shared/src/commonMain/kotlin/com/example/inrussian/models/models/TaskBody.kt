package com.example.inrussian.models.models

import kotlinx.serialization.Serializable

@Serializable
sealed interface TaskBody {
    @Serializable
    data class TextTask(
        val variant: List<Pair<String, String>>
    ) : TaskBody

    @Serializable
    data class AudioTask(val variant: List<Pair<String, String>>) : TaskBody

    @Serializable
    data class TextInputTask(val task: TextInputModel) : TaskBody

    @Serializable
    data class TextInputWithVariantTask(val task: TextInputWithVariantModel) : TaskBody

    @Serializable
    data class ImageTask(val variant: List<Pair<String, String>>) : TaskBody

    @Serializable
    data class ListenAndSelect(val task: ListenAndSelectModel) : TaskBody


}

@Serializable
data class TextInputModel(
    val text: String,
    val gaps: List<Gap>
)



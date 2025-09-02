package com.example.inrussian.models.models

import kotlinx.serialization.Serializable

@Serializable
sealed interface TaskBody {
    @Serializable
    data class TextConnectTask(
        val variant: List<Pair<String, String>>
    ) : TaskBody

    @Serializable
    data class AudioConnectTask(val variant: List<Pair<String, String>>) : TaskBody

    @Serializable
    data class TextInputTask(val task: List<TextInputModel>) : TaskBody

    @Serializable
    data class TextInputWithVariantTask(val task: TextInputWithVariantModel) : TaskBody

    @Serializable
    data class ImageConnectTask(val variant: List<Pair<String, String>>) : TaskBody

    @Serializable
    data class ListenAndSelect(val task: ListenAndSelectModel) : TaskBody

    @Serializable
    data class ImageAndSelect(val task: ImageAndSelectModel) : TaskBody

    @Serializable
    data class ConstructSentenceTask(val task: ConstructSentenceModel) : TaskBody

    @Serializable
    data class SelectWordsTask(val task: SelectWordsModel) : TaskBody
}

@Serializable
data class TextInputModel(
    val label: String,
    val text: String,
    val gaps: List<Gap>
)



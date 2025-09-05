package com.example.inrussian.models.models.task

import com.example.inrussian.models.models.task.SelectWordsModel
import com.example.inrussian.models.models.task.body.TextInputWithVariantModel
import com.example.inrussian.models.models.task.body.ConstructSentenceModel
import com.example.inrussian.models.models.task.body.ImageAndSelectModel
import com.example.inrussian.models.models.task.body.ListenAndSelectModel
import com.example.inrussian.models.models.task.body.TextInputModel
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
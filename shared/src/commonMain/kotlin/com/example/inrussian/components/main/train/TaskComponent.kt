package com.example.inrussian.components.main.train

import co.touchlab.kermit.Logger
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.value.Value
import com.example.inrussian.components.main.train.TrainComponentCopy.Child
import com.example.inrussian.components.main.train.TrainComponentCopy.Child.ImageConnectChild
import com.example.inrussian.components.main.train.TrainComponentCopy.Child.TextConnectChild
import com.example.inrussian.components.main.train.TrainComponentCopy.Config
import com.example.inrussian.components.main.train.TrainComponentCopy.Config.AudioConnectConfig
import com.example.inrussian.components.main.train.TrainComponentCopy.Config.ImageConnectConfig
import com.example.inrussian.components.main.train.TrainComponentCopy.Config.TextConnectConfig
import com.example.inrussian.components.main.train.TrainComponentCopy.Config.TextInputConfig
import com.example.inrussian.components.main.train.TrainComponentCopy.Config.TextInputWithVariantConfig
import com.example.inrussian.components.main.train.tasks.ImageConnectTaskComponent
import com.example.inrussian.components.main.train.tasks.ImageConnectTaskComponentImpl
import com.example.inrussian.components.main.train.tasks.TextConnectTaskComponent
import com.example.inrussian.components.main.train.tasks.TextConnectTaskComponentImpl
import com.example.inrussian.models.models.TaskBody.AudioTask
import com.example.inrussian.models.models.TaskBody.ImageTask
import com.example.inrussian.models.models.TaskBody.TextInputTask
import com.example.inrussian.models.models.TaskBody.TextInputWithVariantTask
import com.example.inrussian.models.models.TaskBody.TextTask
import com.example.inrussian.stores.main.train.TrainStore
import com.example.inrussian.utils.asValue
import com.example.inrussian.utils.componentCoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


interface TrainComponentCopy {
    val childSlot: Value<ChildSlot<Config, Child>>
    fun onConfirmClick()
    fun onBack()

    sealed interface Child {
        data class TextConnectChild(val component: TextConnectTaskComponent) : Child
        data class ImageConnectChild(val component: ImageConnectTaskComponent) : Child
        data object EmptyChild : Child
    }

    @Serializable
    sealed interface Config {
        @Serializable
        @SerialName("TextConnect")
        data class TextConnectConfig(val tasks: TextTask) : Config

        @Serializable
        @SerialName("ImageConnect")
        data class ImageConnectConfig(val tasks: ImageTask) : Config

        @Serializable
        @SerialName("AudioConnect")
        data class AudioConnectConfig(val tasks: AudioTask) : Config

        @Serializable
        @SerialName("TextInput")
        data class TextInputConfig(val tasks: TextInputTask) : Config

        @Serializable
        @SerialName("TextInputWithVariant")
        data class TextInputWithVariantConfig(val tasks: TextInputWithVariantTask) : Config

        @Serializable
        @SerialName("Empty")
        object EmptyTaskConfig : Config
    }
}

class TrainComponentImpl(
    componentContext: ComponentContext,
    private val onOutput: (SectionDetailOutput) -> Unit,
    private val store: TrainStore,
    private val courseId: String,
) : TrainComponentCopy, ComponentContext by componentContext {
    val scope = componentCoroutineScope()
    val state = store.asValue()


    private val navigation = SlotNavigation<Config>()

    override val childSlot = childSlot(
        source = navigation,
        serializer = Config.serializer(),
        key = NAVIGATION_SLOT_KEY,
        handleBackButton = true,
        childFactory = ::createChild
    )

    init {
        scope.launch {
            state.subscribe {
                Logger.i { it.showedTask?.let { it1 -> "activete: ${it1.taskBody::class}" }.toString() }
                navigation.activate(
                    configuration = when (it.showedTask?.taskBody) {
                        is AudioTask -> AudioConnectConfig(it.showedTask.taskBody)
                        is ImageTask -> ImageConnectConfig(it.showedTask.taskBody)
                        is TextInputTask -> TextInputConfig(it.showedTask.taskBody)
                        is TextInputWithVariantTask -> TextInputWithVariantConfig(it.showedTask.taskBody)
                        is TextTask -> TextConnectConfig(it.showedTask.taskBody)
                        null -> Config.EmptyTaskConfig
                    }
                )
            }
        }
    }

    override fun onConfirmClick() {

    }

    private fun createChild(
        config: Config,
        context: ComponentContext
    ): Child {
        Logger.i { "createChild: $config" }
        return when (config) {

            is TextConnectConfig -> {
                Logger.i { "createChild: $config" }
                TextConnectChild(
                    component = TextConnectTaskComponentImpl(
                        context,
                        ::onNextClick,
                        config.tasks
                    )
                )
            }

            is ImageConnectConfig -> {
                Logger.i { "createChild: $config" }
                ImageConnectChild(
                    component = ImageConnectTaskComponentImpl(
                        context,
                        ::onNextClick,
                        config.tasks
                    )
                )
            }

            Config.EmptyTaskConfig -> Child.EmptyChild
            is AudioConnectConfig -> TODO()
            is TextInputConfig -> TODO()
            is TextInputWithVariantConfig -> TODO()

        }
    }


    override fun onBack() {

    }


    fun onNextClick(isPassed: Boolean) {
        store.accept(TrainStore.Intent.ContinueClick(isPassed))
    }

    companion object {
        private const val NAVIGATION_SLOT_KEY = "train_navigation"
    }
}

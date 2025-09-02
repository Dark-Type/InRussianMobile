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
import com.example.inrussian.components.main.train.tasks.impl.AudioConnectTaskComponentImpl
import com.example.inrussian.components.main.train.tasks.impl.ImageConnectTaskComponentImpl
import com.example.inrussian.components.main.train.tasks.impl.TextConnectTaskComponentImpl
import com.example.inrussian.components.main.train.tasks.impl.TextInputTaskComponentImpl
import com.example.inrussian.components.main.train.tasks.impl.TextInputTaskWithVariantComponentImpl
import com.example.inrussian.components.main.train.tasks.interfaces.AudioConnectTaskComponent
import com.example.inrussian.components.main.train.tasks.interfaces.ImageConnectTaskComponent
import com.example.inrussian.components.main.train.tasks.interfaces.TextConnectTaskComponent
import com.example.inrussian.components.main.train.tasks.interfaces.TextInputTaskComponent
import com.example.inrussian.components.main.train.tasks.interfaces.TextInputTaskWithVariantComponent
import com.example.inrussian.models.models.TaskBody
import com.example.inrussian.models.models.TaskBody.AudioConnectTask
import com.example.inrussian.models.models.TaskBody.ImageConnectTask
import com.example.inrussian.models.models.TaskBody.TextConnectTask
import com.example.inrussian.models.models.TaskBody.TextInputTask
import com.example.inrussian.models.models.TaskBody.TextInputWithVariantTask
import com.example.inrussian.stores.main.train.TrainStore
import com.example.inrussian.utils.asValue
import com.example.inrussian.utils.componentCoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


interface TrainComponentCopy {
    val childSlot: Value<ChildSlot<Config, Child>>

    val state: Value<TrainStore.State>
    fun onConfirmClick()
    fun onBack()

    sealed interface Child {
        data class TextConnectChild(val component: TextConnectTaskComponent) : Child
        data class AudioConnectChild(val component: AudioConnectTaskComponent) : Child
        data class ImageConnectChild(val component: ImageConnectTaskComponent) : Child
        data class TextInputChild(val component: TextInputTaskComponent) : Child
        data class TextInputWithVariantsChild(val component: TextInputTaskWithVariantComponent) : Child

        data object EmptyChild : Child
    }

    @Serializable
    sealed interface Config {
        @Serializable
        @SerialName("TextConnect")
        data class TextConnectConfig(val tasks: TextConnectTask) : Config

        @Serializable
        @SerialName("ImageConnect")
        data class ImageConnectConfig(val tasks: ImageConnectTask) : Config

        @Serializable
        @SerialName("AudioConnect")
        data class AudioConnectConfig(val tasks: AudioConnectTask) : Config

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
    override val state = store.asValue()


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
            Logger.i { "TrainComponentImpl courseId: $courseId" }
            state.subscribe {
                Logger.i {
                    it.showedTask?.let { it1 -> "activete: ${it1.taskBody::class}" }.toString()

                }
                Logger.i { "percent: ${state.value.percent}" }
                navigation.activate(
                    configuration = when (it.showedTask?.taskBody) {
                        is AudioConnectTask -> AudioConnectConfig(it.showedTask.taskBody)
                        is ImageConnectTask -> ImageConnectConfig(it.showedTask.taskBody)
                        is TextInputTask -> TextInputConfig(it.showedTask.taskBody)
                        is TextInputWithVariantTask -> TextInputWithVariantConfig(it.showedTask.taskBody)
                        is TextConnectTask -> TextConnectConfig(it.showedTask.taskBody)
                        null -> Config.EmptyTaskConfig
                        is TaskBody.ListenAndSelect -> TODO()
                        is TaskBody.ConstructSentenceTask -> TODO()
                        is TaskBody.ImageAndSelect -> TODO()
                        is TaskBody.SelectWordsTask -> TODO()
                    }
                )
            }
        }
    }

    override fun onConfirmClick() {

    }

    private fun createChild(
        config: Config, context: ComponentContext
    ): Child {
        return when (config) {
            is TextConnectConfig -> {
                TextConnectChild(
                    component = TextConnectTaskComponentImpl(
                        context,
                        onContinueClicked = ::onNextClick,
                        inChecking = ::inCheck,
                        onButtonEnable = ::onChangeButtonState,
                        config.tasks
                    )
                )
            }

            is ImageConnectConfig -> {
                ImageConnectChild(
                    component = ImageConnectTaskComponentImpl(
                        context,
                        onContinueClicked = ::onNextClick,
                        inChecking = ::inCheck,
                        onButtonEnable = ::onChangeButtonState,
                        config.tasks
                    )
                )
            }

            Config.EmptyTaskConfig -> Child.EmptyChild
            is AudioConnectConfig -> Child.AudioConnectChild(
                component = AudioConnectTaskComponentImpl(
                    context,
                    onContinueClicked = ::onNextClick,
                    inChecking = ::inCheck,
                    onButtonEnable = ::onChangeButtonState,
                    config.tasks
                )
            )

            is TextInputConfig -> Child.TextInputChild(
                component =
                    TextInputTaskComponentImpl(
                        context,
                        onContinueClicked = ::onNextClick,
                        inChecking = ::inCheck,
                        onButtonEnable = ::onChangeButtonState,
                        config.tasks
                    )
            )

            is TextInputWithVariantConfig -> Child.TextInputWithVariantsChild(
                component = TextInputTaskWithVariantComponentImpl(
                    context,
                    onContinueClicked = ::onNextClick,
                    inChecking = ::inCheck,
                    onButtonEnable = ::onChangeButtonState,
                    config.tasks
                )
            )

        }
    }


    override fun onBack() {

    }

    fun inCheck(inCheck: Boolean) {
        store.accept(TrainStore.Intent.InCheckStateChange(inCheck))
    }

    fun onNextClick(isPassed: Boolean) {
        store.accept(TrainStore.Intent.ContinueClick(isPassed))
    }

    fun onChangeButtonState(isEnable: Boolean) {
        store.accept(TrainStore.Intent.OnButtonStateChange(isEnable))

    }

    companion object {
        private const val NAVIGATION_SLOT_KEY = "train_navigation"
    }
}

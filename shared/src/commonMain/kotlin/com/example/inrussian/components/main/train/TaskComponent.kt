package com.example.inrussian.components.main.train

//import co.touchlab.kermit.Logger
//import com.arkivanov.decompose.ComponentContext
//import com.arkivanov.decompose.router.slot.ChildSlot
//import com.arkivanov.decompose.router.slot.SlotNavigation
//import com.arkivanov.decompose.router.slot.activate
//import com.arkivanov.decompose.router.slot.childSlot
//import com.arkivanov.decompose.value.Value
//import com.example.inrussian.components.main.train.TrainComponentCopy.Child
//import com.example.inrussian.components.main.train.TrainComponentCopy.Child.AudioConnectChild
//import com.example.inrussian.components.main.train.TrainComponentCopy.Child.ImageConnectChild
//import com.example.inrussian.components.main.train.TrainComponentCopy.Child.TextConnectChild
//import com.example.inrussian.components.main.train.TrainComponentCopy.Child.TextInputChild
//import com.example.inrussian.components.main.train.TrainComponentCopy.Child.TextInputWithVariantsChild
//import com.example.inrussian.components.main.train.TrainComponentCopy.Config
//import com.example.inrussian.components.main.train.TrainComponentCopy.Config.AudioConnectConfig
//import com.example.inrussian.components.main.train.TrainComponentCopy.Config.ImageConnectConfig
//import com.example.inrussian.components.main.train.TrainComponentCopy.Config.TextConnectConfig
//import com.example.inrussian.components.main.train.TrainComponentCopy.Config.TextInputConfig
//import com.example.inrussian.components.main.train.TrainComponentCopy.Config.TextInputWithVariantConfig
//import com.example.inrussian.components.main.train.tasks.impl.AudioConnectTaskComponentImpl
//import com.example.inrussian.components.main.train.tasks.impl.ImageConnectTaskComponentImpl
//import com.example.inrussian.components.main.train.tasks.impl.ListenAndSelectComponentImpl
//import com.example.inrussian.components.main.train.tasks.impl.TextConnectTaskComponentImpl
//import com.example.inrussian.components.main.train.tasks.impl.TextInputTaskComponentImpl
//import com.example.inrussian.components.main.train.tasks.impl.TextInputTaskWithVariantComponentImpl
//import com.example.inrussian.components.main.train.tasks.interfaces.AudioConnectTaskComponent
//import com.example.inrussian.components.main.train.tasks.interfaces.ImageConnectTaskComponent
//import com.example.inrussian.components.main.train.tasks.interfaces.ListenAndSelectComponent
//import com.example.inrussian.components.main.train.tasks.interfaces.TextConnectTaskComponent
//import com.example.inrussian.components.main.train.tasks.interfaces.TextInputTaskComponent
//import com.example.inrussian.components.main.train.tasks.interfaces.TextInputTaskWithVariantComponent
//import com.example.inrussian.models.models.task.TaskBody
//import com.example.inrussian.models.models.task.TaskBody.AudioConnectTask
//import com.example.inrussian.models.models.task.TaskBody.ImageConnectTask
//import com.example.inrussian.models.models.task.TaskBody.ListenAndSelect
//import com.example.inrussian.models.models.task.TaskBody.TextConnectTask
//import com.example.inrussian.models.models.task.TaskBody.TextInputTask
//import com.example.inrussian.models.models.task.TaskBody.TextInputWithVariantTask
//import com.example.inrussian.stores.main.train.TrainStore
//import com.example.inrussian.utils.asValue
//import com.example.inrussian.utils.componentCoroutineScope
//import kotlinx.coroutines.launch
//import kotlinx.serialization.SerialName
//import kotlinx.serialization.Serializable
//interface TrainComponentCopy {
//
//    val childSlot: Value<ChildSlot<Config, Child>>
//    val state: Value<TrainStore.State>
//
//    fun onConfirmClick()
//    fun onBack()
//
//    sealed interface Child {
//        data class TextConnectChild(val component: TextConnectTaskComponent) : Child
//        data class AudioConnectChild(val component: AudioConnectTaskComponent) : Child
//        data class ImageConnectChild(val component: ImageConnectTaskComponent) : Child
//        data class TextInputChild(val component: TextInputTaskComponent) : Child
//        data class TextInputWithVariantsChild(val component: TextInputTaskWithVariantComponent) : Child
//        data class ListenAndSelectChild(val component: ListenAndSelectComponent) : Child
//        data object EmptyChild : Child
//    }
//
//    @Serializable
//    sealed interface Config {
//
//        @Serializable
//        @SerialName("TextConnect")
//        data class TextConnectConfig(val task: ApiTextConnectTask) : Config
//
//        @Serializable
//        @SerialName("ImageConnect")
//        data class ImageConnectConfig(val task: ApiImageConnectTask) : Config
//
//        @Serializable
//        @SerialName("AudioConnect")
//        data class AudioConnectConfig(val task: ApiAudioConnectTask) : Config
//
//        @Serializable
//        @SerialName("TextInput")
//        data class TextInputConfig(val task: ApiTextInputTask) : Config
//
//        @Serializable
//        @SerialName("TextInputWithVariant")
//        data class TextInputWithVariantConfig(val task: ApiTextInputWithVariantTask) : Config
//
//        @Serializable
//        @SerialName("ListenSelect")
//        data class ListenSelectConfig(val task: ApiListenAndSelectTask) : Config
//
//        @Serializable
//        @SerialName("Empty")
//        data object EmptyTaskConfig : Config
//    }
//}
//
///**
// * Output channel for navigation or completion.
// */
//sealed interface TaskHostOutput {
//    data object NavigateBack : TaskHostOutput
//    data object ThemeCompleted : TaskHostOutput
//}
//
//class TrainComponentImpl(
//    componentContext: ComponentContext,
//    private val themeId: String,
//    private val store: TrainStore,
//    private val onOutput: (TaskHostOutput) -> Unit
//) : TrainComponentCopy, ComponentContext by componentContext {
//
//    private val scope = componentCoroutineScope()
//    override val state: Value<TrainStore.State> = store.asValue()
//
//    private val navigation = SlotNavigation<TrainComponentCopy.Config>()
//
//    override val childSlot: Value<ChildSlot<TrainComponentCopy.Config, TrainComponentCopy.Child>> =
//        childSlot(
//            source = navigation,
//            serializer = TrainComponentCopy.Config.serializer(),
//            key = NAVIGATION_SLOT_KEY,
//            handleBackButton = true,
//            childFactory = ::createChild
//        )
//
//    init {
//        // React to task changes
//        scope.launch {
//            state.subscribe {
//                val taskBody = it.showedTask?.body
//                Logger.i { "TrainComponentImpl(theme=$themeId) taskBody=${taskBody?.javaClass?.simpleName} percent=${it.percent}" }
//
//                // Completion detection (percent==1) — emit ThemeCompleted once
//                if ((it.percent ?: 0f) >= 1f) {
//                    onOutput(TaskHostOutput.ThemeCompleted)
//                    return@subscribe
//                }
//
//                navigation.activate(
//                    configuration = when (taskBody) {
//                        is ApiAudioConnectTask -> TrainComponentCopy.Config.AudioConnectConfig(taskBody)
//                        is ApiImageConnectTask -> TrainComponentCopy.Config.ImageConnectConfig(taskBody)
//                        is ApiTextInputTask -> TrainComponentCopy.Config.TextInputConfig(taskBody)
//                        is ApiTextInputWithVariantTask -> TrainComponentCopy.Config.TextInputWithVariantConfig(taskBody)
//                        is ApiTextConnectTask -> TrainComponentCopy.Config.TextConnectConfig(taskBody)
//                        is ApiListenAndSelectTask -> TrainComponentCopy.Config.ListenSelectConfig(taskBody)
//                        is ConstructSentenceTask,
//                        is ImageAndSelect,
//                        is SelectWordsTask,
//                        null -> TrainComponentCopy.Config.EmptyTaskConfig
//                    }
//                )
//            }
//        }
//    }
//
//    override fun onConfirmClick() {
//        // For now treat confirm as "Check & Pass" if button is enabled
//        if (state.value.isButtonEnable) {
//            store.accept(TrainStore.Intent.InCheckStateChange(true))
//            store.accept(TrainStore.Intent.ButtonClick(isPass = true))
//        }
//    }
//
//    override fun onBack() {
//        onOutput(TaskHostOutput.NavigateBack)
//    }
//
//    /* ---------------- Internal child factory ---------------- */
//
//    private fun createChild(
//        config: TrainComponentCopy.Config,
//        context: ComponentContext
//    ): TrainComponentCopy.Child =
//        when (config) {
//            is TrainComponentCopy.Config.TextConnectConfig ->
//                TrainComponentCopy.Child.TextConnectChild(
//                    TextConnectTaskComponentImpl(
//                        context,
//                        onContinueClicked = ::onButtonClick,
//                        inChecking = ::inCheck,
//                        onButtonEnable = ::onChangeButtonState,
//                        task = config.task
//                    )
//                )
//
//            is TrainComponentCopy.Config.ImageConnectConfig ->
//                TrainComponentCopy.Child.ImageConnectChild(
//                    ImageConnectTaskComponentImpl(
//                        context,
//                        onContinueClicked = ::onButtonClick,
//                        inChecking = ::inCheck,
//                        onButtonEnable = ::onChangeButtonState,
//                        task = config.task
//                    )
//                )
//
//            is TrainComponentCopy.Config.AudioConnectConfig ->
//                TrainComponentCopy.Child.AudioConnectChild(
//                    AudioConnectTaskComponentImpl(
//                        context,
//                        onContinueClicked = ::onButtonClick,
//                        inChecking = ::inCheck,
//                        onButtonEnable = ::onChangeButtonState,
//                        task = config.task
//                    )
//                )
//
//            is TrainComponentCopy.Config.TextInputConfig ->
//                TrainComponentCopy.Child.TextInputChild(
//                    TextInputTaskComponentImpl(
//                        context,
//                        onContinueClicked = ::onButtonClick,
//                        inChecking = ::inCheck,
//                        onButtonEnable = ::onChangeButtonState,
//                        task = config.task
//                    )
//                )
//
//            is TrainComponentCopy.Config.TextInputWithVariantConfig ->
//                TrainComponentCopy.Child.TextInputWithVariantsChild(
//                    TextInputTaskWithVariantComponentImpl(
//                        context,
//                        onContinueClicked = ::onButtonClick,
//                        inChecking = ::inCheck,
//                        onButtonEnable = ::onChangeButtonState,
//                        task = config.task
//                    )
//                )
//
//            is TrainComponentCopy.Config.ListenSelectConfig ->
//                TrainComponentCopy.Child.ListenAndSelectChild(
//                    ListenAndSelectComponentImpl(
//                        context,
//                        onContinueClicked = ::onButtonClick,
//                        onButtonEnable = ::onChangeButtonState,
//                        task = config.task
//                    )
//                )
//
//            TrainComponentCopy.Config.EmptyTaskConfig ->
//                TrainComponentCopy.Child.EmptyChild
//        }
//
//    /* ---------------- Store helpers ---------------- */
//
//    private fun inCheck(inCheck: Boolean) {
//        store.accept(TrainStore.Intent.InCheckStateChange(inCheck))
//    }
//
//    private fun onButtonClick(isPassed: Boolean) {
//        store.accept(TrainStore.Intent.ButtonClick(isPassed))
//    }
//
//    private fun onChangeButtonState(isEnable: Boolean) {
//        store.accept(TrainStore.Intent.OnButtonStateChange(isEnable))
//    }
//
//    companion object {
//        private const val NAVIGATION_SLOT_KEY = "theme_task_slot"
//    }
//}
//
///* =================================================================================================
// * CHILD TASK COMPONENT PLACEHOLDERS
// * (Assume these are implemented elsewhere; interfaces declared for compilation)
// * ================================================================================================= */
//
//interface TextConnectTaskComponent
//interface AudioConnectTaskComponent
//interface ImageConnectTaskComponent
//interface TextInputTaskComponent
//interface TextInputTaskWithVariantComponent
//interface ListenAndSelectComponent
//
//class TextConnectTaskComponentImpl(
//    context: ComponentContext,
//    private val onContinueClicked: (Boolean) -> Unit,
//    private val inChecking: (Boolean) -> Unit,
//    private val onButtonEnable: (Boolean) -> Unit,
//    val task: ApiTextConnectTask
//) : TextConnectTaskComponent
//
//class AudioConnectTaskComponentImpl(
//    context: ComponentContext,
//    private val onContinueClicked: (Boolean) -> Unit,
//    private val inChecking: (Boolean) -> Unit,
//    private val onButtonEnable: (Boolean) -> Unit,
//    val task: ApiAudioConnectTask
//) : AudioConnectTaskComponent
//
//class ImageConnectTaskComponentImpl(
//    context: ComponentContext,
//    private val onContinueClicked: (Boolean) -> Unit,
//    private val inChecking: (Boolean) -> Unit,
//    private val onButtonEnable: (Boolean) -> Unit,
//    val task: ApiImageConnectTask
//) : ImageConnectTaskComponent
//
//class TextInputTaskComponentImpl(
//    context: ComponentContext,
//    private val onContinueClicked: (Boolean) -> Unit,
//    private val inChecking: (Boolean) -> Unit,
//    private val onButtonEnable: (Boolean) -> Unit,
//    val task: ApiTextInputTask
//) : TextInputTaskComponent
//
//class TextInputTaskWithVariantComponentImpl(
//    context: ComponentContext,
//    private val onContinueClicked: (Boolean) -> Unit,
//    private val inChecking: (Boolean) -> Unit,
//    private val onButtonEnable: (Boolean) -> Unit,
//    val task: ApiTextInputWithVariantTask
//) : TextInputTaskWithVariantComponent
//
//class ListenAndSelectComponentImpl(
//    context: ComponentContext,
//    private val onContinueClicked: (Boolean) -> Unit,
//    private val onButtonEnable: (Boolean) -> Unit,
//    val task: ApiListenAndSelectTask
//) : ListenAndSelectComponent
//
///* =================================================================================================
// * EXTENSION / UTIL (Minimal stubs)
// * ================================================================================================= */
//
//object Logger {
//    inline fun i(lazy: () -> String) {
//        println(lazy())
//    }
//}
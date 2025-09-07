package com.example.inrussian.components.main.train

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.example.inrussian.components.main.train.tasks.impl.AudioConnectTaskComponentImpl
import com.example.inrussian.components.main.train.tasks.impl.ImageConnectTaskComponentImpl
import com.example.inrussian.components.main.train.tasks.impl.ListenAndSelectComponentImpl
import com.example.inrussian.components.main.train.tasks.impl.TextConnectTaskComponentImpl
import com.example.inrussian.components.main.train.tasks.impl.TextInputTaskComponentImpl
import com.example.inrussian.components.main.train.tasks.impl.TextInputTaskWithVariantComponentImpl
import com.example.inrussian.components.main.train.tasks.interfaces.AudioConnectTaskComponent
import com.example.inrussian.components.main.train.tasks.interfaces.ImageConnectTaskComponent
import com.example.inrussian.components.main.train.tasks.interfaces.ListenAndSelectComponent
import com.example.inrussian.components.main.train.tasks.interfaces.TextConnectTaskComponent
import com.example.inrussian.components.main.train.tasks.interfaces.TextInputTaskComponent
import com.example.inrussian.components.main.train.tasks.interfaces.TextInputTaskWithVariantComponent
import com.example.inrussian.repository.main.train.TaskBody
import com.example.inrussian.repository.main.train.ThemeTreeNode
import com.example.inrussian.repository.main.train.TrainRepository
import com.example.inrussian.stores.main.train.TrainStore
import com.example.inrussian.stores.main.train.TrainStoreFactory
import com.example.inrussian.utils.asValue
import com.example.inrussian.utils.componentCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.math.roundToInt

sealed interface TrainOutput {
    data object NavigateBack : TrainOutput
}

interface TrainComponent {
    val childStack: Value<ChildStack<Config, Child>>
    fun onThemeSelected(courseId: String, themePath: List<String>)
    fun onBack()

    sealed interface Child {
        data class CoursesChild(val component: TrainCoursesListComponent) : Child
        data class ThemeTasksChild(val component: ThemeTasksComponent) : Child
    }

    @Serializable
    sealed interface Config {
        @Serializable
        data object Courses : Config

        @Serializable
        data class ThemeTasks(val courseId: String, val themeId: String) : Config
    }
}

class DefaultTrainComponent(
    componentContext: ComponentContext,
    private val repository: TrainRepository,
    private val storeFactory: TrainStoreFactory,
    private val onOutput: (TrainOutput) -> Unit
) : TrainComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<TrainComponent.Config>()

    override val childStack: Value<ChildStack<TrainComponent.Config, TrainComponent.Child>> =
        childStack(
            source = navigation,
            serializer = TrainComponent.Config.serializer(),
            initialConfiguration = TrainComponent.Config.Courses,
            handleBackButton = true,
            childFactory = ::createChild
        )

    override fun onThemeSelected(courseId: String, themePath: List<String>) {
        val themeId = themePath.lastOrNull() ?: return
        navigation.pushNew(TrainComponent.Config.ThemeTasks(courseId, themeId))
    }

    override fun onBack() {
        if (childStack.value.backStack.isNotEmpty()) navigation.pop()
        else onOutput(TrainOutput.NavigateBack)
    }

    private fun createChild(
        config: TrainComponent.Config,
        ctx: ComponentContext
    ): TrainComponent.Child =
        when (config) {
            TrainComponent.Config.Courses ->
                TrainComponent.Child.CoursesChild(
                    DefaultTrainCoursesListComponent(
                        componentContext = ctx,
                        repository = repository,
                        onNavigateTheme = ::onThemeSelected
                    )
                )

            is TrainComponent.Config.ThemeTasks ->
                TrainComponent.Child.ThemeTasksChild(
                    DefaultThemeTasksComponent(
                        componentContext = ctx,
                        themeId = config.themeId,
                        store = storeFactory.create(config.themeId),
                        onFinished = ::onBack,
                        back = ::onBack
                    )
                )
        }
}

/* ================================================================================================
 * COURSES LIST
 * ================================================================================================
 */

data class TrainCoursesState(
    val isLoading: Boolean = false,
    val courses: List<CourseUiModel> = emptyList(),
    val error: String? = null
)

data class CourseUiModel(
    val id: String,
    val title: String,
    val percent: Int,
    val solvedTasks: Int,
    val totalTasks: Int,
    val themes: List<ThemeModel>
)

data class ThemeModel(
    val id: String,
    val title: String,
    val description: String? = null,
    val childThemes: List<ThemeModel> = emptyList(),
    val solvedTasks: Int,
    val totalTasks: Int,
    val isLeaf: Boolean
) {
    val completionFraction: Float
        get() = if (totalTasks == 0) 0f else (solvedTasks.toFloat() / totalTasks).coerceIn(0f, 1f)
}

interface TrainCoursesListComponent {
    val state: Value<TrainCoursesState>
    fun onRefresh()
    fun onThemeClick(courseId: String, themePath: List<String>)
    fun onResumeCourse(courseId: String)
}

class DefaultTrainCoursesListComponent(
    componentContext: ComponentContext,
    private val repository: TrainRepository,
    private val onNavigateTheme: (courseId: String, themePath: List<String>) -> Unit
) : TrainCoursesListComponent, ComponentContext by componentContext {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    private val _state = MutableValue(TrainCoursesState(isLoading = true))
    override val state: Value<TrainCoursesState> = _state

    init {
        load(
            initial = true,
            force = false
        )
    }

    override fun onRefresh() {
        if (_state.value.isLoading) return
        load(initial = false, force = true)
    }

    override fun onThemeClick(courseId: String, themePath: List<String>) {
        onNavigateTheme(courseId, themePath)
    }

    override fun onResumeCourse(courseId: String) {
        val course = _state.value.courses.firstOrNull { it.id == courseId } ?: return
        val leaf = course.themes.firstNotNullOfOrNull { findIncompleteLeaf(it) } ?: return
        onNavigateTheme(courseId, listOf(leaf.id))
    }

    private fun findIncompleteLeaf(t: ThemeModel): ThemeModel? {
        if (t.isLeaf && t.solvedTasks < t.totalTasks) return t
        t.childThemes.forEach { c -> findIncompleteLeaf(c)?.let { return it } }
        return null
    }

    private fun load(initial: Boolean, force: Boolean) {
        scope.launch {
            if (initial) _state.value = _state.value.copy(isLoading = true, error = null)
            try {
                if (force) repository.refresh()

                val enrollments = repository.userCourseEnrollments()
                val courseUi = enrollments.map { enrollment ->
                    val tree = repository.themeTree(enrollment.courseId)
                    val themes = tree.map { toThemeModel(it) }
                    val leaves = themes.flatMap { collectLeaves(it) }
                    val total = leaves.sumOf { it.totalTasks }.coerceAtLeast(1)
                    val solved = leaves.sumOf { it.solvedTasks }
                    val percent = (solved.toFloat() / total * 100f).roundToInt()
                    CourseUiModel(
                        id = enrollment.courseId,
                        title = "Course ${enrollment.courseId}",
                        percent = percent,
                        solvedTasks = solved,
                        totalTasks = total,
                        themes = themes
                    )
                }

                _state.value = _state.value.copy(isLoading = false, courses = courseUi)
            } catch (t: Throwable) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = t.message ?: "Load error"
                )
            }
        }
    }

    private suspend fun toThemeModel(node: ThemeTreeNode): ThemeModel {
        if (node.children.isEmpty()) {
            val contents = repository.themeContents(node.theme.id)
            return ThemeModel(
                id = node.theme.id,
                title = node.theme.name,
                description = node.theme.description,
                childThemes = emptyList(),
                solvedTasks = 0,
                totalTasks = contents.tasks.size,
                isLeaf = true
            )
        }
        val children = node.children.map { toThemeModel(it) }
        return ThemeModel(
            id = node.theme.id,
            title = node.theme.name,
            description = node.theme.description,
            childThemes = children,
            solvedTasks = children.sumOf { it.solvedTasks },
            totalTasks = children.sumOf { it.totalTasks },
            isLeaf = false
        )
    }

    private fun collectLeaves(t: ThemeModel): List<ThemeModel> =
        if (t.isLeaf) listOf(t) else t.childThemes.flatMap { collectLeaves(it) }

    fun dispose() = scope.cancel()
}

/* ================================================================================================
 * THEME TASKS COMPONENT (Wraps TrainStore + adds helper intents)
 * ================================================================================================
 */

interface ThemeTasksComponent {
    val themeId: String
    val state: Value<TrainStore.State>
    val childSlot: Value<ChildSlot<TaskBodyConfig, TaskBodyChild>>

    fun markCorrectAndSubmit()
    fun markIncorrectAttempt()
    fun continueAfterCorrect()
    fun toggleButton(enabled: Boolean)
    fun onBack()
}

/* ---------------- Slot Child Definitions ---------------- */

sealed interface TaskBodyChild {
    data class TextConnect(val component: TextConnectTaskComponent) : TaskBodyChild
    data class AudioConnect(val component: AudioConnectTaskComponent) : TaskBodyChild
    data class ImageConnect(val component: ImageConnectTaskComponent) : TaskBodyChild
    data class TextInput(val component: TextInputTaskComponent) : TaskBodyChild
    data class TextInputWithVariant(val component: TextInputTaskWithVariantComponent) :
        TaskBodyChild

    data class ListenAndSelect(val component: ListenAndSelectComponent) : TaskBodyChild
    data object Empty : TaskBodyChild
}

@Serializable
sealed interface TaskBodyConfig {
    @Serializable
    @SerialName("TextConnect")
    data class TextConnectCfg(val task: TaskBody.TextConnectTask) : TaskBodyConfig
    @Serializable
    @SerialName("ImageConnect")
    data class ImageConnectCfg(val task: TaskBody.ImageConnectTask) : TaskBodyConfig
    @Serializable
    @SerialName("AudioConnect")
    data class AudioConnectCfg(val task: TaskBody.AudioConnectTask) : TaskBodyConfig
    @Serializable
    @SerialName("TextInput")
    data class TextInputCfg(val task: TaskBody.TextInputTask) : TaskBodyConfig
    @Serializable
    @SerialName("TextInputVariant")
    data class TextInputVariantCfg(val task: TaskBody.TextInputWithVariantTask) : TaskBodyConfig
    @Serializable
    @SerialName("ListenSelect")
    data class ListenSelectCfg(val task: TaskBody.ListenAndSelect) : TaskBodyConfig
    @Serializable
    @SerialName("Empty")
    data object EmptyCfg : TaskBodyConfig
}

class DefaultThemeTasksComponent(
    componentContext: ComponentContext,
    override val themeId: String,
    private val store: TrainStore,
    private val onFinished: () -> Unit,
    private val back: () -> Unit
) : ThemeTasksComponent, ComponentContext by componentContext {

    override val state: Value<TrainStore.State> = store.asValue()
    private val scope = componentCoroutineScope()

    private val navigation = SlotNavigation<TaskBodyConfig>()
    override val childSlot: Value<ChildSlot<TaskBodyConfig, TaskBodyChild>> =
        childSlot(
            source = navigation,
            serializer = TaskBodyConfig.serializer(),
            key = "task_body_slot",
            handleBackButton = false,
            childFactory = ::createChild
        )

    init {
        scope.launch {
            state.subscribe { st ->
                val body = st.showedTask?.body
                val cfg = when (body) {
                    is TaskBody.TextConnectTask -> TaskBodyConfig.TextConnectCfg(body)
                    is TaskBody.ImageConnectTask -> TaskBodyConfig.ImageConnectCfg(body)
                    is TaskBody.AudioConnectTask -> TaskBodyConfig.AudioConnectCfg(body)
                    is TaskBody.TextInputTask -> TaskBodyConfig.TextInputCfg(body)
                    is TaskBody.TextInputWithVariantTask -> TaskBodyConfig.TextInputVariantCfg(body)
                    is TaskBody.ListenAndSelect -> TaskBodyConfig.ListenSelectCfg(body)
                    is TaskBody.ConstructSentenceTask,
                    is TaskBody.ImageAndSelect,
                    is TaskBody.SelectWordsTask,
                    null -> TaskBodyConfig.EmptyCfg
                }
                navigation.activate(cfg)

                if ((st.percent ?: 0f) >= 1f) {
                    onFinished()
                }
            }
        }
    }

    /* ---------- UI Intent Helpers ---------- */

    override fun markCorrectAndSubmit() {
        store.accept(TrainStore.Intent.InCheckStateChange(true))
        store.accept(TrainStore.Intent.ButtonClick(isPass = true))
    }

    override fun markIncorrectAttempt() {
        store.accept(TrainStore.Intent.InCheckStateChange(false))
        store.accept(TrainStore.Intent.ButtonClick(isPass = false))
    }

    override fun continueAfterCorrect() {
        store.accept(TrainStore.Intent.ButtonClick(isPass = true))
    }

    override fun toggleButton(enabled: Boolean) {
        store.accept(TrainStore.Intent.OnButtonStateChange(enabled))
    }

    override fun onBack() = back()

    /* ---------- Child Factory ---------- */
    private fun createChild(config: TaskBodyConfig, ctx: ComponentContext): TaskBodyChild =
        when (config) {
            is TaskBodyConfig.TextConnectCfg ->
                TaskBodyChild.TextConnect(
                    TextConnectTaskComponentImpl(
                        ctx,
                        ::onAction,
                        ::onCheckChange,
                        ::toggleButton,
                        config.task
                    )
                )

            is TaskBodyConfig.ImageConnectCfg ->
                TaskBodyChild.ImageConnect(
                    ImageConnectTaskComponentImpl(
                        ctx,
                        ::onAction,
                        ::onCheckChange,
                        ::toggleButton,
                        config.task
                    )
                )

            is TaskBodyConfig.AudioConnectCfg ->
                TaskBodyChild.AudioConnect(
                    AudioConnectTaskComponentImpl(
                        ctx,
                        ::onAction,
                        ::onCheckChange,
                        ::toggleButton,
                        config.task
                    )
                )

            is TaskBodyConfig.TextInputCfg ->
                TaskBodyChild.TextInput(
                    TextInputTaskComponentImpl(
                        ctx,
                        ::onAction,
                        ::onCheckChange,
                        ::toggleButton,
                        config.task
                    )
                )

            is TaskBodyConfig.TextInputVariantCfg ->
                TaskBodyChild.TextInputWithVariant(
                    TextInputTaskWithVariantComponentImpl(
                        ctx,
                        ::onAction,
                        ::onCheckChange,
                        ::toggleButton,
                        config.task
                    )
                )

            is TaskBodyConfig.ListenSelectCfg ->
                TaskBodyChild.ListenAndSelect(
                    ListenAndSelectComponentImpl(
                        ctx,
                        ::onAction,
                        ::toggleButton,
                        config.task
                    )
                )

            TaskBodyConfig.EmptyCfg -> TaskBodyChild.Empty
        }

    /* ---------- Child → Store Bridges ---------- */

    private fun onAction(passed: Boolean) {
        store.accept(TrainStore.Intent.ButtonClick(passed))
    }

    private fun onCheckChange(inCheck: Boolean) {
        store.accept(TrainStore.Intent.InCheckStateChange(inCheck))
    }
}


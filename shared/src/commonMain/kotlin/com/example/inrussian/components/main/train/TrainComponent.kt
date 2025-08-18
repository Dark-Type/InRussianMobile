package com.example.inrussian.components.main.train

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.example.inrussian.di.SectionDetailComponentFactory
import com.example.inrussian.di.TasksComponentFactory
import com.example.inrussian.models.Course
import com.example.inrussian.models.Section
import com.example.inrussian.navigation.configurations.TrainConfiguration
import com.example.inrussian.repository.main.train.TrainRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.NonCancellable.cancel
import kotlinx.coroutines.NonCancellable.isActive
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

sealed interface TrainOutput {
    data object NavigateBack : TrainOutput
}
sealed interface SectionDetailOutput {
    data object NavigateBack : SectionDetailOutput
}



interface TrainComponent {
    val childStack: Value<ChildStack<*, Child>>
    fun onSectionSelected(sectionId: String)
    fun onBack()

    sealed interface Child {
        data class CoursesChild(val component: TrainCoursesListComponent) : Child
        data class SectionDetailChild(val component: SectionDetailComponent) : Child
    }
}
class DefaultTrainComponent(
    componentContext: ComponentContext,
    private val onOutput: (TrainOutput) -> Unit,
    private val sectionDetailComponentFactory: SectionDetailComponentFactory,
    private val repository: TrainRepository,    // <-- inject this
) : TrainComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    override val childStack: Value<ChildStack<*, TrainComponent.Child>> =
        childStack(
            source = navigation,
            serializer = Config.serializer(),
            initialConfiguration = Config.Courses,
            handleBackButton = true,
            childFactory = ::child
        )

    override fun onSectionSelected(sectionId: String) {
        navigation.pushNew(Config.SectionDetail(sectionId))
    }

    override fun onBack() {
        if (childStack.value.backStack.isNotEmpty()) {
            navigation.pop()
        } else {
            onOutput(TrainOutput.NavigateBack)
        }
    }

    private fun child(
        configuration: Config,
        componentContext: ComponentContext
    ): TrainComponent.Child =
        when (configuration) {
            is Config.Courses ->
                TrainComponent.Child.CoursesChild(
                    DefaultTrainCoursesListComponent(
                        componentContext = componentContext,
                        repository = repository,
                        onSectionClick = ::onSectionSelected
                    )
                )
            is Config.SectionDetail ->
                TrainComponent.Child.SectionDetailChild(
                    sectionDetailComponentFactory(
                        componentContext,
                        configuration.sectionId
                    ) { out ->
                        if (out is SectionDetailOutput.NavigateBack) onBack()
                    }
                )
        }

    @Serializable
    sealed interface Config {
        @Serializable
        data object Courses : Config
        @Serializable
        data class SectionDetail(val sectionId: String) : Config
    }
}

interface TrainCoursesListComponent {
    val state: Value<TrainCoursesState>
    fun onSectionClick(sectionId: String)
    fun refresh()
}

@OptIn(ExperimentalCoroutinesApi::class)
class DefaultTrainCoursesListComponent(
    componentContext: ComponentContext,
    private val repository: TrainRepository,
    private val onSectionClick: (String) -> Unit
) : TrainCoursesListComponent, ComponentContext by componentContext {

    private val scope = CoroutineScope(Dispatchers.Main.immediate)

    private val _state = MutableValue(TrainCoursesState(isLoading = true))
    override val state: Value<TrainCoursesState> = _state

    init {
        scope.launch {
            repository
                .userCourses()
                .flatMapLatest { courses ->
                    if (courses.isEmpty()) {
                        flowOf(emptyList<CourseWithSections>())
                    } else {
                        // For each course, map its sections to CourseWithSections
                        val sectionFlows = courses.map { course ->
                            repository.sectionsForCourse(course.id)
                                .map { sections -> CourseWithSections(course, sections) }
                        }
                        combine(sectionFlows) { array -> array.toList() }
                    }
                }
                .collect { list ->
                    _state.value = TrainCoursesState(
                        isLoading = false,
                        courses = list
                    )
                }
        }
    }

    override fun onSectionClick(sectionId: String) = onSectionClick.invoke(sectionId)

    override fun refresh() {
        // If repository supported refresh logic, you could call it here.
        // For now just re-triggering flows (already hot) so no-op.
    }

    fun dispose() = scope.cancel()
}


interface SectionDetailComponent {
    val sectionId: String
    val state: Value<SectionDetailState>

    fun openTasks(option: TasksOption)
    fun onBack()
}

class DefaultSectionDetailComponent(
    componentContext: ComponentContext,
    private val repository: TrainRepository,
    override val sectionId: String,
    private val onOutput: (SectionDetailOutput) -> Unit,
    private val tasksFactory: TasksComponentFactory
) : SectionDetailComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<InnerConfig>()
    private val scope = CoroutineScope(Dispatchers.Main.immediate)

    private val _state = MutableValue(SectionDetailState(isLoading = true, section = null))
    override val state: Value<SectionDetailState> = _state

    val childStack: Value<ChildStack<InnerConfig, InnerChild>> =
        childStack(
            source = navigation,
            serializer = null,
            initialConfiguration = InnerConfig.Details,
            handleBackButton = true,
            childFactory = ::createChild
        )

    init {
        scope.launch {
            repository.section(sectionId).collectLatest { section ->
                _state.value = _state.value.copy(
                    isLoading = section == null,
                    section = section
                )
            }
        }
    }

    override fun openTasks(option: TasksOption) {
        _state.value = _state.value.copy(selectedOption = option)
        navigation.pushNew(InnerConfig.Tasks(option))
    }

    override fun onBack() {
        if (childStack.value.backStack.isNotEmpty()) {
            navigation.pop()
        } else {
            onOutput(SectionDetailOutput.NavigateBack)
        }
    }

    private fun createChild(
        config: InnerConfig,
        ctx: ComponentContext
    ): InnerChild =
        when (config) {
            is InnerConfig.Details -> InnerChild.DetailsChild(this)
            is InnerConfig.Tasks -> InnerChild.TasksChild(
                tasksFactory(
                    ctx,
                    sectionId,
                    config.option
                ) { out ->
                    when (out) {
                        TasksOutput.NavigateBack -> onBack()
                        TasksOutput.CompletedSection -> {
                            _state.value = _state.value.copy(showCompletionDialog = true)
                        }
                    }
                }
            )
        }

    sealed interface InnerConfig {
        data object Details : InnerConfig
        data class Tasks(val option: TasksOption) : InnerConfig
    }

    sealed interface InnerChild {
        data class DetailsChild(val component: SectionDetailComponent) : InnerChild
        data class TasksChild(val component: TasksComponent) : InnerChild
    }
}
enum class TasksOption {
    All, Theory, Practice, Continue
}

sealed interface TasksOutput {
    data object NavigateBack : TasksOutput
    data object CompletedSection : TasksOutput
}


interface TasksComponent {
    val state: Value<TasksState>
    fun markCurrentAs(correct: Boolean)
    fun onBack()
}
class DefaultTasksComponent(
    componentContext: ComponentContext,
    private val repository: TrainRepository,
    private val sectionId: String,
    private val option: TasksOption,
    private val onOutput: (TasksOutput) -> Unit
) : TasksComponent, ComponentContext by componentContext {

    private val scope = CoroutineScope(Dispatchers.Main.immediate)

    private val _state = MutableValue(
        TasksState(
            isLoading = true,
            option = option,
            sectionId = sectionId
        )
    )
    override val state: Value<TasksState> = _state

    init {
        scope.launch { repository.selectOption(sectionId, option) }
        when (option) {
            TasksOption.Continue -> observeQueue()
            TasksOption.All, TasksOption.Theory, TasksOption.Practice -> observeFiltered()
        }
    }

    private fun observeQueue() {
        scope.launch {
            repository.ensureQueuePopulated(sectionId, 3)
            launch {
                repository.userQueue(sectionId).collectLatest { queue ->
                    val section = repository.section(sectionId).first()
                    val tasks = repository.tasksForSection(sectionId).first()
                    val currentTask = queue.firstOrNull()?.let { qi ->
                        tasks.firstOrNull { it.id == qi.taskId }
                    }
                    val completedTasks = section?.completedTasks ?: 0
                    val total = section?.totalTasks ?: 0
                    val percent = section?.progressPercent ?: 0
                    _state.value = _state.value.copy(
                        isLoading = false,
                        currentQueueTask = currentTask,
                        queueSize = queue.size,
                        remainingInQueue = queue.size,
                        totalTasks = total,
                        completedTasks = completedTasks,
                        progressPercent = percent,
                        completed = total > 0 && completedTasks == total
                    )
                    if (_state.value.completed) {
                        onOutput(TasksOutput.CompletedSection)
                    }
                }
            }
        }
    }

    private fun observeFiltered() {
        scope.launch {
            repository.tasksForSection(sectionId).collectLatest { all ->
                val section = repository.section(sectionId).first()
                val filtered = when (option) {
                    TasksOption.All -> all
                    TasksOption.Theory -> all.filter { it.kind == TaskKind.THEORY }
                    TasksOption.Practice -> all.filter { it.kind == TaskKind.PRACTICE }
                    TasksOption.Continue -> emptyList()
                }
                _state.value = _state.value.copy(
                    isLoading = false,
                    filteredTasks = filtered,
                    totalTasks = section?.totalTasks ?: 0,
                    completedTasks = section?.completedTasks ?: 0,
                    progressPercent = section?.progressPercent ?: 0,
                    completed = (section?.totalTasks ?: 0) > 0 &&
                            section?.completedTasks == section?.totalTasks
                )
                if (_state.value.completed) {
                    onOutput(TasksOutput.CompletedSection)
                }
            }
        }
    }

    override fun markCurrentAs(correct: Boolean) {
        if (option != TasksOption.Continue) return
        val current = _state.value.currentQueueTask ?: return
        scope.launch {
            repository.consumeCurrentQueueTask(sectionId, correct)
        }
    }

    override fun onBack() = onOutput(TasksOutput.NavigateBack)

    fun dispose() = scope.cancel()
}
package com.example.inrussian.components.main.train

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.example.inrussian.di.SectionDetailComponentFactory
import com.example.inrussian.di.TrainComponentFactory
import com.example.inrussian.repository.main.train.TrainRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

sealed interface TrainOutput {
    data object NavigateBack : TrainOutput
}

sealed interface SectionDetailOutput {
    data object NavigateBack : SectionDetailOutput
}

sealed interface TasksOutput {
    data object NavigateBack : TasksOutput
    data object CompletedSection : TasksOutput
}

/* ---------------- TrainComponent Root ---------------- */

interface TrainComponent {
    val childStack: Value<ChildStack<Config, Child>>
    fun onSectionSelected(sectionId: String)
    fun onBack()

    sealed interface Child {
        data class CoursesChild(val component: TrainCoursesListComponent) : Child
        data class SectionDetailChild(val component: SectionDetailComponent) : Child
    }

    @Serializable
    sealed interface Config {
        @Serializable
        data object Courses : Config
        @Serializable
        data class SectionDetail(val sectionId: String) : Config
    }
}

class DefaultTrainComponent(
    componentContext: ComponentContext,
    private val onOutput: (TrainOutput) -> Unit,
    private val sectionDetailComponentFactory: SectionDetailComponentFactory,
    private val repository: TrainRepository
) : TrainComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<TrainComponent.Config>()

    override val childStack: Value<ChildStack<TrainComponent.Config, TrainComponent.Child>> =
        childStack(
            source = navigation,
            serializer = TrainComponent.Config.serializer(),
            initialConfiguration = TrainComponent.Config.Courses,
            handleBackButton = true,
            childFactory = ::child
        )

    override fun onSectionSelected(sectionId: String) {
        navigation.pushNew(TrainComponent.Config.SectionDetail(sectionId))
    }

    override fun onBack() {
        if (childStack.value.backStack.isNotEmpty()) {
            navigation.pop()
        } else onOutput(TrainOutput.NavigateBack)
    }

    private fun child(
        configuration: TrainComponent.Config,
        componentContext: ComponentContext
    ): TrainComponent.Child =
        when (configuration) {
            is TrainComponent.Config.Courses ->
                TrainComponent.Child.CoursesChild(
                    DefaultTrainCoursesListComponent(
                        componentContext,
                        repository,
                        ::onSectionSelected
                    )
                )

            is TrainComponent.Config.SectionDetail ->
                TrainComponent.Child.SectionDetailChild(
                    sectionDetailComponentFactory(
                        componentContext,
                        configuration.sectionId
                    ) { out ->
                        if (out is SectionDetailOutput.NavigateBack) onBack()
                    }
                )
        }
}

/* ---------------- Courses List ---------------- */

interface TrainCoursesListComponent {
    val state: Value<TrainCoursesState>
    fun onSectionClick(sectionId: String)
    fun refresh()
}

@OptIn(ExperimentalCoroutinesApi::class)
class DefaultTrainCoursesListComponent(
    componentContext: ComponentContext,
    private val repository: TrainRepository,
    private val onSectionSelected: (String) -> Unit
) : TrainCoursesListComponent, ComponentContext by componentContext {

    private val scope = CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())

    private val _state = MutableValue(TrainCoursesState(isLoading = true))
    override val state: Value<TrainCoursesState> = _state

    init {
        TODO()
        /*scope.launch {
            repository.userCourses()
                .flatMapLatest { courses ->
                    if (courses.isEmpty()) flowOf(emptyList())
                    else {
                        val flows = courses.map { course ->
                            repository.sectionsForCourse(course.id)
                                .map { secs -> CourseWithSections(course, secs) }
                        }
                        combine(flows) { it.toList() }
                    }
                }
                .collect { list ->
                    _state.value = TrainCoursesState(isLoading = false, courses = list)
                }
        }*/
    }

    override fun onSectionClick(sectionId: String) = onSectionSelected(sectionId)

    override fun refresh() { /* no-op for mock */
    }

    fun dispose() = scope.cancel()
}

/* ---------------- Section Detail ---------------- */

interface SectionDetailComponent {
    val sectionId: String
    val state: Value<SectionDetailState>
    fun openTasks(option: TasksOption)
    fun onBack()
}

typealias TrainComponentFactory = (
    ComponentContext,
    String,
    TasksOption,
    (TasksOutput) -> Unit
) -> TrainComponentCopy

typealias SectionDetailComponentFactory = (
    ComponentContext,
    String,
    (SectionDetailOutput) -> Unit
) -> SectionDetailComponent

class DefaultSectionDetailComponent(
    componentContext: ComponentContext,
    private val repository: TrainRepository,
    override val sectionId: String,
    private val onOutput: (SectionDetailOutput) -> Unit,
    private val tasksFactory: TrainComponentFactory
) : SectionDetailComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<InnerConfig>()
    private val scope = CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())

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
            val section = repository.section(sectionId)
            _state.value = _state.value.copy(
                isLoading = section == null,
                section = section
            )

        }
    }

    override fun openTasks(option: TasksOption) {
        _state.value = _state.value.copy(selectedOption = option)
        navigation.pushNew(InnerConfig.Tasks(option))
    }

    override fun onBack() {
        if (childStack.value.backStack.isNotEmpty()) navigation.pop()
        else onOutput(SectionDetailOutput.NavigateBack)
    }

    private fun createChild(config: InnerConfig, ctx: ComponentContext): InnerChild =
        when (config) {
            InnerConfig.Details -> InnerChild.DetailsChild(this)
            is InnerConfig.Tasks -> InnerChild.TasksChild(
                tasksFactory(ctx, sectionId) { out ->
                    when (out) {
                        TasksOutput.NavigateBack -> onBack()
                        TasksOutput.CompletedSection ->
                            _state.value = _state.value.copy(showCompletionDialog = true)

                        SectionDetailOutput.NavigateBack -> onBack()
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
        data class TasksChild(val component: TrainComponentCopy) : InnerChild
    }
}

/* ---------------- Tasks Component (interactive) ---------------- */

data class TasksState(
    val isLoading: Boolean = false,
    val option: TasksOption,
    val sectionId: String,
    val totalTasks: Int = 0,
    val completedTasks: Int = 0,
    val progressPercent: Int = 0,
    val completed: Boolean = false,
    val currentQueueTask: Task? = null,
    val queueSize: Int = 0,
    val remainingInQueue: Int = 0,
    val filteredTasks: List<FullTask> = emptyList(),
    val currentIndex: Int = 0,
    val activeFullTask: FullTask? = null,
    val singleSelection: String? = null,
    val multiSelection: Set<String> = emptySet(),
    val wordOrder: List<String> = emptyList(),
    val wordSelection: Set<String> = emptySet(),
    val textInput: String = "",
    val lastSubmissionCorrect: Boolean? = null,
    val showResult: Boolean = false,
    val canSubmit: Boolean = false,
    val hadIncorrectAttempt: Boolean = false
)

interface TasksComponent {
    val state: Value<TasksState>
    fun selectOption(optionId: String)
    fun toggleOption(optionId: String)
    fun reorderWordOrder(newOrder: List<String>)
    fun updateTextInput(text: String)
    fun submitAnswer()
    fun nextAfterResult()
    fun markCurrentAs(correct: Boolean)
    fun onBack()
}
/*

class DefaultTasksComponent(
    componentContext: ComponentContext,
    private val repository: TrainRepository,
    private val sectionId: String,
    private val option: TasksOption,
    private val onOutput: (TasksOutput) -> Unit,
    private val json: Json = Json
) : TasksComponent, ComponentContext by componentContext {

    private val scope = CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())
    private val _state = MutableValue(
        TasksState(isLoading = true, option = option, sectionId = sectionId)
    )
    private var queueJob: Job? = null
    override val state: Value<TasksState> = _state

    init {
        scope.launch { repository.selectOption(sectionId, option) }
        when (option) {
            TasksOption.Continue -> observeQueue()
            TasksOption.All, TasksOption.Theory, TasksOption.Practice -> observeFiltered()
        }
    }

    override fun selectOption(optionId: String) {
        val full = _state.value.activeFullTask ?: return
        val at = full.answer?.answerType ?: return
        when (at) {
            AnswerType.SINGLE_CHOICE_SHORT,
            AnswerType.SINGLE_CHOICE_LONG ->
                _state.value = _state.value.copy(singleSelection = optionId, canSubmit = true)

            AnswerType.MULTIPLE_CHOICE_SHORT,
            AnswerType.MULTIPLE_CHOICE_LONG -> toggleOption(optionId)

            AnswerType.WORD_SELECTION -> {
                val cur = _state.value.wordSelection.toMutableSet()
                if (!cur.add(optionId)) cur.remove(optionId)
                _state.value = _state.value.copy(wordSelection = cur, canSubmit = cur.isNotEmpty())
            }

            AnswerType.WORD_ORDER -> {
                val cur = _state.value.wordOrder
                if (!cur.contains(optionId)) {
                    val upd = cur + optionId
                    _state.value = _state.value.copy(
                        wordOrder = upd,
                        canSubmit = upd.size == full.options.size
                    )
                }
            }

            AnswerType.TEXT_INPUT -> {}
        }
    }

    override fun toggleOption(optionId: String) {
        val full = _state.value.activeFullTask ?: return
        val at = full.answer?.answerType ?: return
        if (at != AnswerType.MULTIPLE_CHOICE_SHORT && at != AnswerType.MULTIPLE_CHOICE_LONG) return
        val cur = _state.value.multiSelection.toMutableSet()
        if (!cur.add(optionId)) cur.remove(optionId)
        _state.value = _state.value.copy(multiSelection = cur, canSubmit = cur.isNotEmpty())
    }

    override fun reorderWordOrder(newOrder: List<String>) {
        val full = _state.value.activeFullTask ?: return
        if (full.answer?.answerType != AnswerType.WORD_ORDER) return
        _state.value = _state.value.copy(
            wordOrder = newOrder,
            canSubmit = newOrder.size == full.options.size
        )
    }

    override fun updateTextInput(text: String) {
        _state.value = _state.value.copy(textInput = text, canSubmit = text.isNotBlank())
    }

    override fun submitAnswer() {
        val full = _state.value.activeFullTask ?: return
        val answer = full.answer ?: return
        val correct = evaluate(full, answer)

        val currentHadIncorrect = _state.value.hadIncorrectAttempt
        _state.value = _state.value.copy(
            lastSubmissionCorrect = correct,
            showResult = true,
            hadIncorrectAttempt = currentHadIncorrect || !correct
        )
    }



    override fun nextAfterResult() {
        val st = _state.value
        if (!st.showResult) return

        val correct = st.lastSubmissionCorrect == true
        val hadIncorrect = st.hadIncorrectAttempt

        // Reset interaction state but keep hadIncorrectAttempt until we advance (for reinforcement decision)
        val resetInteraction = st.copy(
            showResult = false,
            lastSubmissionCorrect = null,
            singleSelection = null,
            multiSelection = emptySet(),
            wordOrder = emptyList(),
            wordSelection = emptySet(),
            textInput = "",
            canSubmit = false
        )

        when (option) {
            TasksOption.Continue -> {
                if (!correct) {
                    _state.value = resetInteraction.copy(
                        hadIncorrectAttempt = true,
                        activeFullTask = st.activeFullTask
                    )
                } else {
                    val taskId = st.activeFullTask?.task?.id
                    scope.launch {
                        if (taskId != null) {
                            repository.consumeCurrentQueueTask(sectionId, true)
                            if (hadIncorrect && taskId.isNotBlank()) {
                                repository.scheduleReinforcement(sectionId, taskId, 3..5)
                            }
                        }
                    }
                    _state.value = resetInteraction.copy(hadIncorrectAttempt = false)
                }
            }
            TasksOption.All, TasksOption.Theory, TasksOption.Practice -> {
                if (!correct) {
                    _state.value = resetInteraction.copy(
                        hadIncorrectAttempt = true,
                        activeFullTask = st.activeFullTask
                    )
                } else {
                    val nextIndex = st.currentIndex + 1
                    if (nextIndex < st.filteredTasks.size) {
                        _state.value = resetInteraction.copy(
                            currentIndex = nextIndex,
                            activeFullTask = st.filteredTasks[nextIndex],
                            hadIncorrectAttempt = false
                        )
                    } else {
                        _state.value = resetInteraction.copy(
                            hadIncorrectAttempt = false
                        )
                    }
                }
            }
        }
    }

    override fun markCurrentAs(correct: Boolean) {
        if (option != TasksOption.Continue) return
        scope.launch { repository.consumeCurrentQueueTask(sectionId, correct) }
    }

    override fun onBack() = onOutput(TasksOutput.NavigateBack)

    */
/* ------------ Observers ------------ *//*


    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observeQueue() {
        // Cancel any previous collector if you already handle that elsewhere.
        queueJob?.cancel()
        queueJob = scope.launch {
            // Example structure; adapt to your actual combine/flatMap chain.
            combine(
                repository.userQueue(sectionId),
                repository.section(sectionId)
            ) { queue, section -> queue to section }
                .flatMapLatest { (queue, section) ->
                    // Build the FullTask for the head of the queue (if any)
                    val head = queue.firstOrNull()
                    if (head == null) {
                        flowOf(Triple(queue, section, null))
                    } else {
                        buildFullTaskFlow(head.taskId).map { full ->
                            Triple(queue, section, full)
                        }
                    }
                }
                .collect { (queue, section, full) ->


                    val previous = _state.value
                    val previousTaskId = previous.activeFullTask?.task?.id
                    val newTaskId = full?.task?.id
                    val taskChanged = previousTaskId != newTaskId

                    _state.value = previous.copy(
                        isLoading = false,
                        currentQueueTask = full?.task,
                        activeFullTask = full,
                        queueSize = queue.size,
                        remainingInQueue = queue.size,
                        totalTasks = section?.totalTasks ?: 0,
                        completedTasks = section?.completedTasks ?: 0,
                        progressPercent = section?.progressPercent ?: 0,
                        completed = (section?.totalTasks ?: 0) > 0 &&
                                section?.completedTasks == section?.totalTasks,
                        canSubmit = if (taskChanged) false else previous.canSubmit,
                        singleSelection = if (taskChanged) null else previous.singleSelection,
                        multiSelection = if (taskChanged) emptySet() else previous.multiSelection,
                        wordOrder = if (taskChanged) emptyList() else previous.wordOrder,
                        wordSelection = if (taskChanged) emptySet() else previous.wordSelection,
                        textInput = if (taskChanged) "" else previous.textInput,
                        showResult = if (taskChanged) false else previous.showResult,
                        lastSubmissionCorrect = if (taskChanged) null else previous.lastSubmissionCorrect,
                        hadIncorrectAttempt = if (taskChanged) false else previous.hadIncorrectAttempt
                    )
                }
        }
    }

    private fun buildFullTaskFlow(taskId: String): Flow<FullTask?> {
        return combine(
            repository.tasksForSection(sectionId),
            repository.contentItemsForTask(taskId),
            repository.answerOptionsForTask(taskId),
            repository.answerForTask(taskId)
        ) { tasks, contents, options, answer ->
            val task = tasks.firstOrNull { it.id == taskId } ?: return@combine null
            FullTask(task = task, contents = contents, options = options, answer = answer)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observeFiltered() {
        scope.launch {
            repository.tasksForSection(sectionId)
                .flatMapLatest { tasks ->
                    if (tasks.isEmpty()) {
                        flowOf(Triple(emptyList<FullTask>(), sectionId, null as SectionModel?))
                    } else {
                        combine(
                            combine(tasks.map { task ->
                                combine(
                                    repository.contentItemsForTask(task.id),
                                    repository.answerOptionsForTask(task.id),
                                    repository.answerForTask(task.id)
                                ) { contents, options, ans ->
                                    FullTask(
                                        task,
                                        contents.sortedBy { it.orderNum },
                                        options.sortedBy { it.orderNum },
                                        ans
                                    )
                                }
                            }) { it.toList() },
                            repository.section(sectionId)
                        ) { fulls, section ->
                            Triple(fulls, sectionId, section)
                        }
                    }
                }
                .collect { (fullList, _, section) ->
                    val filtered = when (option) {
                        TasksOption.All -> fullList
                        TasksOption.Theory -> fullList.filter { it.task.type in THEORY_TASK_TYPES }
                        TasksOption.Practice -> fullList.filter { it.task.type in PRACTICE_TASK_TYPES }
                        TasksOption.Continue -> emptyList()
                    }
                    _state.value = _state.value.copy(
                        isLoading = false,
                        filteredTasks = filtered,
                        activeFullTask = filtered.firstOrNull(),
                        currentIndex = 0,
                        totalTasks = section?.totalTasks ?: 0,
                        completedTasks = section?.completedTasks ?: 0,
                        progressPercent = section?.progressPercent ?: 0,
                        completed = (section?.totalTasks ?: 0) > 0 &&
                                section?.completedTasks == section?.totalTasks
                    )
                    if (_state.value.completed) onOutput(TasksOutput.CompletedSection)
                }
        }
    }

    */
/* ------------ Evaluation ------------ *//*


    private fun evaluate(full: FullTask, answer: TaskAnswerItem): Boolean =
        when (answer.answerType) {
            AnswerType.SINGLE_CHOICE_SHORT,
            AnswerType.SINGLE_CHOICE_LONG -> evalSingle(full, answer)
            AnswerType.MULTIPLE_CHOICE_SHORT,
            AnswerType.MULTIPLE_CHOICE_LONG -> evalMultiple(full, answer)
            AnswerType.TEXT_INPUT -> evalText(answer)
            AnswerType.WORD_ORDER -> evalWordOrder(answer)
            AnswerType.WORD_SELECTION -> evalWordSelection(answer)
        }

    private fun evalSingle(full: FullTask, answer: TaskAnswerItem): Boolean {
        val sel = state.value.singleSelection ?: return false
        if (full.options.firstOrNull { it.id == sel }?.isCorrect == true) return true
        return safeCheckId(answer.correctAnswer) { ids -> sel in ids }
    }

    private fun evalMultiple(full: FullTask, answer: TaskAnswerItem): Boolean {
        val selected = state.value.multiSelection
        if (selected.isEmpty()) return false
        val correct = safeCollectIds(answer.correctAnswer)
            ?: full.options.filter { it.isCorrect }.map { it.id }.toSet()
        return selected == correct
    }

    private fun evalText(answer: TaskAnswerItem): Boolean {
        val input = state.value.textInput.trim()
        if (input.isEmpty()) return false
        return try {
            normalize(input) == normalize(answer.correctAnswer.jsonPrimitive.content)
        } catch (_: Throwable) {
            false
        }
    }

    private fun evalWordOrder(answer: TaskAnswerItem): Boolean {
        val user = state.value.wordOrder
        if (user.isEmpty()) return false
        val correct = safeCollectList(answer.correctAnswer) ?: return false
        return user == correct
    }

    private fun evalWordSelection(answer: TaskAnswerItem): Boolean {
        val selected = state.value.wordSelection
        if (selected.isEmpty()) return false
        val correct = safeCollectIds(answer.correctAnswer) ?: return false
        return selected == correct
    }

    private fun safeCheckId(el: JsonElement, block: (Set<String>) -> Boolean): Boolean =
        try { block(safeCollectIds(el) ?: emptySet()) } catch (_: Throwable) { false }

    private fun safeCollectIds(el: JsonElement): Set<String>? = try {
        el.jsonArray.map { it.jsonPrimitive.content }.toSet()
    } catch (_: Throwable) {
        try { setOf(el.jsonPrimitive.content) } catch (_: Throwable) { null }
    }

    private fun safeCollectList(el: JsonElement): List<String>? = try {
        el.jsonArray.map { it.jsonPrimitive.content }
    } catch (_: Throwable) { null }

    private fun normalize(s: String) =
        s.lowercase().replace("\\s+".toRegex(), " ").trim()
}*/

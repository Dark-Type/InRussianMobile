package com.example.inrussian.components.main.train.tasks

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.example.inrussian.components.main.train.tasks.TextConnectTaskComponent.PointF
import com.example.inrussian.components.main.train.tasks.TextConnectTaskComponent.RectF
import com.example.inrussian.components.main.train.tasks.TextConnectTaskComponent.State
import com.example.inrussian.models.models.TaskBody.TextTask
import com.example.inrussian.utils.DragSource
import com.example.inrussian.utils.Piece
import kotlin.math.abs
import kotlin.random.Random
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class TextConnectTaskComponentImpl(
    component: ComponentContext,
    private val onContinueClicked: (Boolean) -> Unit,
    private val inChecking: (Boolean) -> Unit,
    private val onButtonEnable: (Boolean) -> Unit,
    listTextTasks: TextTask,

    ) : TextConnectTaskComponent, ComponentContext by component {
    private val _state = MutableValue(
        State(
            leftPieces = listTextTasks.variant.map {
                Piece(Uuid.random().toString(), it.first)
            },
            rightPieces = (listTextTasks.variant.map {
                Piece(
                    Uuid.random().toString(),
                    it.second,
                    it.first
                )
            }).shuffled(Random(42))
        ))
    override val state: Value<State> = _state
    override fun onTaskClick(taskId: String) {
        TODO("Not yet implemented")
    }

    override fun onContinueClick() {
        val s = _state.value
        if (!s.validated) {
            val rightById = s.rightPieces.associateBy { it.id }
            val invalids = mutableSetOf<String>()

            for (left in s.leftPieces) {
                val rid = s.matches[left.id]
                if (rid == null) {
                    invalids.add(left.id)
                } else {
                    val right = rightById[rid]
                    val isMatchCorrect = right != null && right.key == left.label
                    if (!isMatchCorrect) invalids.add(left.id)
                }
            }

            _state.update { prev -> prev.copy(validated = true, invalidLeftIds = invalids) }

            val hasErrors = invalids.isNotEmpty()
            inChecking(hasErrors)
            onButtonEnable(!hasErrors)

            if (!hasErrors) {
                onContinueClicked(true)
            }
        } else {
            onContinueClicked(true)
        }
    }

    override fun onLeftPositioned(leftId: String, rect: RectF) {
        val anchor = rect.center()
        _state.update { s ->
            val map = s.leftAnchors.toMutableMap()
            putIfChanged(map, leftId, anchor)
            s.copy(leftAnchors = map)
        }
    }

    override fun onPairLeftPositioned(leftId: String, rect: RectF) {
        val anchor = rect.center()
        _state.update { s ->
            val map = s.pairLeftAnchors.toMutableMap()
            putIfChanged(map, leftId, anchor)
            s.copy(pairLeftAnchors = map)
        }
    }

    override fun onRightPositioned(rightId: String, rect: RectF) {
        _state.update { s ->
            val map = s.rightRects.toMutableMap()
            putIfChangedRect(map, rightId, rect)
            s.copy(rightRects = map)
        }
    }

    override fun startDrag(fromPair: Boolean, leftId: String) {
        _state.update { s ->
            val source =
                if (fromPair) DragSource.FromPair(leftId) else DragSource.FromUnmatched(leftId)
            val startPos = if (fromPair) s.pairLeftAnchors[leftId]
                ?: s.leftAnchors[leftId] else s.leftAnchors[leftId]
            s.copy(dragSource = source, dragPos = startPos, hoveredRightId = null)
        }
    }

    override fun dragBy(delta: PointF) {
        _state.update { s ->
            val start = s.dragPos ?: s.dragSource?.let { ds ->
                s.pairLeftAnchors[ds.leftId] ?: s.leftAnchors[ds.leftId]
            }
            val next = start?.let { PointF(it.x + delta.x, it.y + delta.y) }
            val hovered = hitTestRight(s.rightRects, next)
            s.copy(dragPos = next, hoveredRightId = hovered)
        }
    }

    override fun dragBy(leftId: String, delta: PointF) {
        _state.update { s ->
            val start = s.dragPos ?: s.leftAnchors[leftId]
            val next = start?.let { PointF(it.x + delta.x, it.y + delta.y) }
            val hovered = hitTestRight(s.rightRects, next)
            s.copy(dragPos = next, hoveredRightId = hovered)
        }
    }

    override fun endDrag() {
        applyDrop()
        _state.update { s -> s.copy(dragSource = null, dragPos = null, hoveredRightId = null) }
    }

    override fun cancelDrag() {
        _state.update { s -> s.copy(dragSource = null, dragPos = null, hoveredRightId = null) }
    }

    override fun reset() {
        _state.update { s ->
            s.copy(
                matches = emptyMap(),
                dragSource = null,
                dragPos = null,
                hoveredRightId = null,
                rightRects = emptyMap(),
                leftAnchors = emptyMap(),
                pairLeftAnchors = emptyMap()
            )
        }
    }

    private fun applyDrop() {
        val current = _state.value
        val src = current.dragSource ?: return
        val rid = current.hoveredRightId ?: return

        _state.update { s ->
            val newMatches = s.matches.toMutableMap()
            val prevLeft = newMatches.entries.firstOrNull { it.value == rid }?.key
            if (prevLeft != null && prevLeft != src.leftId) {
                newMatches.remove(prevLeft)
            }
            newMatches[src.leftId] = rid
            s.copy(matches = newMatches, validated = false, invalidLeftIds = emptySet())
        }
    }

    companion object {
        private fun putIfChanged(
            map: MutableMap<String, PointF>,
            id: String,
            value: PointF,
            eps: Float = 0.5f
        ) {
            val old = map[id]
            if (old == null || abs(old.x - value.x) > eps || abs(old.y - value.y) > eps) {
                map[id] = value
            }
        }

        private fun putIfChangedRect(
            map: MutableMap<String, RectF>,
            id: String,
            value: RectF,
            eps: Float = 0.5f
        ) {
            val old = map[id]
            if (old == null ||
                abs(old.left - value.left) > eps ||
                abs(old.top - value.top) > eps ||
                abs(old.right - value.right) > eps ||
                abs(old.bottom - value.bottom) > eps
            ) {
                map[id] = value
            }
        }

        fun hitTestRight(rightRects: Map<String, RectF>, point: PointF?): String? {
            if (point == null || !point.isFinite()) return null
            return rightRects.entries.firstOrNull { (_, rect) -> rect.containsInclusive(point) }?.key
        }
    }

    /*var correctList = mutableListOf<Pair<Task, Task>>()
    override val state = MutableValue(TextConnectTaskComponent.State())
    val scope = componentCoroutineScope()

    init {
        val tasks = listTextTasks.variant.map { (qText, aText) ->
            val q = TextTaskModel(id = Uuid.random().toString(), text = qText, isAnswer = false)
            val a = TextTaskModel(id = Uuid.random().toString(), text = aText, isAnswer = true)
            q to a
        }

        val shuffledAnswers = tasks.map { it.second }.shuffled()
        val elements = tasks.mapIndexed { index, (q, _) -> q to shuffledAnswers[index] }

        this.correctList = tasks.toMutableList()
        state.value = state.value.copy(elements = elements)
        Logger.i { "current-> $correctList" }
        Logger.i { "elements-> ${state.value.elements}" }
        scope.launch {
            state.subscribe {
                Logger.i { (it.pairs.size == it.elements.size).toString() + "<- is enable" }
                onButtonEnable(it.pairs.size == it.elements.size)
            }
        }
    }

    override fun onTaskClick(taskId: String) {
        if (!state.value.isChecked) {
            if (state.value.selectedTask == null) {
                val pairConnectedTask =
                    state.value.pairs.find { it.first.id == taskId || it.second.id == taskId }
                if (state.value.selectedTask?.id == taskId) state.value = state.value.copy(
                    selectedTask = state.value.selectedTask?.copyWithState(TaskState.NotSelected)
                )
                else if (pairConnectedTask == null) state.value =
                    state.value.copy(selectedTask = findById(taskId).apply {
                        state = TaskState.Selected
                    })
                else {
                    findById(pairConnectedTask.first.id).state = TaskState.NotSelected
                    findById(pairConnectedTask.second.id).state = TaskState.NotSelected
                    state.value.pairs.remove(pairConnectedTask)
                }
            } else {
                val task = findById(taskId)
                val pairConnectedTask =
                    state.value.pairs.find { it.first.id == taskId || it.second.id == taskId }
                if (pairConnectedTask == null)

                    if ((state.value.selectedTask as TextTaskModel).isAnswer == (task as TextTaskModel).isAnswer) {
                        state.value.updateTaskInPairs(taskId, newState = TaskState.Selected)
                        state.value.selectedTask?.state = TaskState.NotSelected
                    } else {
                        if (task.state != TaskState.Selected) {

                            state.value.pairs.add(if (!task.isAnswer) task to state.value.selectedTask!! else state.value.selectedTask!! to task)
                            state.value.selectedTask?.state = TaskState.Connect
                            state.value = state.value.copy(selectedTask = null)
                            task.state = TaskState.Connect
                        } else if (task == state.value.selectedTask) {
                            state.value.selectedTask?.state = TaskState.NotSelected
                            state.value = state.value.copy(selectedTask = null)
                        }
                    }
            }

        }
        Logger.d { state.value.toString() }

    }

    fun findById(taskId: String): Task {
        val taskPair = state.value.elements.find { it.first.id == taskId || it.second.id == taskId }
        return if (taskPair?.second?.id == taskId) taskPair.second else taskPair!!.first
    }

    fun TextConnectTaskComponent.State.updateTaskInPairs(
        taskId: String, newState: TaskState
    ): TextConnectTaskComponent.State {
        val updatedPairs = pairs.map { pair ->
            when {
                pair.first.id == taskId -> pair.first.copyWithState(newState) to pair.second
                pair.second.id == taskId -> pair.first to pair.second.copyWithState(newState)
                else -> pair
            }
        }
        return this.copy(pairs = updatedPairs.toMutableList())
    }

    override fun onContinueClick() {
        if (state.value.isChecked) {
            inChecking(true)
            state.value.hasError?.let { onContinueClicked(it) }
        } else {
            var hasError = false
            val answerList = state.value.copy().elements

            state.value.pairs.forEachIndexed { i, e ->
                if (correctList.find { it.first.id == e.first.id }?.second?.id != e.second.id) {

                    state.value.elements.find { it.first.id == e.first.id }?.first?.state =
                        TaskState.Incorrect

                    state.value.elements.find { it.second.id == e.second.id }?.second?.state =
                        TaskState.Incorrect
                    hasError = true
                } else {
                    state.value.elements.find { it.first.id == e.first.id }?.first?.state =
                        TaskState.Correct

                    state.value.elements.find { it.second.id == e.second.id }?.second?.state =
                        TaskState.Correct

                }
            }
            state.value =
                state.value.copy(elements = answerList, hasError = hasError, isChecked = true)

            inChecking(false)

        }
    }*/
}
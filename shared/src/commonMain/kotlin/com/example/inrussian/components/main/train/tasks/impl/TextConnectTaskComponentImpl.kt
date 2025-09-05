package com.example.inrussian.components.main.train.tasks.impl

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.example.inrussian.components.main.train.tasks.interfaces.TextConnectTaskComponent
import com.example.inrussian.components.main.train.tasks.interfaces.TextConnectTaskComponent.PointF
import com.example.inrussian.components.main.train.tasks.interfaces.TextConnectTaskComponent.RectF
import com.example.inrussian.components.main.train.tasks.interfaces.TextConnectTaskComponent.State
import com.example.inrussian.models.models.task.TaskBody.TextConnectTask
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
    listTextTasks: TextConnectTask,

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
    init {
        updateButtonEnable()
    }
    private fun updateButtonEnable() {
        val s = _state.value
        val enabled = s.leftPieces.isNotEmpty() && s.leftPieces.all { it.id in s.matches.keys }
        onButtonEnable(enabled)
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
        onButtonEnable(false)
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
        updateButtonEnable()
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

}
package com.example.inrussian.components.main.train.tasks

import com.arkivanov.decompose.value.Value
import com.example.inrussian.utils.DragSource
import com.example.inrussian.utils.Piece
import com.example.inrussian.utils.RowModel


interface TextConnectTaskComponent {
    val state: Value<State>

    data class PointF(val x: Float, val y: Float) {
        fun isFinite(): Boolean = x.isFinite() && y.isFinite()
    }

    data class RectF(val left: Float, val top: Float, val right: Float, val bottom: Float) {
        fun containsInclusive(p: PointF?): Boolean {
            if (p == null) return false
            return p.x >= left && p.x <= right && p.y >= top && p.y <= bottom
        }
        fun center(): PointF = PointF((left + right) / 2f, (top + bottom) / 2f)
    }
    data class State(
        val leftPieces: List<Piece> = emptyList(),
        val rightPieces: List<Piece> = emptyList(),
        val matches: Map<String, String> = emptyMap(),

        val dragSource: DragSource? = null,
        val dragPos: PointF? = null,
        val hoveredRightId: String? = null,

        val rightRects: Map<String, RectF> = emptyMap(),
        val leftAnchors: Map<String, PointF> = emptyMap(),
        val pairLeftAnchors: Map<String, PointF> = emptyMap()
    ) {
        val rows: List<RowModel>
            get() {
                val rightById = rightPieces.associateBy { it.id }
                val matchedRightIds = matches.values.toSet()
                val unmatchedRights = rightPieces.filter { it.id !in matchedRightIds }.toMutableList()
                val result = mutableListOf<RowModel>()
                for (left in leftPieces) {
                    val rid = matches[left.id]
                    if (rid != null) {
                        val right = rightById[rid]
                        if (right != null) {
                            result.add(RowModel.PairRow(left, right))
                        } else {
                            val rr = if (unmatchedRights.isNotEmpty()) unmatchedRights.removeAt(0) else null
                            result.add(RowModel.UnmatchedRow(left, rr))
                        }
                    } else {
                        val rr = if (unmatchedRights.isNotEmpty()) unmatchedRights.removeAt(0) else null
                        result.add(RowModel.UnmatchedRow(left, rr))
                    }
                }
                while (unmatchedRights.isNotEmpty()) {
                    val r = unmatchedRights.removeAt(0)
                    result.add(RowModel.UnmatchedRow(null, r))
                }
                return result
            }
    }
    fun onLeftPositioned(leftId: String, rect: RectF)
    fun onPairLeftPositioned(leftId: String, rect: RectF)
    fun onRightPositioned(rightId: String, rect: RectF)
    fun startDrag(fromPair: Boolean, leftId: String)
    fun dragBy(delta: PointF)
    fun dragBy(leftId: String, delta: PointF)
    fun endDrag()
    fun cancelDrag()
    fun reset()
    /* data class  State(
        val elements: List<Pair<Task, Task>> = listOf(),
        val isChecked: Boolean = false,
        val selectedTask: Task? = null,
        val hasError: Boolean? = null,
        val isAllConnected: Boolean = false,
        val pairs: MutableList<Pair<Task, Task>> = mutableListOf()
    )
*/
    fun onTaskClick(taskId: String)
    fun onContinueClick()
}
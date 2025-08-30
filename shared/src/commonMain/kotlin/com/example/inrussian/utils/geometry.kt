package com.example.inrussian.utils

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
data class Piece(val id: String, val label: String, val key: String = id)

sealed interface RowModel {
    val key: String
    data class PairRow(val left: Piece, val right: Piece): RowModel {
        override val key: String = "P-${left.id}-${right.id}"
    }
    data class UnmatchedRow(val left: Piece?, val right: Piece?): RowModel {
        override val key: String = "U-${left?.id ?: "_"}-${right?.id ?: "_"}"
    }
}

sealed interface DragSource { val leftId: String
    data class FromUnmatched(override val leftId: String): DragSource
    data class FromPair(override val leftId: String): DragSource
}
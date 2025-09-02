package com.example.inrussian.root.main.train.task

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.example.inrussian.components.main.train.tasks.ImageConnectTaskComponent
import com.example.inrussian.components.main.train.tasks.ImageConnectTaskComponent.PointF
import com.example.inrussian.components.main.train.tasks.ImageConnectTaskComponent.RectF
import com.example.inrussian.root.main.train.task.Colors.WrongBg
import com.example.inrussian.root.main.train.task.Colors.WrongBorder
import com.example.inrussian.root.main.train.task.PairRow
import com.example.inrussian.ui.theme.PuzzleLayoutIn
import com.example.inrussian.ui.theme.PuzzleLayoutOut
import com.example.inrussian.ui.theme.TabSide
import com.example.inrussian.utils.Piece
import com.example.inrussian.utils.RowModel
import com.example.inrussian.utils.RowModel.PairRow
import nekit.corporation.shift_app.ui.theme.LocalExtraColors

@Composable
fun ImageConnectTask(
    component: ImageConnectTaskComponent, onContinueClick: (() -> Unit) -> Unit
) {
    val currentColors = LocalExtraColors.current
    val state by component.state.subscribeAsState()
    Column(
        Modifier
            .fillMaxSize()
            .background(currentColors.baseBackground)
    ) {

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            itemsIndexed(state.rows, key = { _, r -> r.key }) { _, r ->
                when (r) {
                    is PairRow -> {
                        PairRow(
                            left = r.left,
                            right = r.right,
                            isWrong = state.invalidLeftIds.contains(r.left.id),isRightHovered = state.hoveredRightId == r.right.id,
                            onLeftHandlePositioned = { coords ->
                                val rectF = coords.rectInRootToRectF()
                                component.onPairLeftPositioned(r.left.id, rectF)
                            },
                            onRightPositioned = { coords ->
                                val rectF = coords.rectInRootToRectF()
                                component.onRightPositioned(r.right.id, rectF)
                            },
                            onLeftDragStart = {
                                component.startDrag(fromPair = true, leftId = r.left.id)
                            },
                            onLeftDragBy = { delta ->
                                val p = PointF(delta.x, delta.y)
                                component.dragBy(p)
                            },
                            onLeftDragEnd = {
                                component.endDrag()
                            },
                            onLeftDragCancel = {
                                component.cancelDrag()
                            })
                    }

                    is RowModel.UnmatchedRow -> {
                        UnmatchedRow(
                            left = r.left,
                            right = r.right,
                            isRightHovered = r.right?.let { state.hoveredRightId == it.id } == true,
                            isWrong = r.left?.let { state.invalidLeftIds.contains(it.id) } == true,
                            onLeftPositioned = { leftId, coords ->
                                component.onLeftPositioned(leftId, coords.rectInRootToRectF())
                            },
                            onRightPositioned = { rightId, coords ->
                                component.onRightPositioned(rightId, coords.rectInRootToRectF())
                            },
                            onLeftDragStart = { leftId ->
                                component.startDrag(fromPair = false, leftId = leftId)
                            },
                            onLeftDragBy = { leftId, delta ->
                                val p = PointF(delta.x, delta.y)
                                component.dragBy(leftId, p)
                            },
                            onLeftDragEnd = {
                                component.endDrag()
                            },
                            onLeftDragCancel = {
                                component.cancelDrag()
                            })
                    }

                }
            }
            item { Spacer(Modifier.height(24.dp)) }
        }
    }
    onContinueClick {
        component.onContinueClick()
    }

}

@Composable
private fun UnmatchedRow(
    left: Piece?,
    right: Piece?,
    isRightHovered: Boolean,
    isWrong: Boolean,
    onLeftPositioned: (leftId: String, coords: LayoutCoordinates) -> Unit,
    onRightPositioned: (rightId: String, coords: LayoutCoordinates) -> Unit,
    onLeftDragStart: (leftId: String) -> Unit,
    onLeftDragBy: (leftId: String, delta: Offset) -> Unit,
    onLeftDragEnd: () -> Unit,
    onLeftDragCancel: () -> Unit
) {
    val shape = RoundedCornerShape(12.dp)
    val leftBorder = when {
        left == null -> Colors.EmptyBorder
        isWrong -> Colors.WrongBorder
        else -> Colors.Outline
    }
    val leftBg = when {
        left == null -> Colors.EmptyBg
        isWrong -> Colors. WrongBg
        else -> Colors.Surface
    }
    val rightBorder = when {
        right == null -> Colors.EmptyBorder
        isWrong -> WrongBorder
        isRightHovered -> Colors.Accent
        else -> Colors.Outline
    }
    val rightBg = when {
        right == null -> Colors.EmptyBg
        isWrong -> WrongBg
        isRightHovered -> Colors.HoverBg
        else -> Colors.Surface
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .background(Colors.RowBg, shape)
            .border(1.dp, Colors.RowBorder, shape)
            .padding(horizontal = 10.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // LEFT
        Box(
            modifier = Modifier
                .weight(1f)
                .padding(end = 6.dp)
                .then(
                    if (left != null) {
                        Modifier
                            .onGloballyPositioned { onLeftPositioned(left.id, it) }
                            .background(leftBg, RoundedCornerShape(10.dp))
                            .border(1.dp, leftBorder, RoundedCornerShape(10.dp))
                            .pointerInput(left.id) {
                                detectDragGestures(
                                    onDragStart = { onLeftDragStart(left.id) },
                                    onDrag = { change, amount ->
                                        onLeftDragBy(left.id, amount)
                                        change.consume()
                                    },
                                    onDragEnd = { onLeftDragEnd() },
                                    onDragCancel = { onLeftDragCancel() })
                            }
                    } else {
                        Modifier
                            .background(Colors.EmptyBg, RoundedCornerShape(10.dp))
                            .border(1.dp, Colors.EmptyBorder, RoundedCornerShape(10.dp))
                    }
                )
                .padding(horizontal = 10.dp, vertical = 8.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            if (left != null) {
                PuzzleLayoutIn(TabSide.RIGHT, onClick = {}) {
                    Text(left.label, modifier = it)
                }
            } else {
                PuzzleLayoutIn(TabSide.RIGHT, onClick = {}) {
                    TextSmall("—")
                }
            }
        }

        // RIGHT
        Box(
            modifier = Modifier
                .weight(1f)
                .padding(start = 6.dp)
                .then(
                    if (right != null) {
                        Modifier
                            .onGloballyPositioned { onRightPositioned(right.id, it) }
                            .background(rightBg, RoundedCornerShape(10.dp))
                            .border(1.dp, rightBorder, RoundedCornerShape(10.dp))
                    } else {
                        Modifier
                            .background(rightBg, RoundedCornerShape(10.dp))
                            .border(1.dp, rightBorder, RoundedCornerShape(10.dp))
                    }
                )
                .padding(horizontal = 10.dp, vertical = 8.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            if (right != null) {
                PuzzleLayoutOut(onClick = {}) {
                    Text(right.label, modifier = it)
                }
            } else {
                PuzzleLayoutOut(onClick = {}) {
                    TextSmall("—")
                }
            }
        }
    }
}
    /*Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .background(com.example.inrussian.root.main.train.task.Colors.RowBg, shape)
            .border(1.dp, com.example.inrussian.root.main.train.task.Colors.RowBorder, shape)
            .padding(horizontal = 10.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .weight(1f)
                .padding(end = 6.dp)
                .then(
                    if (left != null) {
                    Modifier.onGloballyPositioned { onLeftPositioned(left.id, it) }.background(
                            com.example.inrussian.root.main.train.task.Colors.Surface,
                            RoundedCornerShape(10.dp)
                        ).border(
                            1.dp,
                            com.example.inrussian.root.main.train.task.Colors.Outline,
                            RoundedCornerShape(10.dp)
                        ).pointerInput(left.id) {
                            detectDragGestures(
                                onDragStart = { onLeftDragStart(left.id) },
                                onDrag = { change, amount ->
                                    onLeftDragBy(left.id, amount)
                                    change.consume()
                                },
                                onDragEnd = { onLeftDragEnd() },
                                onDragCancel = { onLeftDragCancel() })
                        }
                } else {
                    Modifier.background(
                            com.example.inrussian.root.main.train.task.Colors.EmptyBg,
                            RoundedCornerShape(10.dp)
                        ).border(
                            1.dp,
                            com.example.inrussian.root.main.train.task.Colors.EmptyBorder,
                            RoundedCornerShape(10.dp)
                        )
                })
                .padding(horizontal = 10.dp, vertical = 8.dp),
            contentAlignment = Alignment.CenterStart) {
            if (left != null) {
                PuzzleLayoutIn(TabSide.RIGHT, onClick = {}) {
                    Text(left.label, modifier = it)

                }
            } else {
                PuzzleLayoutIn(TabSide.RIGHT, onClick = {}) {
                    TextSmall("—")
                }
            }
        }

        val rightBorder = when {
            right == null -> com.example.inrussian.root.main.train.task.Colors.EmptyBorder
            isWrong -> Red
            isRightHovered -> com.example.inrussian.root.main.train.task.Colors.Accent
            else -> com.example.inrussian.root.main.train.task.Colors.Outline
        }
        val rightBg = when {
            right == null -> com.example.inrussian.root.main.train.task.Colors.EmptyBg
            isWrong -> Red
            isRightHovered -> com.example.inrussian.root.main.train.task.Colors.HoverBg
            else -> com.example.inrussian.root.main.train.task.Colors.Surface
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .padding(start = 6.dp)
                .then(
                    if (right != null) {
                    Modifier.onGloballyPositioned { onRightPositioned(right.id, it) }
                        .background(rightBg, RoundedCornerShape(10.dp))
                        .border(1.dp, rightBorder, RoundedCornerShape(10.dp))
                } else {
                    Modifier.background(rightBg, RoundedCornerShape(10.dp))
                        .border(1.dp, rightBorder, RoundedCornerShape(10.dp))
                })
                .padding(horizontal = 10.dp, vertical = 8.dp),
            contentAlignment = Alignment.CenterStart) {
            if (right != null) {
                PuzzleLayoutOut(onClick = {}) {
                    Text(right.label, modifier = it)
                }
            } else {
                PuzzleLayoutOut(onClick = {}) {
                    TextSmall("—")
                }
            }
        }
    }
}*/

@Composable
private fun PairRow(
    left: Piece,
    right: Piece,
    isWrong: Boolean,
    isRightHovered: Boolean,
    onLeftHandlePositioned: (LayoutCoordinates) -> Unit,
    onRightPositioned: (LayoutCoordinates) -> Unit,
    onLeftDragStart: () -> Unit,
    onLeftDragBy: (Offset) -> Unit,
    onLeftDragEnd: () -> Unit,
    onLeftDragCancel: () -> Unit
) {
    val shape = RoundedCornerShape(12.dp)
    val leftBorder = if (isWrong) WrongBorder else Color.Transparent
    val leftBg = if (isWrong) WrongBg else Color.Transparent
    val rightBorder = when {
        isWrong -> WrongBorder
        isRightHovered -> Colors.Accent
        else -> Colors.Primary
    }
    val rightBg = when {
        isWrong -> WrongBg
        else -> Colors.MatchedBg
    }
    val rightShape = RoundedCornerShape(10.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .background(com.example.inrussian.root.main.train.task.Colors.MatchedBg, shape)
            .border(
                1.dp,
                if (isRightHovered) com.example.inrussian.root.main.train.task.Colors.Accent else com.example.inrussian.root.main.train.task.Colors.Primary,
                shape
            )
            .padding(horizontal = 10.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        PuzzleLayoutIn(
            TabSide.RIGHT, onClick = {},
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(end = 6.dp)
                .onGloballyPositioned { onLeftHandlePositioned(it) }
                .border(1.dp, leftBorder, RoundedCornerShape(10.dp))
                .background(leftBg, RoundedCornerShape(10.dp))
                .pointerInput(left.id) {
                    detectDragGestures(
                        onDragStart = { onLeftDragStart() },
                        onDrag = { change, amount ->
                            onLeftDragBy(amount)
                            change.consume()
                        },
                        onDragEnd = { onLeftDragEnd() },
                        onDragCancel = { onLeftDragCancel() })
                }
                .padding(horizontal = 10.dp, vertical = 8.dp),
        ) {
            Text(
                left.label, modifier = it
                    .fillMaxHeight()
                    .align(Alignment.Center)
            )

        }

        val rightShape = RoundedCornerShape(10.dp)
        PuzzleLayoutOut(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(start = 6.dp)
                .onGloballyPositioned { onRightPositioned(it) }
                .background(rightBg, rightShape)
                .border(1.dp, rightBorder, rightShape)
                .padding(horizontal = 10.dp, vertical = 8.dp), onClick = {}) {
            Text(
                right.label, modifier = it
                    .fillMaxHeight()
                    .align(Alignment.Center)
            )
        }
    }
}

private fun LayoutCoordinates.rectInRootRect(): Rect = this.positionInRoot().let { pos ->
    val size: IntSize = this.size
    Rect(
        left = pos.x, top = pos.y, right = pos.x + size.width, bottom = pos.y + size.height
    )
}

private fun LayoutCoordinates.rectInRootToRectF(): RectF = rectInRootRect().toRectF()

@Composable
private fun TextSmall(text: String, color: Color = Colors.TextSecondary) {
    BasicText(
        text = text, style = TextStyle(
            color = color, fontSize = 13.sp
        )
    )
}

private fun Rect.toRectF(): RectF = RectF(left, top, right, bottom)

object Colors {
    internal val WrongBg = Color(0xFFFFEBEE) // светло-красный фон
    internal val WrongBorder = Color(0xFFEF5350) //val Background = Color(0xFFF7F7F7)
    val TopBar = Color(0xFFEDEDED)

    val RowBg = Color(0xFFFDFDFD)
    val RowBorder = Color(0xFFE5E5E5)

    val Surface = Color(0xFFFFFFFF)
    val Outline = Color(0xFFBDBDBD)
    val Primary = Color(0xFF2962FF)
    val Accent = Color(0xFF00BFA5)

    val EmptyBg = Color(0xFFF5F5F5)
    val EmptyBorder = Color(0xFFDDDDDD)

    val HoverBg = Accent.copy(alpha = 0.16f)
    val MatchedBg = Color(0xFFE3F2FD)

    val TextPrimary = Color(0xFF1E1E1E)
    val TextSecondary = Color(0xFF424242)
}
package com.example.inrussian.root.main.train.task

import android.provider.CalendarContract.Colors
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
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.example.inrussian.components.main.train.tasks.TextConnectTaskComponent
import com.example.inrussian.components.main.train.tasks.TextConnectTaskComponent.PointF
import com.example.inrussian.components.main.train.tasks.TextConnectTaskComponent.RectF
import com.example.inrussian.models.models.TaskState
import com.example.inrussian.models.models.task.TextTaskModel
import com.example.inrussian.ui.theme.CommonButton
import com.example.inrussian.ui.theme.Green
import com.example.inrussian.ui.theme.Grid
import com.example.inrussian.ui.theme.Orange
import com.example.inrussian.ui.theme.PuzzleLayoutIn
import com.example.inrussian.ui.theme.PuzzleLayoutOut
import com.example.inrussian.ui.theme.TabBackground
import com.example.inrussian.ui.theme.TabSide
import com.example.inrussian.utils.DragSource
import com.example.inrussian.utils.Piece
import com.example.inrussian.utils.RowModel
import com.example.inrussian.utils.RowModel.PairRow
import inrussian.composeapp.generated.resources.Res
import inrussian.composeapp.generated.resources.`continue`
import org.jetbrains.compose.resources.stringResource


@Composable
fun TextConnect(
    component: TextConnectTaskComponent,
    onContinueClick: (() -> Unit) -> Unit
) {
    val state by component.state.subscribeAsState()
    Column(Modifier.fillMaxSize().background(TabBackground)) {

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
                            isRightHovered = state.hoveredRightId == r.right.id,
                            onLeftHandlePositioned = { coords ->
                                // convert Compose Rect -> shared RectF then call store
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
                            }
                        )
                    }
                    is RowModel.UnmatchedRow -> {
                        UnmatchedRow(
                            left = r.left,
                            right = r.right,
                            isRightHovered = r.right?.let { state.hoveredRightId == it.id } == true,
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
                            }
                        )
                    }

                }
            }
            item { Spacer(Modifier.height(24.dp)) }
        }
    }
    /*Grid(
        2,
        dataSize = 2,
        withSpacer = true,
        vSpacing = 24.dp
    ) {
        items(state.elements.size * 2) { mod,index ->
            if (index % 2 == 0)
                PuzzleLayoutIn(
                    TabSide.RIGHT,
                    modifier = mod,
                    background = when (state.elements[index / 2].first.state) {
                        TaskState.Correct -> Green
                        TaskState.Incorrect -> Red
                        TaskState.NotSelected -> White
                        TaskState.Selected -> Orange
                        TaskState.Connect -> Orange.copy(0.5f)
                    },
                    onClick = { component.onTaskClick(state.elements[index / 2].first.id) }) {
                    Text((state.elements[index / 2].first as TextTaskModel).text, it)
                }
            else
                PuzzleLayoutOut(
                    onClick = { component.onTaskClick(state.elements[index / 2].second.id) },
                    modifier = mod,
                    color = when (state.elements[index / 2].second.state) {
                        TaskState.Correct -> Green
                        TaskState.Incorrect -> Red
                        TaskState.NotSelected -> White
                        TaskState.Selected -> Orange
                        TaskState.Connect -> Orange.copy(0.5f)
                    }
                ) {
                    Text((state.elements[index / 2].second as TextTaskModel).text, it)
                }
        }
    }*/
    onContinueClick{
        component.onContinueClick()
    }

}
private fun applyDrop(
    matches: MutableMap<String, String>, dragSource: DragSource?, hoveredRightId: String?
) {
    val src = dragSource ?: return
    val rid = hoveredRightId ?: return

    val prevLeft = matches.entries.firstOrNull { it.value == rid }?.key
    if (prevLeft != null && prevLeft != src.leftId) {
        matches.remove(prevLeft)
    }
    matches[src.leftId] = rid
}


@Composable
private fun UnmatchedRow(
    left: Piece?,
    right: Piece?,
    isRightHovered: Boolean,
    onLeftPositioned: (leftId: String, coords: LayoutCoordinates) -> Unit,
    onRightPositioned: (rightId: String, coords: LayoutCoordinates) -> Unit,
    onLeftDragStart: (leftId: String) -> Unit,
    onLeftDragBy: (leftId: String, delta: Offset) -> Unit,
    onLeftDragEnd: () -> Unit,
    onLeftDragCancel: () -> Unit
) {
    val shape = RoundedCornerShape(12.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .background(com.example.inrussian.root.main.train.task.Colors.RowBg, shape)
            .border(1.dp,com.example.inrussian.root.main.train.task.Colors.RowBorder, shape)
            .padding(horizontal = 10.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .padding(end = 6.dp)
                .then(
                    if (left != null) {
                        Modifier
                            .onGloballyPositioned { onLeftPositioned(left.id, it) }
                            .background(com.example.inrussian.root.main.train.task.Colors.Surface, RoundedCornerShape(10.dp))
                            .border(1.dp,com.example.inrussian.root.main.train.task.Colors.Outline, RoundedCornerShape(10.dp))
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
                            .background(com.example.inrussian.root.main.train.task.Colors.EmptyBg, RoundedCornerShape(10.dp))
                            .border(1.dp,com.example.inrussian.root.main.train.task.Colors.EmptyBorder, RoundedCornerShape(10.dp))
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
            right == null ->com.example.inrussian.root.main.train.task.Colors.EmptyBorder
            isRightHovered ->com.example.inrussian.root.main.train.task.Colors.Accent
            else ->com.example.inrussian.root.main.train.task.Colors.Outline
        }
        val rightBg = when {
            right == null ->com.example.inrussian.root.main.train.task.Colors.EmptyBg
            isRightHovered ->com.example.inrussian.root.main.train.task.Colors.HoverBg
            else ->com.example.inrussian.root.main.train.task.Colors.Surface
        }
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
}

@Composable
private fun PairRow(
    left: Piece,
    right: Piece,
    isRightHovered: Boolean,
    onLeftHandlePositioned: (LayoutCoordinates) -> Unit,
    onRightPositioned: (LayoutCoordinates) -> Unit,
    onLeftDragStart: () -> Unit,
    onLeftDragBy: (Offset) -> Unit,
    onLeftDragEnd: () -> Unit,
    onLeftDragCancel: () -> Unit
) {
    val shape = RoundedCornerShape(12.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .background(com.example.inrussian.root.main.train.task.Colors.MatchedBg, shape)
            .border(1.dp, if (isRightHovered)com.example.inrussian.root.main.train.task.Colors.Accent else
                com.example.inrussian.root.main.train.task.Colors.Primary, shape)
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
                .background(White.copy(alpha = 0.0f))
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
            Text(left.label, modifier = it
                .fillMaxHeight()
                .align(Alignment.Center))

        }

        val rightShape = RoundedCornerShape(10.dp)
        PuzzleLayoutOut(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(start = 6.dp)
                .onGloballyPositioned { onRightPositioned(it) }
                .background(com.example.inrussian.root.main.train.task.Colors.MatchedBg, rightShape)
                .padding(horizontal = 10.dp, vertical = 8.dp), onClick = {}) {
            Text(right.label, modifier = it.fillMaxHeight().align(Alignment.Center))
        }
    }
}

/* ---------------- UI bits ---------------- */

@Composable
private fun TopBar(onReset: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .background(com.example.inrussian.root.main.train.task.Colors.TopBar)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextTitle("Single vertical stack: rows show Left | Right; drag Left onto Right to fuse")
        Spacer(Modifier.weight(1f))
        SimpleButton("Reset", onClick = onReset)
    }
}

@Composable
private fun TextTitle(text: String, color: Color =com.example.inrussian.root.main.train.task.Colors.TextPrimary) {
    BasicText(
        text = text, style = TextStyle(
            color = color, fontSize = 16.sp, fontWeight = FontWeight.SemiBold
        )
    )
}

@Composable
private fun TextBody(text: String, color: Color =com.example.inrussian.root.main.train.task.Colors.TextPrimary) {
    BasicText(
        text = text, style = TextStyle(
            color = color, fontSize = 16.sp
        )
    )
}

@Composable
private fun TextSmall(text: String, color: Color = com.example.inrussian.root.main.train.task.Colors.TextSecondary) {
    BasicText(
        text = text, style = TextStyle(
            color = color, fontSize = 13.sp
        )
    )
}

@Composable
private fun SimpleButton(text: String, onClick: () -> Unit) {
    val shape = RoundedCornerShape(8.dp)
    Box(
        modifier = Modifier
            .background(com.example.inrussian.root.main.train.task.Colors.Primary, shape)
            .border(1.dp,com.example.inrussian.root.main.train.task.Colors.Primary.copy(alpha = 0.9f), shape)
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { onClick() },
                    onDrag = { _, _ -> },
                    onDragEnd = {},
                    onDragCancel = {})
            }, contentAlignment = Alignment.Center
    ) {
        BasicText(
            text = text, style = TextStyle(color = Color.White, fontSize = 13.sp)
        )
    }
}

fun Offset.toPointF(): PointF = PointF(x, y)
fun Rect.toRectF(): RectF = RectF(left, top, right, bottom)

// reuse user's LayoutCoordinates.rectInRoot() that returns androidx.compose.ui.geometry.Rect
fun LayoutCoordinates.rectInRootRect(): Rect = this.positionInRoot().let { pos ->
    val size: IntSize = this.size
    Rect(
        left = pos.x,
        top = pos.y,
        right = pos.x + size.width,
        bottom = pos.y + size.height
    )
}
fun LayoutCoordinates.rectInRootToRectF(): RectF = rectInRootRect().toRectF()
private object Colors {
    val Background = Color(0xFFF7F7F7)
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

/* ---------------- Geometry helpers ---------------- */

private fun LayoutCoordinates.rectInRoot(): Rect {
    val pos = positionInRoot()
    val size: IntSize = this.size
    return Rect(
        left = pos.x, top = pos.y, right = pos.x + size.width, bottom = pos.y + size.height
    )
}

package com.example.inrussian.root.main.train.task

import android.service.quicksettings.Tile
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.example.inrussian.components.main.train.tasks.interfaces.TextConnectTaskComponent
import com.example.inrussian.components.main.train.tasks.interfaces.TextConnectTaskComponent.PointF
import com.example.inrussian.components.main.train.tasks.interfaces.TextConnectTaskComponent.RectF
import com.example.inrussian.ui.theme.PuzzleLayoutIn
import com.example.inrussian.ui.theme.PuzzleLayoutOut
import com.example.inrussian.ui.theme.TabSide
import com.example.inrussian.utils.Piece
import com.example.inrussian.utils.RowModel
import com.example.inrussian.utils.RowModel.PairRow
import com.example.inrussian.ui.theme.LocalExtraColors


@Composable
fun TextConnect(
    component: TextConnectTaskComponent,
    onContinueClick: (() -> Unit) -> Unit
) {
    val currentColors = LocalExtraColors.current
    val state by component.state.subscribeAsState()

    println("THIS IS THIS SCREEEEEEEEEEEEEEN!!!!!!!!!!!!!!!!!!!!!!!!!")

    Column(
        Modifier
            .fillMaxSize()
            .background(currentColors.baseBackground)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            itemsIndexed(state.rows, key = { _, r -> r.key }) { _, r ->
                when (r) {
                    is PairRow -> PairRow(
                        left = r.left,
                        right = r.right,
                        isRightHovered = state.hoveredRightId == r.right.id,
                        isWrong = state.invalidLeftIds.contains(r.left.id),
                        onLeftHandlePositioned = { coords ->
                            component.onPairLeftPositioned(r.left.id, coords.rectInRootToRectF())
                        },
                        onRightPositioned = { coords ->
                            component.onRightPositioned(r.right.id, coords.rectInRootToRectF())
                        },
                        onLeftDragStart = { component.startDrag(fromPair = true, leftId = r.left.id) },
                        onLeftDragBy = { delta -> component.dragBy(PointF(delta.x, delta.y)) },
                        onLeftDragEnd = component::endDrag,
                        onLeftDragCancel = component::cancelDrag
                    )
                    is RowModel.UnmatchedRow -> UnmatchedRow(
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
                        onLeftDragStart = { leftId -> component.startDrag(fromPair = false, leftId = leftId) },
                        onLeftDragBy = { leftId, delta -> component.dragBy(leftId, PointF(delta.x, delta.y)) },
                        onLeftDragEnd = component::endDrag,
                        onLeftDragCancel = component::cancelDrag
                    )
                }
            }
            item { Spacer(Modifier.height(24.dp)) }
        }
    }
    onContinueClick { component.onContinueClick() }
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
    PuzzleRowContainer(
        isHovered = isRightHovered,
        isWrong = isWrong,
        matched = false
    ) {
        // LEFT
        PuzzleTile(
            placeholder = left == null,
            isHovered = false,
            isWrong = isWrong,
            onPositioned = left?.let { { c -> onLeftPositioned(it.id, c) } },
            modifier = Modifier
                .weight(1f)
                .padding(end = TaskDimens.betweenTiles)
                .pointerInput(left?.id) {
                    if (left != null) {
                        detectDragGestures(
                            onDragStart = { onLeftDragStart(left.id) },
                            onDrag = { ch, a -> onLeftDragBy(left.id, a).also { ch.consume() } },
                            onDragEnd = onLeftDragEnd,
                            onDragCancel = onLeftDragCancel
                        )
                    }
                }
        ) {
            PuzzleLayoutIn(
                tabSide = TabSide.RIGHT,
                notchDiameter = TaskDimens.notchDiameter,
                background = taskColors().tileBg,
                onClick = {}
            ) {  val C = taskColors()
                Text(left?.label ?: "—", modifier = it,style = TaskTypography.secondary, color = C.text) }
        }

        // RIGHT
        PuzzleTile(
            placeholder = right == null,
            isHovered = isRightHovered,
            isWrong = isWrong,
            onPositioned = right?.let { { c -> onRightPositioned(it.id, c) } },
            modifier = Modifier
                .weight(1f)
                .padding(start = TaskDimens.betweenTiles)
        ) {
            PuzzleLayoutOut(
                notchDiameter = TaskDimens.notchDiameter,
                color = taskColors().tileBg,
                onClick = {}
            ) { val C = taskColors()
                Text(right?.label ?: "—", style = TaskTypography.secondary, color = C.text)}
        }
    }
}



@Composable
private fun PairRow(
    left: Piece,
    right: Piece,
    isRightHovered: Boolean,
    isWrong: Boolean,
    onLeftHandlePositioned: (LayoutCoordinates) -> Unit,
    onRightPositioned: (LayoutCoordinates) -> Unit,
    onLeftDragStart: () -> Unit,
    onLeftDragBy: (Offset) -> Unit,
    onLeftDragEnd: () -> Unit,
    onLeftDragCancel: () -> Unit
) {
    PuzzleRowContainer(
        isHovered = isRightHovered,
        isWrong = isWrong,
        matched = !isWrong
    ) {
        PuzzleLayoutIn(
            tabSide = TabSide.RIGHT,
            notchDiameter = TaskDimens.notchDiameter,
            background = taskColors().tileBg,
            onClick = {},
            modifier = Modifier
                .weight(1f)
                .padding(end = TaskDimens.betweenTiles)
                .onGloballyPositioned(onLeftHandlePositioned)
                .pointerInput(left.id) {
                    detectDragGestures(
                        onDragStart = { onLeftDragStart() },
                        onDrag = { ch, a -> onLeftDragBy(a).also { ch.consume() } },
                        onDragEnd = onLeftDragEnd,
                        onDragCancel = onLeftDragCancel
                    )
                }
                .padding(TaskDimens.tilePadding)
        ) { Text(left.label, modifier = it, style = TaskTypography.primary, color = taskColors().text) }

        PuzzleLayoutOut(
            notchDiameter = TaskDimens.notchDiameter,
            color = taskColors().tileBg,
            onClick = {},
            modifier = Modifier
                .weight(1f)
                .padding(start = TaskDimens.betweenTiles)
                .onGloballyPositioned(onRightPositioned)
                .padding(TaskDimens.tilePadding)
        ) { val C = taskColors()

            Text(
                left.label,
                modifier = it,
                style = TaskTypography.primary,
                color = C.text
            )}
    }
}




@Composable
private fun PuzzleRowContainer(
    isHovered: Boolean,
    isWrong: Boolean,
    matched: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {
    val C = taskColors()
    val bg by animateColorAsState(
        when {
            isWrong -> C.error.copy(alpha = 0.06f)
            isHovered -> C.rowBg
            else -> C.rowBg
        }, label = "row-bg"
    )
    val elevation by animateDpAsState(if (isHovered) 3.dp else 1.dp, label = "row-elev")

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = TaskDimens.rowMinHeight),
        color = bg,
        shape = RoundedCornerShape(TaskDimens.rowRadius),
        tonalElevation = elevation,
        shadowElevation = elevation,
        border = null
    ) {
        Row(
            Modifier.padding(TaskDimens.rowPadding),
            verticalAlignment = Alignment.CenterVertically,
            content = content
        )
    }
}

@Composable
private fun PuzzleTile(
    placeholder: Boolean,
    isHovered: Boolean,
    isWrong: Boolean,
    onPositioned: ((LayoutCoordinates) -> Unit)?,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    val C = taskColors()
    val bg by animateColorAsState(
        when {
            placeholder -> C.tileBg.copy(alpha = 0.85f)
            isWrong     -> C.error.copy(alpha = 0.06f)
            isHovered   -> C.hover
            else        -> C.tileBg.copy(alpha = 0.00f)
        }, label = "tile-bg"
    )

    Box(
        modifier = modifier
            .defaultMinSize(minHeight = TaskDimens.tileMinHeight)
            .then(if (onPositioned != null) Modifier.onGloballyPositioned(onPositioned) else Modifier)
            .clip(RoundedCornerShape(TaskDimens.tileRadius))
            .background(bg)
            .padding(TaskDimens.tilePadding),
        contentAlignment = Alignment.CenterStart,
        content = content
    )
}




@Composable
private fun TextSmall(
    text: String, color: Color = com.example.inrussian.root.main.train.task.Colors.TextSecondary
) {
    BasicText(
        text = text, style = TextStyle(
            color = color, fontSize = 13.sp
        )
    )
}

@Composable
private fun taskColors() = LocalExtraColors.current.let { ec ->
    object {
        val rowBg = ec.secondaryBackground
        val tileBg = ec.componentBackground
        val text = ec.fontCaptive
        val subtle = ec.fontInactive
        val hover = ec.baseBackground.copy(alpha = 0.06f)
        val error = ec.errorColor
        val stroke = ec.stroke
    }
}

private fun Rect.toRectF(): RectF = RectF(left, top, right, bottom)

// reuse user's LayoutCoordinates.rectInRoot() that returns androidx.compose.ui.geometry.Rect
private fun LayoutCoordinates.rectInRootRect(): Rect = this.positionInRoot().let { pos ->
    val size: IntSize = this.size
    Rect(
        left = pos.x, top = pos.y, right = pos.x + size.width, bottom = pos.y + size.height
    )
}

private fun LayoutCoordinates.rectInRootToRectF(): RectF = rectInRootRect().toRectF()


/* ---------------- Geometry helpers ---------------- */


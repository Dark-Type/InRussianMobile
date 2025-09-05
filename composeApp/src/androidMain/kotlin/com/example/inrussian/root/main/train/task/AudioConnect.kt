package com.example.inrussian.root.main.train.task

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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.example.inrussian.components.main.train.tasks.interfaces.AudioConnectTaskComponent
import com.example.inrussian.components.main.train.tasks.interfaces.AudioConnectTaskComponent.PointF
import com.example.inrussian.components.main.train.tasks.interfaces.AudioConnectTaskComponent.RectF
import com.example.inrussian.root.main.train.task.Colors.variantBackground
import com.example.inrussian.ui.theme.DarkGrey
import com.example.inrussian.ui.theme.PuzzleLayoutIn
import com.example.inrussian.ui.theme.PuzzleLayoutOut
import com.example.inrussian.ui.theme.TabSide
import com.example.inrussian.utils.Piece
import com.example.inrussian.utils.RowModel
import com.example.inrussian.utils.RowModel.PairRow
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import inrussian.composeapp.generated.resources.Res
import inrussian.composeapp.generated.resources.pause
import inrussian.composeapp.generated.resources.play_button
import nekit.corporation.shift_app.ui.theme.LocalExtraColors
import org.jetbrains.compose.resources.vectorResource

//@Preview(showBackground = true, showSystemUi = true, backgroundColor = 0xFFEAEAEA)
@Composable
fun AudioConnect(
    component: AudioConnectTaskComponent, onContinueClick: (() -> Unit) -> Unit
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
                            isWrong = state.invalidLeftIds.contains(r.left.id),
                            isRightHovered = state.hoveredRightId == r.right.id,
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
                            },
                            leftElement = @Composable { m ->
                                AudioButtonExo(
                                    r.left?.label, modifier = m.fillMaxHeight()
                                )
                            },
                            rightElement = @Composable { m ->
                                Text(
                                    r.right?.label ?: "", modifier = m.fillMaxHeight()
                                )
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
    onLeftPositioned: (leftId: String, coords: LayoutCoordinates) -> Unit,
    onRightPositioned: (rightId: String, coords: LayoutCoordinates) -> Unit,
    onLeftDragStart: (leftId: String) -> Unit,
    onLeftDragBy: (leftId: String, delta: Offset) -> Unit,
    onLeftDragEnd: () -> Unit,
    onLeftDragCancel: () -> Unit,
    leftElement: @Composable (BoxScope.(Modifier) -> Unit),
    rightElement: @Composable (BoxScope.(Modifier) -> Unit),
) {
    val shape = RoundedCornerShape(12.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .border(1.dp, Colors.RowBorder, shape)
            .padding(horizontal = 10.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (left != null) {
            PuzzleLayoutIn(
                TabSide.RIGHT,
                onClick = {},
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(end = 6.dp)
                    .then(
                        Modifier
                            .onGloballyPositioned { onLeftPositioned(left.id, it) }
                            .pointerInput(left.id) {
                                detectDragGestures(
                                    onDragStart = { onLeftDragStart(left.id) },
                                    onDrag = { change, amount ->
                                        onLeftDragBy(left.id, amount)
                                        change.consume()
                                    },
                                    onDragEnd = { onLeftDragEnd() },
                                    onDragCancel = { onLeftDragCancel() })
                            })
                    .padding(horizontal = 10.dp, vertical = 8.dp),
                contentAlignment = Alignment.CenterStart,
                background = variantBackground
            ) {
                this.leftElement(it)

            }
        } else {
            PuzzleLayoutIn(
                TabSide.RIGHT,
                onClick = {},
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(end = 6.dp)
                    .padding(horizontal = 10.dp, vertical = 8.dp),
                background = variantBackground
            ) {
                TextSmall("—")
            }
        }
        if (right != null) {
            PuzzleLayoutOut(
                onClick = {},
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(start = 6.dp)
                    .then(Modifier.onGloballyPositioned { onRightPositioned(right.id, it) }
                    )
                    .padding(horizontal = 10.dp, vertical = 8.dp),
                borderWidth = 1.5.dp,
                color = variantBackground) {
                this.rightElement(it.align(Alignment.Center))

            }

        } else PuzzleLayoutOut(
            onClick = {}, color = variantBackground
        ) {
            TextSmall("—")
        }
    }
}

@Composable
fun AudioButtonExo(
    audioUrl: String?, modifier: Modifier = Modifier, onError: ((Throwable) -> Unit)? = null
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val player = remember {
        com.google.android.exoplayer2.ExoPlayer.Builder(context).build().apply {
            playWhenReady = false
        }
    }
    val isPlayingState = remember { mutableStateOf(player.isPlaying) }
    DisposableEffect(player) {
        val listener = object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                isPlayingState.value = isPlaying
            }

            override fun onPlayerError(error: PlaybackException) {
                onError?.invoke(error)
            }

            override fun onPlaybackStateChanged(state: Int) {
            }
        }
        player.addListener(listener)

        val observer = object : DefaultLifecycleObserver {
            override fun onPause(owner: LifecycleOwner) {
                player.playWhenReady = false
            }

            override fun onStop(owner: LifecycleOwner) {
                player.pause()
            }

            override fun onDestroy(owner: LifecycleOwner) {
                player.release()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            player.removeListener(listener)
            player.release()
        }
    }
    LaunchedEffect(audioUrl) {
        if (!audioUrl.isNullOrBlank()) {
            try {
                val mediaItem = com.google.android.exoplayer2.MediaItem.fromUri(audioUrl)
                player.setMediaItem(mediaItem)
                player.prepare()
            } catch (e: Exception) {
                onError?.invoke(e)
            }
        } else {
            player.stop()
            player.clearMediaItems()
            isPlayingState.value = false
        }
    }

    IconButton(onClick = {
        if (audioUrl.isNullOrBlank()) return@IconButton
        try {
            if (player.isPlaying) player.pause() else player.play()
        } catch (e: Exception) {
            onError?.invoke(e)
        }
    }, Modifier.size(50.dp)) {
        Icon(
            vectorResource(if (!isPlayingState.value) Res.drawable.play_button else Res.drawable.pause),
            "",
            tint = DarkGrey.copy(0.8f)
        )
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
    val shape = RoundedCornerShape(12.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .background(if (isWrong) Red.copy(0.1f) else Colors.MatchedBg, shape)
            .border(
                1.dp, when {
                    isWrong -> Red
                    isRightHovered -> Colors.Accent
                    else -> Colors.Primary
                }, shape
            )
            .padding(horizontal = 10.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        PuzzleLayoutIn(
            TabSide.RIGHT, onClick = {},
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
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
                .padding(vertical = 8.dp),
            background = if (isWrong) Red.copy(0.1f) else Colors.MatchedBg
        ) {
            AudioButtonExo(left.label)
        }

        PuzzleLayoutOut(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .onGloballyPositioned { onRightPositioned(it) }
                .padding(vertical = 8.dp), onClick = {},
            borderColor = Color.Black,
            borderWidth = 1.5.dp,
            color = if (isWrong) Red.copy(0.1f) else Colors.MatchedBg
        ) {
            Box(Modifier.fillMaxHeight()) {
                Text(
                    right.label, modifier = it.align(Alignment.Center),
                    textAlign = TextAlign.Center
                )
            }
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


package com.example.inrussian.ui.theme

import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp

enum class TabSide { LEFT, RIGHT, TOP, BOTTOM }

@Composable
//@Preview(showBackground = true, showSystemUi = true, backgroundColor = 0xFFEAEAEA)
fun PuzzleLayoutIn(
    tabSide: TabSide = TabSide.LEFT,
    modifier: Modifier = Modifier,
    elementModifier: Modifier = Modifier,
    background: Color = White,
    onClick: () -> Unit,
    element: @Composable BoxScope.(Modifier) -> Unit,
) {
    Box(
        modifier
            .height(IntrinsicSize.Min)
            .drawBehind {
                drawIntoCanvas { canvas ->
                    val native = canvas.nativeCanvas
                    val saveCount = native.saveLayer(0f, 0f, size.width, size.height, null)

                    val bgPaint = android.graphics.Paint().apply {
                        color = background.toArgb()
                        isAntiAlias = true
                    }
                    native.drawRect(0f, 0f, size.width, size.height, bgPaint)

                    val clearPaint = android.graphics.Paint().apply {
                        isAntiAlias = true
                        xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
                    }
                    var cx = size.width
                    var cy = size.height / 2f
                    var radius = size.height / 4f
                    when (tabSide) {
                        TabSide.LEFT -> {
                            cx = 0f
                        }

                        TabSide.RIGHT -> {}
                        TabSide.TOP -> {
                            cx = size.width / 2;cy = 0f;radius = size.width / 4f
                        }

                        TabSide.BOTTOM -> {
                            cx = size.width / 2;cy = size.height;radius = size.width / 4f
                        }
                    }
                    native.drawCircle(cx, cy, radius, clearPaint)

                    native.restoreToCount(saveCount)
                }

            }
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        element(elementModifier.align(Alignment.Center))
    }
}

@Composable
fun PuzzleLayoutOut(
    tabSide: TabSide = TabSide.LEFT,
    modifier: Modifier = Modifier,
    elementModifier: Modifier = Modifier,
    color: Color = White,
    onClick: () -> Unit,
    element: @Composable BoxScope. (Modifier) -> Unit,
) {
    Box(
        modifier
            .height(IntrinsicSize.Min)
            .background(color)
            .drawBehind {
                val r = size.height * 0.25f

                var ovalRect = Rect(

                    left = size.width - r,
                    top = (size.height - 2 * r) / 2f,
                    right = size.width + r,
                    bottom = (size.height - 2 * r) / 2f + 2 * r
                )
                var startAngle = -90f
                var sweepAngle = 180f
                when (tabSide) {
                    TabSide.LEFT -> {
                        ovalRect =
                            Rect(
                                -r,
                                (size.height - 2 * r) / 2f,
                                r,
                                (size.height - 2 * r) / 2f + 2 * r
                            );startAngle = 270f;sweepAngle = -180f
                    }

                    TabSide.RIGHT -> {}
                    TabSide.TOP -> TODO()
                    TabSide.BOTTOM -> TODO()
                }

                drawArc(
                    color = color,
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = true,
                    topLeft = ovalRect.topLeft,
                    size = ovalRect.size
                )
            }
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        element(
            elementModifier
                .align(Alignment.Center)
                .fillMaxHeight()
        )
    }
}

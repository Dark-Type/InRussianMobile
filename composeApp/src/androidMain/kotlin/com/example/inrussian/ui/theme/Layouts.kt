package com.example.inrussian.ui.theme

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
fun Grid(
    columns: Int,
    modifier: Modifier = Modifier,
    hSpacing: Dp = 0.dp,
    vSpacing: Dp = 0.dp,
    reverseLayout: Boolean = false,
    dataSize: Int,
    withSpacer: Boolean = false,
    verticalArrangement: Arrangement.Vertical = if (!reverseLayout) Arrangement.Top else Arrangement.Bottom,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    content: @Composable GridScope.() -> Unit
) {
    val scope = GridScope()

    scope.items.clear()
    scope.content()

    val elements = scope.items.toList()
    val rowCount = (elements.size + columns - 1) / columns
    Log.d("GridDebug", "elements=${elements.size}, rowCount=$rowCount")

    LazyColumn(
        modifier = modifier,
        verticalArrangement = verticalArrangement,
    ) {
        items(rowCount, key = { rowIndex -> rowIndex }) { rowIndex ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),
                horizontalArrangement = horizontalArrangement
            ) {
                for (columnIndex in 0 until columns) {
                    val elementIndex = rowIndex * columns + columnIndex
                    if (elementIndex < elements.size) {
                        elements[elementIndex](
                            Modifier
                                .weight(1f)
                                .fillMaxHeight()
                        )
                    } else {
                        Spacer(Modifier.weight(1f))
                    }
                    if (columnIndex != columns - 1) {
                        if (withSpacer)
                            Spacer(Modifier.weight(1f))
                        else
                            Spacer(Modifier.width(hSpacing))
                    }
                }
            }
            if (rowIndex != rowCount - 1) {
                Spacer(Modifier.height(vSpacing))
            }
        }
    }
}

@DslMarker
private annotation class GridScopeMarker

@GridScopeMarker
class GridScope internal constructor() {
    internal val items = mutableStateListOf<@Composable (Modifier) -> Unit>()
    inline fun <T> itemsIndexed(
        items: List<T>,
        crossinline itemContent: @Composable GridScope.(index: Int, item: T, modifier: Modifier) -> Unit
    ) {
        for ((index, element) in items.withIndex()) {
            item { mod ->
                itemContent(index, element, mod)
            }
        }
    }

    inline fun <T> itemsIndexed(
        items: Array<T>,
        crossinline itemContent: @Composable GridScope.(index: Int, item: T, modifier: Modifier) -> Unit
    ) {
        for ((index, element) in items.withIndex()) {
            item { mod ->
                itemContent(index, element, mod)
            }
        }
    }

    fun item(content: @Composable (Modifier) -> Unit) {
        items += content
    }

    inline fun <T> items(
        items: Array<T>,
        crossinline itemContent: @Composable GridScope.(item: T, modifier: Modifier) -> Unit
    ) {
        for (element in items) {
            item { mod ->
                itemContent(element, mod)
            }
        }
    }

    inline fun <T> items(
        items: List<T>,
        crossinline itemContent: @Composable GridScope.(item: T, modifier: Modifier) -> Unit
    ) {
        for (element in items) {
            item { mod ->
                itemContent(element, mod)
            }
        }
    }

    inline fun items(
        items: Int, crossinline content: @Composable (Modifier, Int) -> Unit
    ) {
        for (element in 0 until items) {
            item { mod ->
                content(mod, element)
            }
        }
    }
}
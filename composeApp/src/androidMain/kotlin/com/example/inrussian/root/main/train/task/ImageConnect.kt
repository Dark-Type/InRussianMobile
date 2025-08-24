package com.example.inrussian.root.main.train.task

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.example.inrussian.components.main.train.tasks.ImageConnectTaskComponent
import com.example.inrussian.models.models.TaskState
import com.example.inrussian.models.models.task.TextTaskModel
import com.example.inrussian.ui.theme.Green
import com.example.inrussian.ui.theme.Orange
import com.example.inrussian.ui.theme.PuzzleLayoutIn
import com.example.inrussian.ui.theme.PuzzleLayoutOut
import com.example.inrussian.ui.theme.TabSide


@Composable
fun ImageConnectTask(
    imageComponent: ImageConnectTaskComponent
) {
    val state by imageComponent.state.subscribeAsState()
    Box(Modifier.fillMaxSize())
    LazyVerticalStaggeredGrid(
        StaggeredGridCells.Fixed(2),
        overscrollEffect = null,
        verticalItemSpacing = 12.dp,
        horizontalArrangement = Arrangement.spacedBy(90.dp)
    ) {
        itemsIndexed(state.elements) { index, element ->
            val workElement = (element as TextTaskModel)
            val color = when (workElement.state) {
                TaskState.Correct -> Green
                TaskState.Incorrect -> Red
                TaskState.NotSelected -> White
                TaskState.Selected -> Orange
                TaskState.Connect -> Orange.copy(0.5f)
            }

            if (index % 2 == 0)
                PuzzleLayoutIn(
                    TabSide.RIGHT,
                    background = color,
                    onClick = { imageComponent.onTaskClick(element.id) }
                ) {
                    Text(workElement.text)
                }
            else
                PuzzleLayoutOut(
                    onClick = { imageComponent.onTaskClick(element.id) },
                    color = color
                ) {
                    Text(workElement.text)
                }
        }
    }
}
package com.example.inrussian.root.main.train.task

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.example.inrussian.components.main.train.tasks.TextConnectTaskComponent
import com.example.inrussian.models.models.TaskState
import com.example.inrussian.models.models.task.TextTaskModel
import com.example.inrussian.ui.theme.Green
import com.example.inrussian.ui.theme.Orange
import com.example.inrussian.ui.theme.PuzzleLayoutIn
import com.example.inrussian.ui.theme.PuzzleLayoutOut
import com.example.inrussian.ui.theme.TabSide


@Composable
fun TextConnect(
    textComponent: TextConnectTaskComponent
) {
    val state by textComponent.state.subscribeAsState()
    LazyVerticalStaggeredGrid(
        StaggeredGridCells.Fixed(2),
        overscrollEffect = null,
        verticalItemSpacing = 12.dp,
        horizontalArrangement = Arrangement.spacedBy(90.dp)
    ) {
        items(state.elements.size * 2) { index ->
            if (index % 2 == 0)
                PuzzleLayoutIn(
                    TabSide.RIGHT,
                    background = when (state.elements[index / 2].first.state) {
                        TaskState.Correct -> Green
                        TaskState.Incorrect -> Red
                        TaskState.NotSelected -> White
                        TaskState.Selected -> Orange
                        TaskState.Connect -> Orange.copy(0.5f)
                    },
                    onClick = { textComponent.onTaskClick(state.elements[index / 2].first.id) }) {
                    Text((state.elements[index / 2].first as TextTaskModel).text)
                }
            else
                PuzzleLayoutOut(
                    onClick = { textComponent.onTaskClick(state.elements[index / 2].second.id) },
                    color = when (state.elements[index / 2].second.state) {
                        TaskState.Correct -> Green
                        TaskState.Incorrect -> Red
                        TaskState.NotSelected -> White
                        TaskState.Selected -> Orange
                        TaskState.Connect -> Orange.copy(0.5f)
                    }
                ) {
                    Text((state.elements[index / 2].second as TextTaskModel).text)
                }
        }
    }
}
package com.example.inrussian.root.main.train.task

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.example.inrussian.components.main.train.tasks.TextConnectTaskComponent
import com.example.inrussian.models.models.TaskState
import com.example.inrussian.models.models.task.TextTaskModel
import com.example.inrussian.ui.theme.CommonButton
import com.example.inrussian.ui.theme.Green
import com.example.inrussian.ui.theme.Grid
import com.example.inrussian.ui.theme.Orange
import com.example.inrussian.ui.theme.PuzzleLayoutIn
import com.example.inrussian.ui.theme.PuzzleLayoutOut
import com.example.inrussian.ui.theme.TabSide
import inrussian.composeapp.generated.resources.Res
import inrussian.composeapp.generated.resources.`continue`
import org.jetbrains.compose.resources.stringResource


@Composable
fun TextConnect(
    component: TextConnectTaskComponent,
    onContinueClick: (() -> Unit) -> Unit
) {
    val state by component.state.subscribeAsState()
    Grid(
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
    }
    onContinueClick{
        component.onContinueClick()
    }

}
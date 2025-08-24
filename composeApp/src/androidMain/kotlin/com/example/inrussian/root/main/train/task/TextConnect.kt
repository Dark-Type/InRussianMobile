package com.example.inrussian.root.main.train.task

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.unit.dp
import com.example.inrussian.models.models.Task
import com.example.inrussian.models.models.TaskState
import com.example.inrussian.models.models.TextTaskModel
import com.example.inrussian.ui.theme.Green
import com.example.inrussian.ui.theme.Orange
import com.example.inrussian.ui.theme.PuzzleLayoutIn
import com.example.inrussian.ui.theme.PuzzleLayoutOut
import com.example.inrussian.ui.theme.TabSide


@Composable
fun TextConnect(
    elements: List<Task> = listOf<Task>(
        TextTaskModel(
            "",
            "какой даун это придумал",
            state = TaskState.Selected
        ),
        TextTaskModel(
            "",
            "какой даун это придумал",
            state = TaskState.Correct
        ),
        TextTaskModel(
            "",
            "какой даун это придумал",
            state = TaskState.Correct
        ),
        TextTaskModel(
            "",
            "какой даун это придумал"
        ),
        TextTaskModel(
            "",
            "какой даун это придумал"
        ),
        TextTaskModel(
            "",
            "какой даун это придумал"
        ),
    )
) {
    Box(Modifier.fillMaxSize())
    LazyVerticalStaggeredGrid(
        StaggeredGridCells.Fixed(2),
        overscrollEffect = null,
        verticalItemSpacing = 12.dp,
        horizontalArrangement = Arrangement.spacedBy(90.dp)
    ) {
        itemsIndexed(elements) { index, element ->
            val workElement = (element as TextTaskModel)
            val color = when (workElement.state) {
                TaskState.Correct -> Green
                TaskState.Incorrect -> Red
                TaskState.NotSelected -> White
                TaskState.Selected -> Orange
            }

            if (index % 2 == 0)
                PuzzleLayoutIn(TabSide.RIGHT, background = color, onClick = {}) {
                    Text(workElement.text)
                }
            else
                PuzzleLayoutOut(onClick = {}, color = color) {
                    Text(workElement.text)
                }
        }
    }
}
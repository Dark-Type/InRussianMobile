package com.example.inrussian.root.main.train.task

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.example.inrussian.components.main.train.tasks.AudioConnectTaskComponent
import com.example.inrussian.models.models.TaskState
import com.example.inrussian.models.models.task.AudioTask
import com.example.inrussian.models.models.task.TextTaskModel
import com.example.inrussian.ui.theme.Black
import com.example.inrussian.ui.theme.DarkGrey
import com.example.inrussian.ui.theme.Green
import com.example.inrussian.ui.theme.Grid
import com.example.inrussian.ui.theme.Orange
import com.example.inrussian.ui.theme.PuzzleLayoutIn
import com.example.inrussian.ui.theme.PuzzleLayoutOut
import com.example.inrussian.ui.theme.TabSide
import inrussian.composeapp.generated.resources.Res
import inrussian.composeapp.generated.resources.pause
import inrussian.composeapp.generated.resources.play_button
import org.jetbrains.compose.resources.vectorResource

//@Preview(showBackground = true, showSystemUi = true, backgroundColor = 0xFFEAEAEA)
@Composable
fun AudioConnect(
    component: AudioConnectTaskComponent,
    onContinueClick: (() -> Unit) -> Unit
) {
    val state by component.state.subscribeAsState()
    Box(Modifier.fillMaxSize())
    Grid(
        2,
        dataSize = 3,
        hSpacing = 30.dp,
        vSpacing = 32.dp
    ) {
        itemsIndexed(state.elements) { index, element, mod ->

            val (background, textColor) = when (element.state) {
                TaskState.Correct -> Green to White
                TaskState.Incorrect -> Red to White
                TaskState.NotSelected -> White to Black
                TaskState.Selected -> Orange to White
                TaskState.Connect -> Orange.copy(0.5f) to White
            }

            if (index % 2 == 0)
                PuzzleLayoutIn(
                    TabSide.RIGHT,
                    background = background,
                    onClick = {},
                    modifier = mod
                ) {
                    IconButton({}, it) {
                        Icon(
                            vectorResource(if ((element as AudioTask).isPlay) Res.drawable.play_button else Res.drawable.pause),
                            "",
                            tint = DarkGrey.copy(0.8f)
                        )
                    }
                }
            else
                PuzzleLayoutOut(onClick = {}, color = background, modifier = mod) {

                        Text(
                            text = (element as TextTaskModel).text,
                            it,
                            textAlign = TextAlign.Center,
                            color = textColor
                        )
                }
        }
        onContinueClick{
            component.onContinueClick()
        }
    }
}

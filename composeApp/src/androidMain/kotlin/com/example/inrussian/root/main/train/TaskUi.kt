package com.example.inrussian.root.main.train

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.example.inrussian.components.main.train.TrainComponentCopy
import com.example.inrussian.root.main.train.task.AudioConnect
import com.example.inrussian.root.main.train.task.ImageAndSelectTaskUi
import com.example.inrussian.root.main.train.task.ImageConnectTask
import com.example.inrussian.root.main.train.task.ListenAndSelectTaskUi
import com.example.inrussian.root.main.train.task.TextConnect
import com.example.inrussian.root.main.train.task.TextInputTask
import com.example.inrussian.root.main.train.task.TextInputWithVariantTask
import com.example.inrussian.ui.theme.Black
import com.example.inrussian.ui.theme.CommonButton
import com.example.inrussian.ui.theme.LightGrey
import com.example.inrussian.ui.theme.Orange
import inrussian.composeapp.generated.resources.Res
import inrussian.composeapp.generated.resources.check
import inrussian.composeapp.generated.resources.`continue`
import inrussian.composeapp.generated.resources.try_again
import org.jetbrains.compose.resources.stringResource

@Composable
fun TaskUi(component: TrainComponentCopy) {
    val slot by component.childSlot.subscribeAsState()
    val state by component.state.subscribeAsState()
    var onEvent: (() -> Unit)? = null
    Column(
        Modifier
            .background(LightGrey)
            .padding(horizontal = 28.dp)
            .padding(bottom = 102.dp)
    ) {
        LinearProgressIndicator(
            { state.percent ?: 0f },
            Modifier.fillMaxWidth(),
            color = Orange,
            trackColor = Orange.copy(0.5f)
        )
        Spacer(Modifier.height(16.dp))
        TaskDescription(
            {},
            state.showedTask?.taskText ?: "",
            state.showedTask?.taskType ?: listOf()
        )
        Spacer(Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            when (val state = slot.child?.instance) {
                TrainComponentCopy.Child.EmptyChild -> EmptyTaskScreen()
                is TrainComponentCopy.Child.ImageConnectChild -> ImageConnectTask(state.component) {
                    onEvent = it
                }
                
                is TrainComponentCopy.Child.TextConnectChild -> TextConnect(state.component) {
                    onEvent = it
                }
                
                null ->
                    Text(
                        "тут совсем пусто",
                        Modifier.align(Alignment.Center),
                        color = Black,
                        fontSize = 38.sp
                    )
                
                is TrainComponentCopy.Child.AudioConnectChild -> AudioConnect(state.component) {
                    onEvent = it
                }
                
                is TrainComponentCopy.Child.TextInputChild -> TextInputTask(state.component) {
                    onEvent = it
                }
                
                is TrainComponentCopy.Child.TextInputWithVariantsChild -> TextInputWithVariantTask(
                    state.component
                ) {
                    onEvent = it
                }
                
                is TrainComponentCopy.Child.ListenAndSelectChild -> ListenAndSelectTaskUi(state.component) {
                    onEvent = it
                }
                
                is TrainComponentCopy.Child.ImageAndSelectChild -> ImageAndSelectTaskUi(state.component) {
                    onEvent = it
                }
            }
        }
        Spacer(Modifier.height(16.dp))
        
        CommonButton(
            when (state.isCorrect) {
                true -> stringResource(Res.string.`continue`)
                false -> stringResource(Res.string.try_again)
                null -> stringResource(Res.string.check)
            },
            state.isButtonEnable,
            { onEvent?.invoke() })
        Spacer(Modifier.height(16.dp))
        
        
    }
}


@Composable
fun EmptyTaskScreen() {
    Box(Modifier.fillMaxSize()) {
        Text("тут совсем пусто", color = Black, fontSize = 38.sp)
    }
}
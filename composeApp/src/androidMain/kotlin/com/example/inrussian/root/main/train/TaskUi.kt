package com.example.inrussian.root.main.train

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.example.inrussian.components.main.train.TrainComponentCopy
import com.example.inrussian.root.main.train.task.ImageConnectTask

//@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TaskUi(component: TrainComponentCopy) {
    val slot by component.childSlot.subscribeAsState()
    Column(Modifier.padding(28.dp)) {
        LinearProgressIndicator({ 0.1f }, Modifier.fillMaxWidth())
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            when (val state = slot.child?.instance) {
                TrainComponentCopy.Child.EmptyChild -> EmptyTaskScreen()
                is TrainComponentCopy.Child.ImageConnectChild -> ImageConnectTask(state.component)
                is TrainComponentCopy.Child.TextConnectChild -> TODO()
                null -> TODO()
            }
        }


    }
}


@Composable
fun EmptyTaskScreen() {
    Box(Modifier.fillMaxSize()) {
        Text("")
    }
}
package com.example.inrussian.root.main.train.v2

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.example.inrussian.R
import com.example.inrussian.components.main.train.TaskBodyChild
import com.example.inrussian.components.main.train.ThemeTasksComponent
import com.example.inrussian.getImageRes
import com.example.inrussian.repository.main.train.TaskType
import com.example.inrussian.root.main.train.task.AudioConnect
import com.example.inrussian.root.main.train.task.ImageAndSelectTaskUi
import com.example.inrussian.root.main.train.task.ImageConnectTask
import com.example.inrussian.root.main.train.task.ListenAndSelectTaskUi
import com.example.inrussian.root.main.train.task.TextConnect
import com.example.inrussian.root.main.train.task.TextInputTask
import com.example.inrussian.root.main.train.task.TextInputWithVariantTask
import com.example.inrussian.stores.main.train.TrainStore
import com.example.inrussian.ui.theme.CommonButton
import com.example.inrussian.ui.theme.LocalExtraColors
import com.example.inrussian.ui.theme.Orange
import inrussian.composeapp.generated.resources.Res
import inrussian.composeapp.generated.resources.attention
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun ThemeTasksScreen(component: ThemeTasksComponent) {
    val state by component.state.subscribeAsState()
    val slot by component.childSlot.subscribeAsState()
    
    when {
        state.isLoading && state.showedTask == null -> Loading()
        state.showedTask == null -> EmptyOrFinished(state, component)
        else -> ActiveTask(state, component, slot.child?.instance)
    }
}

@Composable
private fun ActiveTask(
    state: TrainStore.State, component: ThemeTasksComponent, bodyChild: TaskBodyChild?
) {
    val progress = (state.percent ?: 0f).coerceIn(0f, 1f)
    var onEventState by remember { mutableStateOf<(() -> Unit)?>(null) }
    val currentColors = LocalExtraColors.current
    
    Column(
        Modifier
            .fillMaxSize()
            .background(currentColors.secondaryBackground)
            .padding(horizontal = 20.dp, vertical = 24.dp), horizontalAlignment = Alignment.Start
    ) {
        Spacer(Modifier.height(32.dp))
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp),
            color = Orange,
            trackColor = White
        )
        Spacer(Modifier.height(16.dp))

        TaskDescription(
            {},
            state.showedTask?.question ?: "",
            state.showedTask?.types ?: listOf()
        )
        Spacer(Modifier.height(24.dp))

        Box(Modifier.weight(1f, fill = true)) {
            TaskBodyChildRenderer(bodyChild) { callback -> onEventState = callback }
        }
        
        Spacer(Modifier.height(32.dp))


        when (state.isCorrect) {
            null -> {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    CommonButton(
                        text = stringResource(R.string.continue_button),
                        enable = state.isButtonEnable,
                        onClick = { component.markCorrectAndSubmit();onEventState?.invoke() },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            
            true -> {
                Spacer(Modifier.height(12.dp))
                CommonButton(
                    text = stringResource(R.string.next_button),
                    enable = true,
                    onClick = {
                    component.continueAfterCorrect()
                    onEventState?.invoke()
                }, modifier = Modifier.fillMaxWidth())
            }
            
            false -> {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    CommonButton(
                        text = stringResource(R.string.repeat_button),
                        enable = state.isButtonEnable,
                        onClick = { component.markCorrectAndSubmit();onEventState?.invoke() },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun TaskBodyChildRenderer(child: TaskBodyChild?, onSetOnEvent: ((() -> Unit)?) -> Unit) {
    val currentColor = LocalExtraColors.current
    
    Surface(
        color = currentColor.componentBackground,
        tonalElevation = 2.dp,
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(Modifier.padding(16.dp)) {
            when (child) {
                is TaskBodyChild.TextConnect -> TextConnect(child.component) {
                    onSetOnEvent(it)
                }
                
                is TaskBodyChild.AudioConnect -> AudioConnect(child.component) { onSetOnEvent(it) }
                is TaskBodyChild.ImageConnect -> ImageConnectTask(child.component) { onSetOnEvent(it) }
                is TaskBodyChild.TextInput -> TextInputTask(child.component) { onSetOnEvent(it) }
                is TaskBodyChild.TextInputWithVariant -> TextInputWithVariantTask(child.component) {
                    onSetOnEvent(
                        it
                    )
                }
                
                is TaskBodyChild.ListenAndSelect -> ListenAndSelectTaskUi(child.component) {
                    onSetOnEvent(
                        it
                    )
                }
                
                TaskBodyChild.Empty, null -> Text("No specific body renderer")
                is TaskBodyChild.ImageAndSelect -> ImageAndSelectTaskUi(child.component) {
                    onSetOnEvent(it)
                }
            }
        }
    }
}

@Composable
private fun EmptyOrFinished(state: TrainStore.State, component: ThemeTasksComponent) {
    val finished = (state.percent ?: 0f) >= 0.999f
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                if (finished) "Theme Completed!" else "No tasks",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(16.dp))
            Button(onClick = component::onBack) { Text("Back to Courses") }
        }
    }
}

@Composable
private fun Loading() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}


@Composable
fun TaskDescription(onInfoClick: () -> Unit, text: String, tasksTypes: List<TaskType>) {
    val currentColors = LocalExtraColors.current
    Column(
        Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(currentColors.componentBackground)
            .padding(horizontal = 10.dp)
            .padding(bottom = 16.dp)
    ) {
        Row {
            Spacer(Modifier.width(16.dp))
            for (type in tasksTypes) {
                Image(painterResource(type.getImageRes()), "", Modifier.size(25.dp, 35.dp))
                Spacer(Modifier.width(8.dp))
            }
            Spacer(Modifier.weight(1f))
            IconButton(
                onInfoClick, Modifier
                    .padding(top = 16.dp)
                    .size(33.dp)
            ) {
                Icon(
                    vectorResource(Res.drawable.attention), "", tint = Orange
                )
            }
        }
        Spacer(Modifier.height(4.dp))
        Text(
            text,
            fontSize = 16.sp,
            color = currentColors.fontCaptive
        )
    }
}

package com.example.inrussian.root.main.train.v2

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.example.inrussian.components.main.train.TaskBodyChild
import com.example.inrussian.components.main.train.ThemeTasksComponent
import com.example.inrussian.root.main.train.task.AudioConnect
import com.example.inrussian.root.main.train.task.ImageAndSelectTaskUi
import com.example.inrussian.root.main.train.task.ImageConnectTask
import com.example.inrussian.root.main.train.task.ListenAndSelectTaskUi
import com.example.inrussian.root.main.train.task.TextConnect
import com.example.inrussian.root.main.train.task.TextInputTask
import com.example.inrussian.root.main.train.task.TextInputWithVariantTask
import com.example.inrussian.stores.main.train.TrainStore
import inrussian.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.painterResource

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
    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 24.dp), horizontalAlignment = Alignment.Start
    ) {
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
        )
        Spacer(Modifier.height(16.dp))
        
        Text(
            "Theme: ${state.showedTask?.themeId ?: component.themeId}",
            style = MaterialTheme.typography.labelMedium
        )
        Spacer(Modifier.height(6.dp))
        Text(
            "Progress: ${(progress * 100).toInt()}%", style = MaterialTheme.typography.labelSmall
        )
        Spacer(Modifier.height(20.dp))
        TaskDescription(
            {},
            state.showedTask?.question ?: "",
            state.showedTask?.types ?: listOf()
        )
        Spacer(Modifier.height(24.dp))
        
        TaskBodyChildRenderer(bodyChild) { callback ->
            onEventState = callback
        }
        
        Spacer(Modifier.height(32.dp))
        
        when (state.isCorrect) {
            null -> {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(
                        onClick = { component.markCorrectAndSubmit();onEventState?.invoke() },
                        modifier = Modifier.weight(1f)
                    ) { Text("Check (Mock Correct)") }
                    OutlinedButton(
                        onClick = { component.markIncorrectAttempt();onEventState?.invoke() },
                        modifier = Modifier.weight(1f)
                    ) { Text("Mark Wrong") }
                }
            }
            
            true -> {
                Text(
                    "Correct!",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(12.dp))
                Button(onClick = {
                    component.continueAfterCorrect()
                    onEventState?.invoke()
                }, modifier = Modifier.fillMaxWidth()) {
                    Text("Continue")
                }
            }
            
            false -> {
                Text(
                    "Incorrect, try again.",
                    color = MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(12.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(
                        onClick = { component.markCorrectAndSubmit();onEventState?.invoke() },
                        modifier = Modifier.weight(1f)
                    ) { Text("Force Correct") }
                    OutlinedButton(
                        onClick = { component.markIncorrectAttempt();onEventState?.invoke() },
                        modifier = Modifier.weight(1f)
                    ) { Text("Retry Wrong") }
                }
            }
        }
        
        Spacer(Modifier.height(40.dp))
        OutlinedButton(onClick = component::onBack) { Text("Back") }
    }
}

@Composable
private fun TaskBodyChildRenderer(child: TaskBodyChild?, onSetOnEvent: ((() -> Unit)?) -> Unit) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant,
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

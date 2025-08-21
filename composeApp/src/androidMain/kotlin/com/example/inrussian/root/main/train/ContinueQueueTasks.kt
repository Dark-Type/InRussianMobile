package com.example.inrussian.root.main.train

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.value.Value
import com.example.inrussian.components.main.train.TasksComponent
import com.example.inrussian.components.main.train.TasksOption
import com.example.inrussian.components.main.train.TasksState
import com.example.inrussian.ui.theme.DarkGrey
import com.example.inrussian.ui.theme.Green
import com.example.inrussian.ui.theme.Orange
import inrussian.composeapp.generated.resources.Res
import inrussian.composeapp.generated.resources.attention
import inrussian.composeapp.generated.resources.book
import inrussian.composeapp.generated.resources.headphones
import inrussian.composeapp.generated.resources.list_checkbox
import inrussian.composeapp.generated.resources.pause
import inrussian.composeapp.generated.resources.play_button
import inrussian.composeapp.generated.resources.speaker
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun ContinueQueueTasks(state: TasksState, component: TasksComponent) {
    Row(Modifier.padding(horizontal = 25.dp)) {
        //  TaskDescription()
        //SpeakerElement()
        ChoiceElement()
    }/*Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Очередь задач", style = MaterialTheme.typography.titleLarge)

        LinearProgressIndicator(
            progress = { state.progressPercent / 100f },
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            "Прогресс секции: ${state.progressPercent}% " +
                    "(${state.completedTasks}/${state.totalTasks})",
            style = MaterialTheme.typography.bodySmall
        )

        val full = state.activeFullTask
        if (full == null) {
            Text("Очередь пуста.", style = MaterialTheme.typography.bodyMedium)
        } else {
            TaskCard(
                fullTask = full,
                state = state,
                showQueueMeta = true,
                remainingInQueue = state.remainingInQueue,
                onSelect = component::selectOption,
                onToggle = component::toggleOption,
                onReorderWordOrder = component::reorderWordOrder,
                onWordAdd = { component.selectOption(it) },
                onWordRemove = { *//* remove from order *//* },
                onTextChange = component::updateTextInput
            )
            Spacer(Modifier.height(8.dp))
            SubmissionArea(state = state, component = component)
        }

        Spacer(Modifier.weight(1f))
        OutlinedButton(
            onClick = { component.onBack() },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Назад") }
    }*/
}

@Composable
fun TaskDescription(onInfoClick: () -> Unit, text: String) {
    Column(
        Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(White)
            .padding(horizontal = 10.dp)
            .padding(bottom = 16.dp)
    ) {
        Row {
            Spacer(Modifier.width(16.dp))
            Image(painterResource(Res.drawable.book), "", Modifier.size(25.dp, 35.dp))
            Spacer(Modifier.width(8.dp))
            Image(painterResource(Res.drawable.headphones), "", Modifier.size(25.dp, 35.dp))
            Spacer(Modifier.width(8.dp))
            Image(painterResource(Res.drawable.list_checkbox), "", Modifier.size(25.dp, 35.dp))
            Spacer(Modifier.weight(1f))
            IconButton(
                {}, Modifier
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
            "Прослушайте диалог и ответьте на вопрос:\nНа каком направлении может учиться спикер 2?",
            fontSize = 16.sp
        )
    }
}


@Composable
fun SpeakerElement() {
    Column(
        Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(White)
            .padding(16.dp)
    ) {
        SpeakerItem(
            false,
            "Вы учитесь в Томском Государственном университете?",
            "你在托木斯克州立大学学习吗?",
            "1"
        )
        Spacer(Modifier.height(24.dp))
        SpeakerItem(
            true,
            "Нет, я учусь в Сибирском Государственном Медицинском университете.",
            "不，我在西伯利亚国立医科大学学习。",
            "2"
        )
    }
}

@Composable
fun ChoiceElement() {
    Column(
        Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(White)
            .padding(10.dp)
    ) {
        Row {
            ChoiceItem(true, true, {}, "Медицина", Modifier.weight(1f))
            Spacer(Modifier.width(15.dp))
            ChoiceItem(false, false, {}, "Инженерия", Modifier.weight(1f))

        }
        Spacer(Modifier.height(16.dp))

        Row {
            ChoiceItem(true, false, {}, "Маркетинг", Modifier.weight(1f))
            Spacer(Modifier.width(15.dp))
            ChoiceItem(false, false, {}, "Дизайн", Modifier.weight(1f))

        }
    }
}

@Composable
fun ChoiceItem(
    isSelected: Boolean,
    isCorrect: Boolean,
    onSelect: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
) {
    var modifier = modifier
    if (!isSelected && !isCorrect)
        modifier = modifier.border(1.dp, DarkGrey.copy(0.3f), RoundedCornerShape(12.dp))
    Button(
        onSelect,
        modifier,
        colors = if (isCorrect) ButtonDefaults.textButtonColors()
            .copy(containerColor = Green) else if (isSelected) ButtonDefaults.textButtonColors()
            .copy(containerColor = Orange) else ButtonDefaults.textButtonColors(),
        shape = RoundedCornerShape(10.dp)
    ) {
        Text(text, color = if (isCorrect || isSelected) White else DarkGrey.copy(0.7f))
    }
}


@Composable
fun ColumnScope.SpeakerItem(isPlay: Boolean, text: String, transcription: String, label: String) {
    Text(
        stringResource(Res.string.speaker) + " $label",
        fontSize = 20.sp,
        fontWeight = FontWeight.SemiBold
    )
    Spacer(Modifier.height(12.dp))
    Row(verticalAlignment = Alignment.CenterVertically) {
        IconButton({}, Modifier.size(50.dp)) {
            Icon(
                vectorResource(if (isPlay) Res.drawable.play_button else Res.drawable.pause),
                "",
                tint = DarkGrey.copy(0.8f)
            )
        }
        Spacer(Modifier.width(8.dp))
        Column {
            Text(
                text, fontSize = 16.sp, color = DarkGrey.copy(0.9f), fontWeight = FontWeight.W500
            )
            Spacer(Modifier.height(4.dp))
            Text(
                transcription,
                fontSize = 16.sp,
                color = DarkGrey.copy(0.5f),
                fontWeight = FontWeight.W500
            )
        }
    }
}

class ContinueQueueTasks : TasksComponent {
    override val state: Value<TasksState>
        get() = TODO("Not yet implemented")

    override fun selectOption(optionId: String) {
        TODO("Not yet implemented")
    }

    override fun toggleOption(optionId: String) {
        TODO("Not yet implemented")
    }

    override fun reorderWordOrder(newOrder: List<String>) {
        TODO("Not yet implemented")
    }

    override fun updateTextInput(text: String) {
        TODO("Not yet implemented")
    }

    override fun submitAnswer() {
        TODO("Not yet implemented")
    }

    override fun nextAfterResult() {
        TODO("Not yet implemented")
    }

    override fun markCurrentAs(correct: Boolean) {
        TODO("Not yet implemented")
    }

    override fun onBack() {
        TODO("Not yet implemented")
    }

    @Composable
    @Preview(showBackground = true, showSystemUi = true, backgroundColor = 0x33999999)
    fun Preview() {
        ContinueQueueTasks(TasksState(option = TasksOption.Continue, sectionId = ""), this)
    }
}
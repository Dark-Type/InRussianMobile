package com.example.inrussian.root.main.train

import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.inrussian.components.main.train.AnswerType
import com.example.inrussian.components.main.train.ContentType
import com.example.inrussian.components.main.train.FullTask
import com.example.inrussian.components.main.train.TaskContentItem
import com.example.inrussian.components.main.train.TaskType
import com.example.inrussian.components.main.train.TasksComponent
import com.example.inrussian.components.main.train.TasksState
import com.example.inrussian.models.models.task.AudioTask
import com.example.inrussian.models.models.task.Task
import com.example.inrussian.models.models.TaskState
import com.example.inrussian.ui.theme.Black
import com.example.inrussian.ui.theme.DarkGrey
import com.example.inrussian.ui.theme.Green
import com.example.inrussian.ui.theme.Grid
import com.example.inrussian.ui.theme.Orange
import inrussian.composeapp.generated.resources.Res
import inrussian.composeapp.generated.resources.attention
import inrussian.composeapp.generated.resources.book
import inrussian.composeapp.generated.resources.headphones
import inrussian.composeapp.generated.resources.list_checkbox
import inrussian.composeapp.generated.resources.pause
import inrussian.composeapp.generated.resources.play_button
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun TaskCard(
    fullTask: FullTask,
    state: TasksState,
    showQueueMeta: Boolean,
    remainingInQueue: Int,
    onSelect: (String) -> Unit,
    onToggle: (String) -> Unit,
    onReorderWordOrder: (List<String>) -> Unit,
    onWordAdd: (String) -> Unit,
    onWordRemove: (String) -> Unit,
    onTextChange: (String) -> Unit
) {
    Card(
        Modifier
            .fillMaxWidth()
            .background(White),

        ) {
        Column(
            Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Header(fullTask, showQueueMeta, {})

            ContentsBlock(fullTask)

            /* AnswerBlock(
                 fullTask = fullTask,
                 state = state,
                 onSelect = onSelect,
                 onToggle = onToggle,
                 onReorderWordOrder = onReorderWordOrder,
                 onWordAdd = onWordAdd,
                 onWordRemove = onWordRemove,
                 onTextChange = onTextChange
             )*/
        }
    }
}

@Composable
private fun Header(fullTask: FullTask, showQueueMeta: Boolean, onClick: (String) -> Unit) {
    Column(
        Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(White)
            .padding(horizontal = 10.dp)
            .padding(bottom = 16.dp)
    ) {
        Row {
            Spacer(Modifier.width(2.dp))
            Image(painterResource(Res.drawable.book), "", Modifier.size(25.dp, 35.dp))
            Spacer(Modifier.width(8.dp))
            when (fullTask.task.type) {
                TaskType.LISTEN_AND_CHOOSE -> Image(
                    painterResource(Res.drawable.headphones),
                    "",
                    Modifier.size(25.dp, 35.dp)
                )

                TaskType.READ_AND_CHOOSE -> Image(
                    painterResource(Res.drawable.list_checkbox),
                    "",
                    Modifier.size(25.dp, 35.dp)
                )

                else -> Spacer(Modifier.width(8.dp))
            }

            Spacer(Modifier.weight(1f))
            IconButton(
                { onClick(fullTask.task.id) }, Modifier
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
            fullTask.task.text ?: "",
            fontSize = 16.sp
        )
    }
}

@Composable
private fun ContentsBlock(fullTask: FullTask) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        fullTask.contents.forEach { c ->
            when (c.contentType) {
                ContentType.TEXT -> c.description?.let { Text(it) }
                ContentType.AUDIO -> AudioStub(c)
                ContentType.IMAGE -> ImageStub(c)
                ContentType.VIDEO -> VideoStub(c)
                ContentType.AVATAR -> {}
            }
        }
    }
}




//@Preview(showBackground = true, showSystemUi = true, backgroundColor = 0xFFEAEAEA)



@Composable
private fun AudioStub(item: TaskContentItem) {
    Column(
        Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(White)
            .padding(16.dp)
    ) {
        SpeakerItem(
            false,
            item.description ?: "",
            item.translation ?: "",
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
private fun ImageStub(item: TaskContentItem) {
    Surface(
        modifier = Modifier
            .height(120.dp)
            .fillMaxWidth(),
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = MaterialTheme.shapes.small
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(item.description ?: "Изображение", style = MaterialTheme.typography.labelMedium)
        }
    }
}

@Composable
private fun VideoStub(item: TaskContentItem) {
    Surface(
        modifier = Modifier
            .height(140.dp)
            .fillMaxWidth(),
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = MaterialTheme.shapes.small
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(item.description ?: "Видео", style = MaterialTheme.typography.labelMedium)
        }
    }
}

@Composable
private fun AnswerBlock(
    fullTask: FullTask,
    state: TasksState,
    onSelect: (String) -> Unit,
    onToggle: (String) -> Unit,
    onReorderWordOrder: (List<String>) -> Unit,
    onWordAdd: (String) -> Unit,
    onWordRemove: (String) -> Unit,
    onTextChange: (String) -> Unit
) {
    val answerType = fullTask.answer?.answerType
    if (answerType == null) {
        Text("Нет вариантов ответа", style = MaterialTheme.typography.bodySmall)
        return
    }

    when (answerType) {
        AnswerType.SINGLE_CHOICE_SHORT,
        AnswerType.SINGLE_CHOICE_LONG -> {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                fullTask.options.forEach { option ->
                    val selected = state.singleSelection == option.id
                    SelectableChip(
                        text = option.optionText ?: "...",
                        selected = selected,
                        onClick = { onSelect(option.id) }
                    )
                }
            }
        }

        AnswerType.MULTIPLE_CHOICE_SHORT,
        AnswerType.MULTIPLE_CHOICE_LONG -> {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                fullTask.options.forEach { option ->
                    val selected = option.id in state.multiSelection
                    SelectableChip(
                        text = option.optionText ?: "...",
                        selected = selected,
                        onClick = { onToggle(option.id) }
                    )
                }
            }
        }

        AnswerType.WORD_SELECTION -> {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Выберите подходящие слова:", style = MaterialTheme.typography.labelSmall)
                FlowRowWrap {
                    fullTask.options.forEach { option ->
                        val selected = option.id in state.wordSelection
                        SelectableChip(
                            text = option.optionText ?: "",
                            selected = selected,
                            onClick = { onSelect(option.id) }
                        )
                    }
                }
            }
        }

        AnswerType.WORD_ORDER -> {
            val selectedIds = state.wordOrder
            val remaining = fullTask.options.filterNot { it.id in selectedIds }
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Постройте предложение:", style = MaterialTheme.typography.labelSmall)
                Text("Выбранный порядок:", style = MaterialTheme.typography.bodySmall)
                FlowRowWrap {
                    selectedIds.forEach { id ->
                        val opt = fullTask.options.firstOrNull { it.id == id }
                        if (opt != null) {
                            SelectableChip(
                                text = opt.optionText ?: "",
                                selected = true,
                                onClick = {
                                    // Remove from order
                                    val newOrder = selectedIds.toMutableList()
                                    newOrder.remove(id)
                                    onReorderWordOrder(newOrder)
                                }
                            )
                        }
                    }
                }
                Divider()
                Text("Доступно:", style = MaterialTheme.typography.bodySmall)
                FlowRowWrap {
                    remaining.forEach { opt ->
                        SelectableChip(
                            text = opt.optionText ?: "",
                            selected = false,
                            onClick = {
                                val newOrder = selectedIds + opt.id
                                onReorderWordOrder(newOrder)
                            }
                        )
                    }
                }
            }
        }

        AnswerType.TEXT_INPUT -> {
            var local by remember(
                state.sectionId,
                fullTask.task.id
            ) { mutableStateOf(state.textInput) }
            OutlinedTextField(
                value = local,
                onValueChange = {
                    local = it
                    onTextChange(it)
                },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Ответ") }
            )
        }
    }
}

@Composable
private fun SelectableChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .wrapContentWidth()
            .padding(end = 8.dp, bottom = 8.dp)
            .clickable { onClick() },
        shape = MaterialTheme.shapes.small,
        tonalElevation = if (selected) 4.dp else 0.dp,
        color = if (selected)
            MaterialTheme.colorScheme.primaryContainer
        else
            MaterialTheme.colorScheme.surfaceVariant
    ) {
        Text(
            text,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            style = MaterialTheme.typography.bodySmall,
            color = if (selected) MaterialTheme.colorScheme.onPrimaryContainer
            else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun FlowRowWrap(content: @Composable () -> Unit) {
    // Simple replacement using a LazyRow/Column combo; for simplicity we just Column
    // If you have accompanist-flowlayout, replace with FlowRow.
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
        WrapLayout { content() }
    }
}

/**
 * Extremely naive wrap layout (not perfect). For production replace with FlowRow (Accompanist) or your own layout.
 */
@Composable
private fun WrapLayout(content: @Composable () -> Unit) {
    // Simplification: just place children in a Row; true wrapping requires custom layout.
    Row { content() }
}

/**
 * Submission / result area shared by queue & filtered views.
 */
@Composable
fun SubmissionArea(state: TasksState, component: TasksComponent) {
    if (state.showResult) {
        val correct = state.lastSubmissionCorrect == true
        val color = if (correct) Color(0xFF2E7D32) else Color(0xFFC62828)
        val msg = if (correct) "Верно!" else "Неверно"
        Surface(
            color = color.copy(alpha = 0.08f),
            shape = MaterialTheme.shapes.small
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(msg, color = color, style = MaterialTheme.typography.bodyMedium)
                TextButton(onClick = { component.nextAfterResult() }) {
                    Text(if (correct) "Далее" else "Попробовать ещё")
                }
            }
        }
    } else {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = { component.submitAnswer() },
                enabled = state.canSubmit,
                modifier = Modifier.weight(1f)
            ) { Text("Ответить") }
        }
    }
}

/*
class TaskUiPrimitives : TasksComponent {
    @Composable
    // @Preview(showBackground = true, showSystemUi = true)
    fun Preview() {
        FilteredTasksList(state.value, this)
    }

    override val state = MutableValue(
        TasksState(
            isLoading = false,
            option = TasksOption.Theory,
            sectionId = "sectionId",
            activeFullTask =
                FullTask(Task(""))

        ),

        )

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


}*/

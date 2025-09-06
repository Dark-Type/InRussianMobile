package com.example.inrussian.root.main.train

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.value.Value
import com.example.inrussian.components.main.train.TaskType
import com.example.inrussian.components.main.train.TasksComponent
import com.example.inrussian.components.main.train.TasksOption
import com.example.inrussian.components.main.train.TasksState
import com.example.inrussian.getImageRes
import com.example.inrussian.ui.theme.Orange
import inrussian.composeapp.generated.resources.Res
import inrussian.composeapp.generated.resources.attention
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun ContinueQueueTasks(state: TasksState, component: TasksComponent) {
    Row(Modifier.padding(horizontal = 25.dp)) {
        //  TaskDescription()
        //SpeakerElement()
        //  ChoiceElement()
    }
}

@Composable
fun TaskDescription(onInfoClick: () -> Unit, text: String, tasksTypes: List<TaskType>) {
    Column(
        Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(White)
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
            fontSize = 16.sp
        )
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
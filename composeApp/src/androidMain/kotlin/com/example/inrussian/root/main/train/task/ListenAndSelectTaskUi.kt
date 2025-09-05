package com.example.inrussian.root.main.train.task

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.example.inrussian.components.main.train.tasks.interfaces.ListenAndSelectComponent
import com.example.inrussian.ui.theme.DarkGrey
import com.example.inrussian.ui.theme.Green
import com.example.inrussian.ui.theme.Orange
import inrussian.composeapp.generated.resources.Res
import inrussian.composeapp.generated.resources.pause
import inrussian.composeapp.generated.resources.play_button
import inrussian.composeapp.generated.resources.speaker
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import androidx.compose.runtime.getValue

@Composable
fun ListenAndSelectTaskUi(component: ListenAndSelectComponent) {
    val state by component.state.subscribeAsState()
    
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
fun SpeakerItem(isPlay: Boolean, text: String, transcription: String, label: String) {
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

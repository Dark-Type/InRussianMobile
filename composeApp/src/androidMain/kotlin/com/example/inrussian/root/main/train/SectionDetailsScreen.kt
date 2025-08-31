package com.example.inrussian.root.main.train

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.value.MutableValue
import com.example.inrussian.components.main.train.SectionModel
import com.example.inrussian.components.main.train.SectionDetailComponent
import com.example.inrussian.components.main.train.SectionDetailState
import com.example.inrussian.components.main.train.TasksOption
import com.example.inrussian.components.main.train.ThemeMeta
import com.example.inrussian.root.main.profile.LabelText
import com.example.inrussian.ui.theme.Blue
import com.example.inrussian.ui.theme.CommonButton
import com.example.inrussian.ui.theme.DarkGrey
import com.example.inrussian.ui.theme.LightGrey
import com.example.inrussian.ui.theme.Orange
import com.example.inrussian.ui.theme.Yellow
import com.example.inrussian.ui.theme.lightCircleGreen
import inrussian.composeapp.generated.resources.Res
import inrussian.composeapp.generated.resources.back_arrow
import inrussian.composeapp.generated.resources.continue_education
import inrussian.composeapp.generated.resources.mastered
import inrussian.composeapp.generated.resources.not_passed
import inrussian.composeapp.generated.resources.on_mastered
import inrussian.composeapp.generated.resources.practice
import inrussian.composeapp.generated.resources.theoretical
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun SectionDetailsScreen(
    component: SectionDetailComponent, state: SectionDetailState
) {
    val section: SectionModel? = state.section
    if (state.isLoading || section == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }
    Column(
        Modifier
            .fillMaxSize()
            .background(LightGrey)
    ) {
        Row(modifier = Modifier.padding(horizontal = 16.dp, vertical = 50.dp)) {
            LabelText(section.title)

            Spacer(Modifier.weight(1f))

            IconButton(
                component::onBack, Modifier
                    .clip(CircleShape)
                    .background(DarkGrey.copy(0.6f))
                    .size(40.dp)
            ) {

                Icon(vectorResource(Res.drawable.back_arrow), "", tint = White)
            }
        }
        Row(
            Modifier
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(White)
                .padding(28.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    stringResource(Res.string.mastered) + ": ${state.section?.completedTasks}",
                    color = DarkGrey.copy(0.8f)
                )
                Text(
                    stringResource(Res.string.on_mastered) + ": ${state.section?.completedTasks}",
                    color = DarkGrey.copy(0.8f)
                )
                Text(
                    stringResource(Res.string.not_passed) + ": ${state.section?.completedTasks}",
                    color = DarkGrey.copy(0.8f)
                )
            }
            Spacer(Modifier.weight(1f))
            Box() {

                val color = when {
                    state.section!!.progressPercent < 35 -> Orange
                    state.section!!.progressPercent < 85 -> Yellow
                    else -> lightCircleGreen

                }
                CircularProgressIndicator(
                    progress = { state.section?.progressPercent!!.toFloat() / 100f },
                    color = color,
                    strokeWidth = 5.dp,
                    trackColor = color.copy(0.5f),
                    modifier = Modifier.size(100.dp)
                )
                Text(
                    state.section?.progressPercent.toString() + "%",
                    Modifier.align(Alignment.Center),
                    color = color,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
        Spacer(Modifier.height(80.dp))
        Row {
            Spacer(Modifier.width(16.dp))
            TextButton(
                { component.openTasks(TasksOption.Theory) },
                Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Blue)
                    .weight(1f)
                    .aspectRatio(0.9f),
                contentPadding = PaddingValues()
            ) {
                Text(
                    stringResource(Res.string.theoretical),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    color = White
                )
            }

            Spacer(Modifier.width(16.dp))
            TextButton(
                { component.openTasks(TasksOption.Practice) },
                Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Blue)
                    .weight(1f)
                    .aspectRatio(0.9f),
                contentPadding = PaddingValues()
            ) {
                Text(
                    stringResource(Res.string.practice),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    color = White
                )
            }
            Spacer(Modifier.width(16.dp))

        }
        Spacer(Modifier.weight(1f))
        CommonButton(
            stringResource(Res.string.continue_education),
            true,
            { component.openTasks(TasksOption.Continue) },
            Modifier.padding(horizontal = 16.dp)
        )
        Spacer(Modifier.height(94.dp))

    }

}

class SectionDetailsScreen : SectionDetailComponent {
    override val sectionId: String
        get() = ""
    override val state = MutableValue(
        SectionDetailState(
            isLoading = false, section = SectionModel(
                id = "",
                courseId = "",
                title = "Транспорт",
                totalTheory = 1,
                totalPractice = 1,
                completedTheory = 1,
                completedPractice = 1,
                themes = listOf(ThemeMeta(id = "", 3, 4, 5))
            )
        )
    )

    override fun openTasks(option: TasksOption) {
        TODO("Not yet implemented")
    }

    override fun onBack() {
        TODO("Not yet implemented")
    }

    @Composable
    @Preview(showBackground = true, showSystemUi = true)
    fun Preview() {
        SectionDetailsScreen(this, state.value)
    }

}
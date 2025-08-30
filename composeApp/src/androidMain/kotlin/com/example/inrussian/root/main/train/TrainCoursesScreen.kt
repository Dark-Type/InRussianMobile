package com.example.inrussian.root.main.train

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arkivanov.decompose.value.MutableValue
import com.example.inrussian.components.main.train.CourseWithSections
import com.example.inrussian.components.main.train.Section
import com.example.inrussian.components.main.train.ShortCourse
import com.example.inrussian.components.main.train.ThemeMeta
import com.example.inrussian.components.main.train.TrainCoursesListComponent
import com.example.inrussian.components.main.train.TrainCoursesState
import com.example.inrussian.root.main.profile.LabelText
import com.example.inrussian.ui.theme.LightGrey
import com.example.inrussian.ui.theme.Orange
import com.example.inrussian.ui.theme.Yellow
import com.example.inrussian.ui.theme.lightCircleGreen
import inrussian.composeapp.generated.resources.Res
import inrussian.composeapp.generated.resources.main
import org.jetbrains.compose.resources.stringResource

@Composable
fun TrainCoursesScreen(component: TrainCoursesListComponent) {
    val state by component.state.subscribeAsState()

    if (state.isLoading) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }


    LazyColumn(
        Modifier
            .background(LightGrey)
            .padding(horizontal = 16.dp)
            .fillMaxSize()
    ) {
        item {
            Spacer(Modifier.height(50.dp))
            LabelText(stringResource(Res.string.main))
            Spacer(Modifier.height(50.dp))
        }

        items(state.courses) {
            val color = when {
                it.course.percent < 30 -> Orange
                it.course.percent < 85 -> Yellow
                else -> lightCircleGreen
            }
            Column(
                Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(White)
                    .border(5.dp, color, RoundedCornerShape(12.dp))
                    .padding(18.dp)
            ) {
                Row {
                    Text(
                        it.course.title,
                        fontWeight = FontWeight.SemiBold,
                        color = color,
                        fontSize = 22.sp
                    )
                    Spacer(Modifier.weight(1f))
                    Text(
                        it.course.percent.toString() + "%",
                        fontWeight = FontWeight.SemiBold,
                        color = color,
                        fontSize = 22.sp
                    )
                }
                val rows = it.sections.chunked(3)

                rows.forEachIndexed { rowIndex, rowItems ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        for (i in 0 until 3) {
                            val item = rowItems.getOrNull(i)
                            if (item != null) {
                                TrainCardItem(
                                    item,
                                    onClick = { component.onSectionClick(it.id) },
                                    Modifier.weight(1f)
                                )
                            } else {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }

                    if (rowIndex != rows.lastIndex) {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun TrainCardItem(
    courseSection: Section,
    onClick: (Section) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp)
            .border(
                3.dp,
                if (courseSection.progressPercent == 100) lightCircleGreen else Color.Transparent,
                RoundedCornerShape(12.dp)
            )
            .padding(vertical = 10.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick(courseSection) },
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Box {

            val color = when {
                courseSection.progressPercent < 35 -> Orange
                courseSection.progressPercent < 85 -> Yellow
                else -> lightCircleGreen

            }
            CircularProgressIndicator(
                progress = { courseSection.progressPercent.toFloat() / 100f },
                color = color,
                trackColor = color.copy(0.5f),
                modifier = Modifier.size(60.dp)
            )
            Text(
                courseSection.progressPercent.toString() + "%",
                Modifier.align(Alignment.Center),
                color = color
            )
        }
        Spacer(Modifier.height(8.dp))
        if (courseSection.title.isNotBlank())
            Text(courseSection.title)
    }
}

class TrainCoursesScreen : TrainCoursesListComponent {
    override val state = MutableValue(
        TrainCoursesState(
            isLoading = false, courses = listOf(
                CourseWithSections(
                    course = ShortCourse(id = "", title = "Курс на патент", 85f),
                    sections = listOf(
                        Section(
                            id = "",
                            courseId = "",
                            title = "Транспорт",
                            totalTheory = 1,
                            totalPractice = 1,
                            completedTheory = 1,
                            completedPractice = 1,
                            themes = listOf(ThemeMeta(id = "", 3, 4, 5))
                        ), Section(
                            id = "",
                            courseId = "",
                            title = "Транспорт",
                            totalTheory = 10,
                            totalPractice = 10,
                            completedTheory = 5,
                            completedPractice = 10,
                            themes = listOf(ThemeMeta(id = "", 3, 4, 5))
                        ),
                        Section(
                            id = "",
                            courseId = "",
                            title = "Инструкция",
                            totalTheory = 10,
                            totalPractice = 10,
                            completedTheory = 5,
                            completedPractice = 10,
                            themes = listOf(ThemeMeta(id = "", 3, 4, 5))
                        ), Section(
                            id = "",
                            courseId = "",
                            title = "",
                            totalTheory = 10,
                            totalPractice = 10,
                            completedTheory = 5,
                            completedPractice = 10,
                            themes = listOf(ThemeMeta(id = "", 3, 4, 5))
                        ), Section(
                            id = "",
                            courseId = "",
                            title = "",
                            totalTheory = 10,
                            totalPractice = 10,
                            completedTheory = 5,
                            completedPractice = 10,
                            themes = listOf(ThemeMeta(id = "", 3, 4, 5))
                        )
                    )
                )
            )
        )
    )

    override fun onSectionClick(sectionId: String) {
        TODO("Not yet implemented")
    }

    override fun refresh() {
        TODO("Not yet implemented")
    }


    @Composable
    @Preview(showBackground = true, showSystemUi = true)
    fun Preview() {
        TrainCoursesScreen(this)
    }


}
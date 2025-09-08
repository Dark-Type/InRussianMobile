package com.example.inrussian.root.main.home

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arkivanov.decompose.value.MutableValue
import com.example.inrussian.components.main.home.CourseModel
import com.example.inrussian.components.main.home.CoursesListComponent
import com.example.inrussian.components.main.home.CoursesListState
import com.example.inrussian.ui.theme.Orange
import inrussian.composeapp.generated.resources.Res
import inrussian.composeapp.generated.resources.course_for_you
import inrussian.composeapp.generated.resources.courses
import inrussian.composeapp.generated.resources.recommended_image_mock
import com.example.inrussian.ui.theme.LocalExtraColors
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun CoursesListComponentUi(component: CoursesListComponent) {
    val currentColors = LocalExtraColors.current
    val state by component.state.subscribeAsState()

    LazyColumn(
        Modifier
            .fillMaxSize()
            .background(currentColors.secondaryBackground)
            .padding(horizontal = 16.dp)
    ) {
        item {
            Spacer(Modifier.height(16.dp))
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        "Главная",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = currentColors.fontCaptive
                    )
                    Text(
                        "Запишитесь на курс или продолжите обучение",
                        fontSize = 14.sp,
                        color = currentColors.fontInactive
                    )
                }
            }
        }

        item {
            Spacer(Modifier.height(24.dp))
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {

                    Text(
                        stringResource(Res.string.course_for_you),
                        color = currentColors.fontCaptive,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
                    )
                }


            }
        }

        item {
            Spacer(Modifier.height(12.dp))
        }

        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(horizontal = 4.dp)
            ) {
                items(state.recommended) { course ->
                    RecommendedCourseItem(course) {
                        component.onRecommendedCourseClick(it.id)
                    }
                }
            }
        }

        item {
            Spacer(Modifier.height(24.dp))
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {

                    Text(
                        stringResource(Res.string.courses),
                        color = currentColors.fontCaptive,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
                    )
                }

                if (state.enrolled.isNotEmpty()) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = currentColors.fontInactive.copy(alpha = 0.1f)
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            "${state.enrolled.size} активных",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            color = currentColors.fontInactive,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        item {
            Spacer(Modifier.height(12.dp))
        }

        if (state.enrolled.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = currentColors.secondaryBackground),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = currentColors.fontInactive.copy(alpha = 0.5f),
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "Вы еще не записаны ни на один курс",
                            color = currentColors.fontInactive,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            "Выберите курс и начните обучение прямо сейчас",
                            color = currentColors.fontInactive,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        } else {
            items(state.enrolled, key = { it.id }) { course ->
                EnrolledCourseItem(course) {
                    component.onEnrolledCourseClick(it.id)
                }
                Spacer(Modifier.height(8.dp))
            }
        }

        item {
            Spacer(Modifier.height(24.dp))
        }
    }
}


@Composable
fun RecommendedCourseItem(course: CourseModel, onClick: (CourseModel) -> Unit) {
    val containerWidth = 180.dp
    val containerHeight = 135.dp
    val labelWidth = 160.dp
    val labelHeight = 65.dp
    val triangleSize = 10.dp

    Box(
        modifier = Modifier
            .size(containerWidth + 20.dp, containerHeight + 20.dp)
            .clickable { onClick(course) }
    ) {
        Card(
            modifier = Modifier
                .align(Alignment.Center)
                .size(containerWidth, containerHeight),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Image(
                    painterResource(Res.drawable.recommended_image_mock),
                    contentDescription = "",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(y = 50.dp)
        ) {
            Box(
                modifier = Modifier
                    .width(labelWidth)
                    .height(labelHeight)
                    .clip(
                        RoundedCornerShape(
                            topStart = 10.dp,
                            topEnd = 10.dp,
                            bottomEnd = 10.dp,
                            bottomStart = 0.dp
                        )
                    )
                    .background(Orange)
            ) {
                Column(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 8.dp, end = 16.dp)
                ) {
                    Text(
                        course.name,
                        color = White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(Modifier.height(4.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "${(5..15).random()} тем",
                            color = White.copy(alpha = 0.9f),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Canvas(
                modifier = Modifier
                    .size(triangleSize)
                    .align(Alignment.Start)
                    .offset(y = (-0.5).dp)
            ) {
                val path = Path().apply {
                    moveTo(0f, 0f)
                    lineTo(size.width, 0f)
                    lineTo(size.width, size.height)
                    close()
                }
                drawPath(path, color = Color(0xFF901C00))
            }
        }
    }
}

@Composable
fun EnrolledCourseItem(course: CourseModel, onClick: (CourseModel) -> Unit) {
    val progress = (25..95).random()
    val completedTasks = (progress * 0.3f).toInt()
    val totalTasks = (completedTasks * 1.5f).toInt()
    val streakDays = (1..14).random()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(course) },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painterResource(Res.drawable.recommended_image_mock),
                contentDescription = "",
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = course.name,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        color = Color.Black,
                        modifier = Modifier.weight(1f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                }

                Text(
                    text = "IDO",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 2.dp)
                )

                Spacer(Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Прогресс",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = "$progress%",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .padding(top = 4.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(Orange.copy(alpha = 0.2f))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(progress / 100f)
                            .clip(RoundedCornerShape(3.dp))
                            .background(Orange)
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row {
                        Text(
                            text = "$completedTasks/$totalTasks задач",
                            fontSize = 11.sp,
                            color = Color.Gray
                        )
                    }

                    if (streakDays > 0) {
                        Row(verticalAlignment = Alignment.CenterVertically) {

                            Text(
                                text = "$streakDays дней подряд",
                                fontSize = 11.sp,
                                color = Orange,
                                modifier = Modifier.padding(start = 2.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

class CoursesListComponentUi : CoursesListComponent {

    @Composable
    @Preview(showBackground = true, showSystemUi = true)
    fun Preview() {
        CoursesListComponentUi(this)
    }

    override val state = MutableValue(
        CoursesListState(
            recommended = listOf(CourseModel(name = "通过工作考试的培训 / Курс на патент")),
            enrolled = listOf(CourseModel(name = "通过工作考试的培训 / Курс на патент")),
            isLoading = false
        )
    )

    override fun onRecommendedCourseClick(courseId: String) {
        TODO("Not yet implemented")
    }

    override fun onEnrolledCourseClick(courseId: String) {
        TODO("Not yet implemented")
    }

}
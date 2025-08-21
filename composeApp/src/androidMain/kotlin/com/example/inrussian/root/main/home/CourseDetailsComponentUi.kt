package com.example.inrussian.root.main.home

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arkivanov.decompose.value.MutableValue
import com.example.inrussian.components.main.home.Course
import com.example.inrussian.components.main.home.CourseDetailsComponent
import com.example.inrussian.components.main.home.CourseDetailsState
import com.example.inrussian.components.main.home.CourseSection
import com.example.inrussian.ui.theme.LightGrey
import com.example.inrussian.ui.theme.Orange
import inrussian.composeapp.generated.resources.Res
import inrussian.composeapp.generated.resources.author
import inrussian.composeapp.generated.resources.back_arrow
import inrussian.composeapp.generated.resources.enroll
import inrussian.composeapp.generated.resources.post
import inrussian.composeapp.generated.resources.profile
import inrussian.composeapp.generated.resources.recommended_image_mock
import inrussian.composeapp.generated.resources.y_enrolled
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun CourseDetailsComponentUi(component: CourseDetailsComponent) {
    val state by component.state.subscribeAsState()
    val course = state.course
    LazyColumn(
        Modifier
            .background(LightGrey)
            .fillMaxSize()
    ) {
        item() {
            Box(Modifier.fillMaxWidth()) {
                Icon(
                    painterResource(Res.drawable.recommended_image_mock),
                    "",
                    Modifier
                        .height(height = 350.dp)
                        .fillMaxWidth()
                )
                IconButton(
                    component::onBack,
                    Modifier
                        .padding(start = 20.dp)
                        .clip(CircleShape)
                        .size(40.dp)
                        .background(Orange)
                ) {
                    Icon(
                        vectorResource(Res.drawable.back_arrow), "", tint = White

                    )
                }
                TextButton(
                    component::toggleEnroll,
                    colors = ButtonDefaults.buttonColors().copy(containerColor = Orange),
                    modifier = Modifier
                        .align(
                            Alignment.TopEnd
                        )
                        .padding(end = 16.dp)

                ) { Text(stringResource(if (state.isEnrolled) Res.string.y_enrolled else Res.string.enroll)) }
                Text(
                    state.course?.name ?: "",
                    modifier = Modifier
                        .padding(bottom = 50.dp, end = 100.dp)
                        .clip(RoundedCornerShape(bottomEnd = 10.dp, topEnd = 10.dp))
                        .background(Orange)
                        .align(Alignment.BottomStart),
                    color = White,
                    fontSize = 32.sp,
                )
            }
        }

        item {
            Row(Modifier.padding(horizontal = 16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier

                        .clip(
                            CircleShape
                        )
                        .background(White)
                        .padding(horizontal = 14.dp, vertical = 7.dp),
                ) {
                    Icon(
                        vectorResource(Res.drawable.profile),
                        "",
                        tint = Orange,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        stringResource((Res.string.author)) + ": ${state.course?.authorUrl}",
                        fontWeight = FontWeight.W500
                    )
                }
                Spacer(Modifier.weight(1f))
                Text(
                    stringResource(Res.string.post) + " ${state.course?.createdAt}",
                    modifier = Modifier
                        .clip(
                            CircleShape
                        )
                        .background(White)
                        .padding(horizontal = 14.dp, vertical = 9.dp),
                    fontWeight = FontWeight.W500
                )
            }
        }
        item {
            Spacer(Modifier.height(12.dp))
        }
        items(state.sections) {
            Spacer(Modifier.height(12.dp))
            CourseItem(it, state.isEnrolled)
        }
        item {
            Spacer(Modifier.height(12.dp))
        }
    }

}

@Composable
fun CourseItem(course: CourseSection, showProgress: Boolean) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(White)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row {
            Text(course.title, fontSize = 20.sp, fontWeight = FontWeight.W500)
            Spacer(Modifier.weight(1f))
            Text(
                "${course.completedLessons.toFloat() / course.totalLessons.toFloat() * 100} %",
                fontSize = 20.sp, fontWeight = FontWeight.W500
            )
        }
        if (showProgress) {
            Spacer(Modifier.height(16.dp))
            LinearProgressIndicator(
                progress = { course.completedLessons.toFloat() / course.totalLessons.toFloat() },
                modifier = Modifier.fillMaxWidth(),
                color = Orange,
                trackColor = Orange.copy(0.5f)
            )
        }
    }
}


class CourseDetailsComponentUi() : CourseDetailsComponent {
    override val courseId: String = ""
    override val state = MutableValue(
        CourseDetailsState(
            course = Course(name = "通过工作考试的培训 / Курс на патент", authorId = "Ido"),
            isEnrolled = true,
            sections = listOf(CourseSection("", "Инструкция", 100, 50)),
            progressPercent = 0,
            isLoading = false
        )
    )

    override fun toggleEnroll() {
        TODO("Not yet implemented")
    }

    override fun onBack() {
        TODO("Not yet implemented")
    }

    override fun showInfo() {
        TODO("Not yet implemented")
    }

    override fun signUp() {
        TODO("Not yet implemented")
    }

    @Preview(showBackground = true, showSystemUi = true)
    @Composable
    fun Preview() {
        CourseDetailsComponentUi(this)
    }

}
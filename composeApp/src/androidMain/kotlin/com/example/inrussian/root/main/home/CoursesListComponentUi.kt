package com.example.inrussian.root.main.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arkivanov.decompose.value.MutableValue
import com.example.inrussian.components.main.home.CourseModel
import com.example.inrussian.components.main.home.CoursesListComponent
import com.example.inrussian.components.main.home.CoursesListState
import com.example.inrussian.root.main.profile.LabelText
import com.example.inrussian.ui.theme.DarkGrey
import com.example.inrussian.ui.theme.LightGrey
import com.example.inrussian.ui.theme.Orange
import inrussian.composeapp.generated.resources.Res
import inrussian.composeapp.generated.resources.author
import inrussian.composeapp.generated.resources.course_for_you
import inrussian.composeapp.generated.resources.courses
import inrussian.composeapp.generated.resources.main
import inrussian.composeapp.generated.resources.recommended_image_mock
import nekit.corporation.shift_app.ui.theme.LocalExtraColors
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun CoursesListComponentUi(component: CoursesListComponent) {
    val currentColors = LocalExtraColors.current
    val state by component.state.subscribeAsState()

    LazyColumn(
        Modifier
            .fillMaxSize()
            .background(currentColors.baseBackground)
            .padding(horizontal = 16.dp)
    ) {
        item {
            Spacer(Modifier.height(50.dp))
        }
        item {
            LabelText(stringResource(Res.string.main))
        }
        item {
            Spacer(Modifier.height(50.dp))
        }
        item {
            Text(
                stringResource(Res.string.course_for_you),
                color = currentColors.fontInactive,
                modifier = Modifier.padding(bottom = 8.dp),
                fontWeight = FontWeight.W400
            )
        }
        item {
            LazyRow {
                items(state.recommended) {
                    RecommendedCourseItem(it) { component.onRecommendedCourseClick(it.id) }
                }
            }
        }
        item {
            Spacer(Modifier.height(50.dp))
        }
        item {
            Text(
                stringResource(Res.string.courses),
                color = currentColors.fontInactive,
                modifier = Modifier.padding(bottom = 8.dp),
                fontWeight = FontWeight.W400
            )
        }
        items(state.enrolled) {
            CourseItem(it) { component.onEnrolledCourseClick(it.id) }
        }
    }

}

@Composable
fun RecommendedCourseItem(course: CourseModel, onClick: (CourseModel) -> Unit) {
    Box(
        Modifier
            .padding(start = 10.dp)
            .size(180.dp, 135.dp)
            .clickable {
                onClick(course)
            }
    ) {
        Image(
            painterResource(Res.drawable.recommended_image_mock),
            "",
            Modifier.size(170.dp, 135.dp),
            contentScale = ContentScale.Crop
        )
        Text(
            course.name,
            Modifier
                .offset((-10).dp)
                .padding(bottom = 24.dp, end = 16.dp)
                .clip(RoundedCornerShape(10.dp, 10.dp, 10.dp))
                .background(Orange)
                .align(Alignment.BottomStart)
                .padding(top = 8.dp, bottom = 16.dp)
                .padding(horizontal = 6.dp),
            color = White
        )
    }
}

@Composable
fun CourseItem(course: CourseModel, onClick: (CourseModel) -> Unit) {
    Row(
        Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(White)
            .fillMaxWidth()
            .clickable { onClick(course) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painterResource(Res.drawable.recommended_image_mock),
            "",
            Modifier
                .padding(4.dp)
                .size(50.dp)
                .clip(RoundedCornerShape(10.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(Modifier.width(12.dp))

        Column {
            Text(course.name, fontSize = 9.sp)
            Spacer(Modifier.height(12.dp))
            Text(
                stringResource(Res.string.author) + ": ${course.authorId}",
                fontSize = 8.sp,
                color = DarkGrey.copy(0.8f)
            )

        }
        Spacer(Modifier.weight(1f))
        Box(Modifier.padding(end = 12.dp)) {
            CircularProgressIndicator(
                progress = { 0f },
                trackColor = Orange.copy(0.3f),
                color = Orange
            )
            Text("0 %", Modifier.align(Alignment.Center), fontSize = 10.sp, color = Orange)
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
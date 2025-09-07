package com.example.inrussian.root.main.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arkivanov.decompose.value.MutableValue
import com.example.inrussian.components.main.home.CourseDetailsComponent
import com.example.inrussian.components.main.home.CourseDetailsState
import com.example.inrussian.components.main.home.CourseModel
import com.example.inrussian.components.main.home.CourseSection
import com.example.inrussian.ui.theme.Orange
import inrussian.composeapp.generated.resources.Res
import inrussian.composeapp.generated.resources.author
import inrussian.composeapp.generated.resources.back
import inrussian.composeapp.generated.resources.back_arrow
import inrussian.composeapp.generated.resources.enroll
import inrussian.composeapp.generated.resources.post
import inrussian.composeapp.generated.resources.recommended_image_mock
import inrussian.composeapp.generated.resources.y_enrolled
import com.example.inrussian.ui.theme.LocalExtraColors
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import kotlin.math.roundToInt

@Composable
fun CourseDetailsComponentUi(component: CourseDetailsComponent) {
    val state by component.state.subscribeAsState()
    val course = state.course
    val currentColors = LocalExtraColors.current

    // Map old sections -> themes (transition layer).
    val themes = remember(state.sections) { state.sections } // Replace with state.themes if you add it later.

    val totalThemes = themes.size
    val totalThemeLessons = themes.sumOf { it.totalLessons }
    val completedThemeLessons = themes.sumOf { it.completedLessons }
    val progressFraction =
        if (totalThemeLessons == 0) 0f else (completedThemeLessons.toFloat() / totalThemeLessons.toFloat()).coerceIn(0f, 1f)
    val progressPercent = (progressFraction * 100).roundToInt()

    val enrolled = state.isEnrolled

    // Achievements only meaningful when enrolled.
    val achievements = remember(course?.id, enrolled, progressPercent) {
        if (!enrolled) emptyList() else listOf(
            AchievementUi("first_steps", "First Step", "Completed first theme", progressPercent > 0),
            AchievementUi("steady", "Consistency", "Active 3 days", progressPercent >= 10),
            AchievementUi("halfway", "Halfway", "Reached 50% progress", progressPercent >= 50),
            AchievementUi("closer", "Closer", "Reached 80% progress", progressPercent >= 80),
            AchievementUi("finisher", "Finisher", "Completed 100%", progressPercent >= 100)
        )
    }

    val scrollState = rememberScrollState()

    Column(
        Modifier
            .fillMaxSize()
            .background(currentColors.baseBackground)
            .verticalScroll(scrollState)
    ) {
        HeroImageWithOverlay(
            title = course?.name.orEmpty(),
            onBack = component::onBack,
            onEnrollToggle = component::toggleEnroll,
            isEnrolled = enrolled
        )

        Spacer(Modifier.height(20.dp))

        // Author & Meta Card
        AuthorMetaBlock(
            authorId = course?.authorId,
            createdAt = course?.createdAt,
            totalThemes = totalThemes,
            totalLessons = totalThemeLessons
        )

        // Description
        if (!course?.description.isNullOrBlank()) {
            Text(
                text = course.description ?: "Описание курса будет здесь",
                fontSize = 14.sp,
                lineHeight = 20.sp,
                color = currentColors.fontCaptive.copy(alpha = 0.85f),
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 18.dp)
            )
        }

        // Enroll button if NOT enrolled (we also keep top hero action for quick CTA)
        if (!enrolled) {
            Spacer(Modifier.height(24.dp))
            Button(
                onClick = component::toggleEnroll,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Orange)
            ) {
                Text(
                    stringResource(Res.string.enroll),
                    fontWeight = FontWeight.SemiBold,
                    color = White,
                    fontSize = 16.sp
                )
            }
        }

        // Progress & Achievements only if enrolled
        if (enrolled) {
            Spacer(Modifier.height(28.dp))
            ProgressCardCompact(
                progressFraction = progressFraction,
                progressPercent = progressPercent,
                completedLessons = completedThemeLessons,
                totalLessons = totalThemeLessons
            )

            if (achievements.isNotEmpty()) {
                Spacer(Modifier.height(26.dp))
                AchievementGrid(achievements)
            }
        }

        if (themes.isNotEmpty()) {
            Spacer(Modifier.height(32.dp))
            Text(
                text = "Темы курса",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = currentColors.fontCaptive,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(Modifier.height(8.dp))
            themes.forEach { theme ->
                ThemeItem(
                    theme = theme,
                    showProgress = enrolled,
                )
                Spacer(Modifier.height(10.dp))
            }
        }

        Spacer(Modifier.height(90.dp))
    }
}

/* ----------------------- HERO ----------------------- */

@Composable
private fun HeroImageWithOverlay(
    title: String,
    onBack: () -> Unit,
    onEnrollToggle: () -> Unit,
    isEnrolled: Boolean
) {
    Box(
        Modifier
            .fillMaxWidth()
            .heightIn(min = 300.dp)
            .aspectRatio(402f / 331f)
    ) {
        // Hero image
        Image(
            painterResource(Res.drawable.recommended_image_mock),
            contentDescription = "",
            modifier = Modifier
                .fillMaxSize()
                .clip(
                    RoundedCornerShape(
                        bottomStart = 28.dp,
                        bottomEnd = 28.dp
                    )
                ),
            contentScale = ContentScale.Crop
        )

        // Gradient overlay
        Box(
            Modifier
                .matchParentSize()
                .background(
                    Brush.verticalGradient(
                        0f to Color.Black.copy(alpha = 0.45f),
                        0.35f to Color.Transparent,
                        0.75f to Color.Black.copy(alpha = 0.65f),
                        1f to Color.Black.copy(alpha = 0.85f)
                    )
                )
        )

        // Top Controls
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                onClick = onBack,
                shape = CircleShape,
                color = Orange,
                shadowElevation = 4.dp,
                modifier = Modifier.size(44.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        painterResource(Res.drawable.back_arrow),
                        contentDescription = stringResource(Res.string.back),
                        tint = White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            FilledTonalButton(
                onClick = onEnrollToggle,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = if (isEnrolled) Color(0xFF276749) else Orange,
                    contentColor = White
                )
            ) {
                Text(
                    text = stringResource(if (isEnrolled) Res.string.y_enrolled else Res.string.enroll),
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        // Title overlay (Orange pill) - explicitly requested
        if (title.isNotBlank()) {
            Text(
                text = title,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 20.dp, bottom = 28.dp, end = 60.dp)
                    .clip(RoundedCornerShape(topEnd = 14.dp, bottomEnd = 14.dp))
                    .background(Orange)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                color = White,
                fontSize = 30.sp,
                lineHeight = 36.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

/* --------------------- AUTHOR META ------------------ */

@Composable
private fun AuthorMetaBlock(
    authorId: String?,
    createdAt: String?,
    totalThemes: Int,
    totalLessons: Int
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White)
            .padding(horizontal = 18.dp, vertical = 14.dp)
            .fillMaxWidth()
    ) {
        // Avatar mock
        Box(
            Modifier
                .size(54.dp)
                .clip(CircleShape)
                .background(Orange.copy(0.18f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = (authorId ?: "?").take(1).uppercase(),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Orange
            )
        }
        Spacer(Modifier.width(16.dp))
        Column(Modifier.weight(1f)) {
            Text(
                text = stringResource(Res.string.author) + ": " + (authorId?.take(12) ?: "-"),
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF222222)
            )
            if (!createdAt.isNullOrBlank()) {
                Text(
                    text = stringResource(Res.string.post) + " " + createdAt.take(10),
                    fontSize = 12.sp,
                    color = Color(0xFF666666)
                )
            }
        }
        Column(horizontalAlignment = Alignment.End) {
            MetaBadge("$totalThemes тем")
            Spacer(Modifier.height(6.dp))
            MetaBadge("$totalLessons уроков")
        }
    }
}

@Composable
private fun MetaBadge(text: String) {
    Box(
        Modifier
            .background(Orange.copy(alpha = 0.12f), RoundedCornerShape(50))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(text, fontSize = 11.sp, fontWeight = FontWeight.Medium, color = Orange)
    }
}

/* ------------------- PROGRESS CARD ------------------ */

@Composable
private fun ProgressCardCompact(
    progressFraction: Float,
    progressPercent: Int,
    completedLessons: Int,
    totalLessons: Int
) {
    val currentColors = LocalExtraColors.current
    Column(
        Modifier
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .padding(horizontal = 18.dp, vertical = 20.dp)
    ) {
        Text(
            "Прогресс",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF222222)
        )
        Spacer(Modifier.height(14.dp))

        // Custom progress bar without trailing dot at 0%
        Box(
            Modifier
                .fillMaxWidth()
                .height(14.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Orange.copy(alpha = 0.18f))
        ) {
            if (progressFraction > 0f) {
                Box(
                    Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(progressFraction)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Orange)
                )
            }
        }
        Spacer(Modifier.height(10.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "$progressPercent%",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Orange
            )
            Spacer(Modifier.width(12.dp))
            Text(
                "$completedLessons / $totalLessons уроков завершено",
                fontSize = 12.sp,
                color = Color(0xFF666666)
            )
        }

        Spacer(Modifier.height(18.dp))

        // Mini stats (mocked)
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ProgressMiniStat("Серия", value = mockStreak(progressPercent).toString())
            ProgressMiniStat("Точность", value = "${mockAccuracy(progressPercent)}%")
            ProgressMiniStat(
                "Осталось",
                value = (totalLessons - completedLessons).coerceAtLeast(0).toString()
            )
        }
    }
}

@Composable
private fun ProgressMiniStat(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF222222))
        Text(label, fontSize = 11.sp, color = Color(0xFF777777))
    }
}

/* ------------------ ACHIEVEMENT GRID ---------------- */

private data class AchievementUi(
    val id: String,
    val title: String,
    val description: String,
    val unlocked: Boolean
)

@Composable
private fun AchievementGrid(achievements: List<AchievementUi>) {
    Column(
        Modifier
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .padding(18.dp)
    ) {
        Text(
            "Достижения",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF222222)
        )
        Spacer(Modifier.height(14.dp))

        // FlowRow ensures at least 2 per row if width permits
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            maxItemsInEachRow = 2
        ) {
            achievements.forEach { ach ->
                AchievementCard(ach)
            }
        }
    }
}

@Composable
private fun AchievementCard(achievement: AchievementUi) {
    val gradient = if (achievement.unlocked) {
        Brush.verticalGradient(
            listOf(
                Color(0xFFFFF2D1),
                Color(0xFFFFDF9E)
            )
        )
    } else {
        Brush.verticalGradient(
            listOf(
                Color(0xFFE8E8E8),
                Color(0xFFD4D4D4)
            )
        )
    }
    val borderColor = if (achievement.unlocked) Color(0xFFFFB347) else Color(0xFFB5B5B5)
    val icon = when (achievement.id) {
        "first_steps" -> if (achievement.unlocked) "🎯" else "🔒"
        "steady" -> if (achievement.unlocked) "🔥" else "🔒"
        "halfway" -> if (achievement.unlocked) "⚡" else "🔒"
        "closer" -> if (achievement.unlocked) "🚀" else "🔒"
        "finisher" -> if (achievement.unlocked) "🏆" else "🔒"
        else -> if (achievement.unlocked) "⭐" else "🔒"
    }

    Column(
        modifier = Modifier
            .width(150.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.Transparent)
            .border(1.dp, borderColor, RoundedCornerShape(16.dp))
            .drawBehind {
                drawRoundRect(
                    brush = gradient,
                    cornerRadius = CornerRadius(32f, 32f)
                )
            }
            .padding(horizontal = 12.dp, vertical = 14.dp)
    ) {
        Text(icon, fontSize = 28.sp)
        Spacer(Modifier.height(4.dp))
        Text(
            achievement.title,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = Color(0xFF222222)
        )
        Text(
            achievement.description,
            fontSize = 11.sp,
            lineHeight = 13.sp,
            color = Color(0xFF444444),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = 2.dp)
        )
        if (achievement.unlocked) {
            Text(
                "Открыто",
                fontSize = 10.sp,
                color = Color(0xFFB14A00),
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

/* --------------------- THEME ITEM ------------------- */

@Composable
private fun ThemeItem(theme: CourseSection, showProgress: Boolean) {
    val currentColors = LocalExtraColors.current
    Column(
        Modifier
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 14.dp)
            .fillMaxWidth()
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                theme.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = currentColors.fontCaptive,
                modifier = Modifier.weight(1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (showProgress) {
                val f =
                    if (theme.totalLessons == 0) 0f else theme.completedLessons.toFloat() / theme.totalLessons
                Text(
                    "${(f * 100).roundToInt()}%",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Orange
                )
            }
        }
        if (showProgress) {
            Spacer(Modifier.height(10.dp))
            val prog =
                if (theme.totalLessons == 0) 0f else theme.completedLessons.toFloat() / theme.totalLessons
            // Custom bar to avoid trailing dot for very small or zero
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Orange.copy(alpha = 0.25f))
            ) {
                if (prog > 0f) {
                    Box(
                        Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(prog)
                            .clip(RoundedCornerShape(4.dp))
                            .background(Orange)
                    )
                }
            }
            Spacer(Modifier.height(6.dp))
            Text(
                "${theme.completedLessons}/${theme.totalLessons} уроков",
                fontSize = 11.sp,
                color = Color(0xFF666666)
            )
        }
    }
}

/* ------------------ MOCK HELPERS -------------------- */

private fun mockStreak(progressPercent: Int): Int =
    when {
        progressPercent >= 80 -> 12
        progressPercent >= 50 -> 7
        progressPercent >= 30 -> 4
        progressPercent > 0 -> 2
        else -> 0
    }

private fun mockAccuracy(progressPercent: Int): Int =
    (82 + (progressPercent / 6)).coerceIn(78, 97)

@Preview(name = "CourseDetailsComponentUiPreview", showBackground = true)
@Composable
fun CourseDetailsComponentUiPreview() {
    val mockState = MutableValue(
        CourseDetailsState(
            course = CourseModel(name = "Курс на патент, прям оч большое название", description = "Большое описание курса, почему Вам стоит записаться, и прочее"),
            isEnrolled = true,
            sections = emptyList(),
            progressPercent = 20,
            isLoading = false
        )
    )
    val mockComponent = object : CourseDetailsComponent {
        override val courseId: String = "preview"
        override val state = mockState
        override fun toggleEnroll() {}
        override fun onBack() {}
        override fun showInfo() {}
        override fun signUp() {}
    }
    CourseDetailsComponentUi(mockComponent)
}

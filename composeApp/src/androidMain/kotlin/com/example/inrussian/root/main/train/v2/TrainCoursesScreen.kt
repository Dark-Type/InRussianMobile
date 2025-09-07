package com.example.inrussian.root.main.train.v2

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arkivanov.decompose.value.Value
import com.example.inrussian.components.main.train.CourseUiModel
import com.example.inrussian.components.main.train.ThemeModel
import com.example.inrussian.components.main.train.TrainCoursesListComponent
import com.example.inrussian.components.main.train.TrainCoursesState
import com.example.inrussian.ui.theme.Orange
import com.example.inrussian.ui.theme.Yellow
import com.example.inrussian.ui.theme.lightCircleGreen
import com.example.inrussian.ui.theme.LocalExtraColors
import kotlin.math.roundToInt


data class TrainCoursesState(
    val isLoading: Boolean = false,
    val courses: List<CourseUiModel> = emptyList(),
    val error: String? = null
)

///* Provided externally */
//data class ThemeModel(
//    val id: String,
//    val title: String,
//    val description: String? = null,
//    val childThemes: List<ThemeModel> = emptyList(),
//    val solvedTasks: Int,
//    val totalTasks: Int,
//    val isLeaf: Boolean
//) {
//    val completionFraction: Float
//        get() = if (totalTasks == 0) 0f else (solvedTasks.toFloat() / totalTasks.toFloat()).coerceIn(0f, 1f)
//}

data class CourseUiModel(
    val id: String,
    val title: String,
    val percent: Int,
    val totalTasks: Int,
    val solvedTasks: Int,
    val themes: List<ThemeModel>
)

interface TrainCoursesListComponent {
    val state: Value<TrainCoursesState>
    fun onRefresh()
    fun onThemeClick(courseId: String, themePath: List<String>)
    fun onResumeCourse(courseId: String)
}

/* Colors from your existing theme (ExtraColors) */
val Orange = Color(0xFFFF7A00)
val Yellow = Color(0xFFF7C843)
val lightCircleGreen = Color(0xFF3BB273)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrainCoursesScreen(component: TrainCoursesListComponent) {
    val state by component.state.subscribeAsState()
    val colors = LocalExtraColors.current
    val listState = rememberLazyListState()

    val expandedCourses = remember { mutableStateMapOf<String, Boolean>() }
    val expandedThemes = remember { mutableStateMapOf<String, Boolean>() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Training", fontWeight = FontWeight.SemiBold)
                        Text(
                            "${state.courses.size} course${if (state.courses.size == 1) "" else "s"}",
                            style = MaterialTheme.typography.labelSmall,
                            color = colors.fontInactive
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { component.onRefresh() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                    }
                }
            )
        },
        containerColor = colors.baseBackground
    ) { padding ->
        when {
            state.isLoading && state.courses.isEmpty() ->
                LoadingState(Modifier.padding(padding).fillMaxSize())

            state.error != null && state.courses.isEmpty() ->
                ErrorState(
                    error = state.error,
                    onRetry = component::onRefresh,
                    modifier = Modifier.padding(padding).fillMaxSize()
                )

            else -> {
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize()
                        .background(colors.baseBackground),
                    contentPadding = PaddingValues(bottom = 32.dp, top = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(state.courses, key = { it.id }) { course ->
                        val expanded = expandedCourses[course.id] == true
                        CourseCard(
                            course = course,
                            expanded = expanded,
                            onExpandToggle = {
                                expandedCourses[course.id] = !(expandedCourses[course.id] ?: false)
                            },
                            onResume = { component.onResumeCourse(course.id) },
                            onThemeRowClick = { theme, path ->
                                val key = path.joinToString(">")
                                if (theme.childThemes.isNotEmpty()) {
                                    expandedThemes[key] = !(expandedThemes[key] ?: false)
                                } else {
                                    component.onThemeClick(course.id, path)
                                }
                            },
                            onDeepThemeNavigate = { theme, path ->
                                component.onThemeClick(course.id, path)
                            },
                            expandedThemes = expandedThemes
                        )
                    }
                }
            }
        }
    }
}

/* ---------------- Course Card ---------------- */

@Composable
private fun CourseCard(
    course: CourseUiModel,
    expanded: Boolean,
    onExpandToggle: () -> Unit,
    onResume: () -> Unit,
    onThemeRowClick: (ThemeModel, List<String>) -> Unit,
    onDeepThemeNavigate: (ThemeModel, List<String>) -> Unit,
    expandedThemes: Map<String, Boolean>
) {
    val colors = LocalExtraColors.current
    val accentColor = when {
        course.percent < 30 -> Orange
        course.percent < 85 -> Yellow
        else -> lightCircleGreen
    }
    val rotation by animateFloatAsState(
        targetValue = if (expanded) 90f else 0f,
        animationSpec = tween(250),
        label = "courseArrowRotation"
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .semantics {
                heading()
                contentDescription = "Course ${course.title}, progress ${course.percent}%."
                stateDescription = if (expanded) "Expanded" else "Collapsed"
            },
        shape = RoundedCornerShape(20.dp),
        color = colors.componentBackground,
        tonalElevation = 1.dp,
        border = BorderStroke(1.dp, colors.stroke)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { onExpandToggle() }
                    .padding(bottom = 4.dp)
            ) {
                Box(
                    Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(accentColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        progress = { course.percent / 100f },
                        strokeWidth = 4.dp,
                        color = accentColor,
                        trackColor = accentColor.copy(0.25f),
                        modifier = Modifier.size(40.dp)
                    )
                }
                Spacer(Modifier.width(14.dp))
                Column(Modifier.weight(1f)) {
                    Text(
                        course.title,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp,
                        color = colors.fontCaptive,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        "${course.solvedTasks}/${course.totalTasks} tasks • ${course.percent}%",
                        fontSize = 12.sp,
                        color = colors.fontInactive
                    )
                }
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = null,
                    tint = colors.fontInactive,
                    modifier = Modifier
                        .size(24.dp)
                        .graphicsLayer { rotationZ = rotation }
                )
            }

            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                AssistChip(
                    onClick = onResume,
                    label = { Text("Resume") },
                    leadingIcon = {
                        Icon(Icons.Default.PlayArrow, contentDescription = null, tint = accentColor)
                    },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = accentColor.copy(alpha = 0.12f),
                        labelColor = accentColor
                    )
                )
            }

            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(Modifier.padding(top = 16.dp)) {
                    AnimatedProgressBar(
                        fraction = course.percent / 100f,
                        accent = accentColor,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                    )
                    Spacer(Modifier.height(18.dp))
                    if (course.themes.isEmpty()) {
                        Text(
                            "No themes yet",
                            color = colors.fontInactive,
                            fontSize = 13.sp
                        )
                    } else {
                        course.themes.forEach { theme ->
                            ThemeNode(
                                theme = theme,
                                parentPath = emptyList(),
                                onThemeRowClick = onThemeRowClick,
                                onDeepThemeNavigate = onDeepThemeNavigate,
                                expandedThemes = expandedThemes
                            )
                            Spacer(Modifier.height(12.dp))
                        }
                    }
                }
            }
        }
    }
}

/* ---------------- Theme Node ---------------- */

@Composable
private fun ThemeNode(
    theme: ThemeModel,
    parentPath: List<String>,
    onThemeRowClick: (ThemeModel, List<String>) -> Unit,
    onDeepThemeNavigate: (ThemeModel, List<String>) -> Unit,
    expandedThemes: Map<String, Boolean>,
    depth: Int = 0
) {
    val path = remember(theme.id, parentPath) { parentPath + theme.id }
    val pathKey = remember(path) { path.joinToString(">") }
    val isExpanded = expandedThemes[pathKey] == true
    val hasChildren = theme.childThemes.isNotEmpty()
    val accent = when {
        theme.completionFraction >= 0.95f -> lightCircleGreen
        theme.completionFraction >= 0.50f -> Yellow
        else -> Orange
    }
    val rotation by animateFloatAsState(
        targetValue = if (isExpanded) 90f else 0f,
        animationSpec = tween(220),
        label = "themeArrowRotation"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .drawGuideline(depth)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFFDFDFE))
                .border(
                    1.dp,
                    if (theme.completionFraction >= 1f) accent.copy(0.6f) else Color(0xFFE3E4E8),
                    RoundedCornerShape(12.dp)
                )
                .clickable { onThemeRowClick(theme, path) }
                .padding(horizontal = 12.dp, vertical = 10.dp)
                .semantics {
                    contentDescription =
                        "Theme ${theme.title} progress ${(theme.completionFraction * 100).roundToInt()} percent"
                    stateDescription = if (isExpanded) "Expanded" else "Collapsed"
                }
        ) {
            if (hasChildren) {
                Icon(
                    Icons.Default.KeyboardArrowRight,
                    contentDescription = null,
                    tint = accent,
                    modifier = Modifier
                        .size(20.dp)
                        .graphicsLayer { rotationZ = rotation }
                )
            } else {
                Box(Modifier.size(20.dp), contentAlignment = Alignment.Center) {
                    Canvas(Modifier.size(12.dp)) {
                        drawCircle(
                            color = if (theme.completionFraction >= 1f) accent else accent.copy(alpha = .35f)
                        )
                    }
                }
            }
            Spacer(Modifier.width(10.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    theme.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "${theme.solvedTasks}/${theme.totalTasks}",
                        fontSize = 11.sp,
                        color = Color(0xFF6A6F76)
                    )
                    Text(
                        " • ${(theme.completionFraction * 100).roundToInt()}%",
                        fontSize = 11.sp,
                        color = accent
                    )
                }
            }
            Spacer(Modifier.width(8.dp))
            AnimatedProgressBar(
                fraction = theme.completionFraction,
                accent = accent,
                modifier = Modifier
                    .height(6.dp)
                    .width(70.dp)
            )
        }

        AnimatedVisibility(
            visible = isExpanded && hasChildren,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            Column(Modifier.padding(start = 24.dp, top = 10.dp, end = 4.dp)) {
                if (!theme.description.isNullOrBlank()) {
                    Text(
                        theme.description!!,
                        fontSize = 12.sp,
                        lineHeight = 16.sp,
                        color = Color(0xFF5C6167),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                if (theme.childThemes.isNotEmpty()) {
                    TextButton(
                        onClick = { onDeepThemeNavigate(theme, path) },
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text("Open Theme", fontSize = 12.sp)
                    }
                    Spacer(Modifier.height(6.dp))
                }

                theme.childThemes.forEach { child ->
                    ThemeNode(
                        theme = child,
                        parentPath = path,
                        onThemeRowClick = onThemeRowClick,
                        onDeepThemeNavigate = onDeepThemeNavigate,
                        expandedThemes = expandedThemes,
                        depth = depth + 1
                    )
                    Spacer(Modifier.height(10.dp))
                }
            }
        }
    }
}

private fun Modifier.drawGuideline(depth: Int): Modifier =
    if (depth <= 0) this else drawBehind {
        val stroke = 1.dp.toPx()
        val xOffset = 8.dp.toPx()
        val color = Color(0xFFE2E5EA)
        for (i in 0 until depth) {
            val x = xOffset + (i * 18.dp.toPx())
            drawLine(
                color = color,
                start = Offset(x, 0f),
                end = Offset(x, size.height),
                strokeWidth = stroke
            )
        }
    }

/* ---------------- Progress Bar ---------------- */

@Composable
private fun AnimatedProgressBar(
    fraction: Float,
    accent: Color,
    modifier: Modifier
) {
    val animated by animateFloatAsState(
        targetValue = fraction.coerceIn(0f, 1f),
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "animatedProgress"
    )
    val trackColor = accent.copy(alpha = 0.20f)
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(percent = 50))
            .background(trackColor)
    ) {
        if (animated > 0f) {
            Box(
                Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(animated)
                    .background(
                        brush = Brush.horizontalGradient(
                            listOf(
                                accent.copy(alpha = 0.75f),
                                accent
                            )
                        )
                    )
            )
        }
    }
}

/* ---------------- Loading / Error ---------------- */

@Composable
private fun LoadingState(modifier: Modifier = Modifier) {
    Box(modifier, contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorState(error: String?, onRetry: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier
            .padding(32.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Something went wrong", fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
        Spacer(Modifier.height(12.dp))
        Text(error ?: "Unknown error", color = Color(0xFF5F6368), fontSize = 13.sp, lineHeight = 18.sp)
        Spacer(Modifier.height(20.dp))
        Button(onClick = onRetry) { Text("Retry") }
    }
}
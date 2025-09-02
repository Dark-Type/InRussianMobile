package com.example.inrussian.root.main.train.task

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.example.inrussian.components.main.train.tasks.interfaces.TextInputTaskWithVariantComponent

//@Preview(showBackground = true, showSystemUi = true, backgroundColor = 0xFFEAEAEA)

@Composable
fun TextInputWithVariantTask(
    component: TextInputTaskWithVariantComponent,
    onContinueClick: (() -> Unit) -> Unit
) {
    val state by component.state.subscribeAsState()
    var expandedGapId by remember { mutableStateOf<String?>(null) }

    if (state.blocks != null) {
        Box(Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .background(Color.White)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    state.blocks?.label ?: "",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.align(
                        Alignment.CenterHorizontally
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))

                val words = state.blocks!!.words
                val gaps = state.blocks!!.gaps.sortedBy { it.pos }

                val inlineContent = mutableMapOf<String, InlineTextContent>()
                val annotatedString = buildAnnotatedString {
                    var currentIndex = 0

                    gaps.forEach { gap ->
                        while (currentIndex < gap.pos && currentIndex < words.size) {
                            append(words[currentIndex] + " ")
                            currentIndex++
                        }

                        if (currentIndex == gap.pos) {
                            val id = gap.id
                            inlineContent[id] = createInlineContent(
                                gap = gap,
                                expanded = expandedGapId == gap.id,
                                onExpandedChange = { isExpanded ->
                                    expandedGapId = if (isExpanded) gap.id else null
                                },
                                onVariantSelected = { variant ->
                                    component.onVariantSelected(0, gap.id, variant)
                                    expandedGapId = null
                                }
                            )
                            appendInlineContent(id, "_____")
                            append(words[currentIndex] + " ")
                            currentIndex++
                        }
                    }

                    while (currentIndex < words.size) {
                        append(words[currentIndex] + " ")
                        currentIndex++
                    }
                }

                Text(
                    text = annotatedString,
                    style = TextStyle(
                        fontSize = 15.sp,
                        color = Color.DarkGray.copy(alpha = 0.7f),
                        fontWeight = FontWeight.W500
                    ),
                    inlineContent = inlineContent, modifier = Modifier.align(
                        Alignment.CenterHorizontally
                    )
                )
            }

            onContinueClick {
                component.onContinueClick()
            }
        }
    }
}

@Composable
private fun createInlineContent(
    gap: TextInputTaskWithVariantComponent.InputGap,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onVariantSelected: (String) -> Unit
): InlineTextContent {
    val borderColor = when (gap.state) {
        is TextInputTaskWithVariantComponent.GapState.Entering -> Color.DarkGray.copy(alpha = 0.5f)
        is TextInputTaskWithVariantComponent.GapState.Success -> Color.Green
        is TextInputTaskWithVariantComponent.GapState.Error -> Color.Red
    }

    val textColor = when (gap.state) {
        is TextInputTaskWithVariantComponent.GapState.Entering -> Color.DarkGray.copy(alpha = 0.8f)
        is TextInputTaskWithVariantComponent.GapState.Success -> Color.Green
        is TextInputTaskWithVariantComponent.GapState.Error -> Color.Red
    }

    val displayText = if (gap.selected.isBlank()) "        " else gap.selected
    val width = (maxOf(displayText.length, 10) * 9).sp

    return InlineTextContent(
        Placeholder(
            width = width,
            height = 30.sp,
            placeholderVerticalAlign = PlaceholderVerticalAlign.TextTop
        )
    ) {
        Box {
            Box(
                modifier = Modifier
                    .border(1.dp, borderColor, RoundedCornerShape(4.dp))
                    .clickable { onExpandedChange(true) }
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .background(Color.White)
                    .align(Alignment.Center)
            ) {
                Text(
                    text = displayText,
                    color = textColor,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.W500,
                    modifier = Modifier.align(Alignment.Center),
                    textAlign = TextAlign.Center
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { onExpandedChange(false) }
            ) {
                gap.variants.forEach { variant ->
                    DropdownMenuItem(
                        onClick = {
                            onVariantSelected(variant)
                        },
                        text = {
                            Text(
                                text = variant,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.W500
                            )
                        }
                    )
                }
            }
        }
    }
}

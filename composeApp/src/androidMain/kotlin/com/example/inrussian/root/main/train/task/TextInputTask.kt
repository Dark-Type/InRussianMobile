package com.example.inrussian.root.main.train.task

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.example.inrussian.components.main.train.tasks.interfaces.TextInputTaskComponent

//@Preview(showBackground = true, showSystemUi = true, backgroundColor = 0xFFEAEAEA)

@Composable
fun TextInputTask(
    component: TextInputTaskComponent,
    onContinueClick: (() -> Unit) -> Unit
) {
    val state by component.state.subscribeAsState()

    Box(Modifier.fillMaxSize()) {
        LazyColumn(
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White)
                .padding(16.dp)
        ) {
            itemsIndexed(state.blocks) { blockIndex, block ->
                Text(block.label, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(8.dp))

                val inlineContent = mutableMapOf<String, InlineTextContent>()
                val annotatedString = buildAnnotatedString {
                    val words = block.words

                    val sortedGaps = block.gaps.sortedBy { it.pos }

                    var currentWordIndex = 0

                    sortedGaps.forEach { gap ->
                        while (currentWordIndex < gap.pos && currentWordIndex < words.size) {
                            append(words[currentWordIndex] + " ")
                            currentWordIndex++
                        }

                        if (currentWordIndex == gap.pos) {
                            val id = gap.id
                            inlineContent[id] = createInlineContent(gap) { newText ->
                                component.onTextChange(blockIndex, gap.id, newText)
                            }
                            appendInlineContent(id, "_____")
                            append(" " + words[currentWordIndex] + " ")
                            currentWordIndex++
                        }
                    }

                    while (currentWordIndex < words.size) {
                        append(words[currentWordIndex] + " ")
                        currentWordIndex++
                    }
                }

                Text(
                    text = annotatedString,
                    style = TextStyle(
                        fontSize = 15.sp,
                        color = Color.DarkGray.copy(alpha = 0.7f),
                        fontWeight = FontWeight.W500
                    ),
                    inlineContent = inlineContent
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        onContinueClick {
            component.onContinueClick()
        }
    }
}

@Composable
private fun createInlineContent(
    gap: TextInputTaskComponent.InputGap,
    onTextChange: (String) -> Unit
): InlineTextContent {
    val borderColor = when (gap.state) {
        is TextInputTaskComponent.GapState.Entering -> Color.DarkGray.copy(alpha = 0.5f)
        is TextInputTaskComponent.GapState.Success -> Color.Green
        is TextInputTaskComponent.GapState.Error -> Color.Red
    }

    val textColor = when (gap.state) {
        is TextInputTaskComponent.GapState.Entering -> Color.DarkGray.copy(alpha = 0.8f)
        is TextInputTaskComponent.GapState.Success -> Color.Green
        is TextInputTaskComponent.GapState.Error -> Color.Red
    }

    val width = (maxOf(gap.answer.length, gap.input.length, 5) * 10).sp

    return InlineTextContent(
        Placeholder(
            width = width,
            height = 24.sp,
            placeholderVerticalAlign = PlaceholderVerticalAlign.TextTop
        )
    ) {
        BasicTextField(
            value = gap.input,
            onValueChange = onTextChange,
            modifier = Modifier
                .border(1.dp, borderColor, RoundedCornerShape(4.dp))
                .padding(horizontal = 4.dp)
                .background(Color.White),
            textStyle = TextStyle(
                fontSize = 15.sp,
                color = textColor,
                fontWeight = FontWeight.W500
            ),
            singleLine = true
        )
    }
}

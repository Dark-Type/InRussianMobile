package com.example.inrussian.root.main.train.task

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.inrussian.models.models.Gap
import com.example.inrussian.models.models.Sentence
import com.example.inrussian.models.models.task.TextInsertTask

@Preview(showBackground = true, showSystemUi = true, backgroundColor = 0xFFEAEAEA)
@Composable
fun TextInputTask(
    elements: List<TextInsertTask> = listOf(
        TextInsertTask(
            "Заголовок 1",
            listOf(
                Sentence(
                    "Текст будет здесь\nТекст ебать копать хоронить будет здесь\nПеревод будет находиться здесь \n" +
                            "Либо его не будет",
                    listOf(Gap( "находиться", 11), Gap( ",будет", 23))
                )
            )
        )
    ), onGapChange: (Gap) -> Unit = {}
) {
    Box(Modifier.fillMaxSize()) {

    }
    LazyColumn(
        Modifier
            .background(Color.White)
            .padding(16.dp)
    ) {
        items(elements) { element ->
            Text(element.label, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(8.dp))
            Column(Modifier.padding(horizontal = 16.dp)) {
                element.sentence.forEach { sentence ->
                    val inlineContent = mutableMapOf<String, InlineTextContent>()

                    val annotated = buildAnnotatedString {
                        var startPos = 0
                        sentence.gaps.forEachIndexed { gapIndex, gap ->
                            if (gap.index > startPos) {
                                append(
                                    sentence.text.substring(
                                        startPos,
                                        minOf(gap.index + 1, sentence.text.length)
                                    )
                                )
                            }
                            val id = "gap_${element.label}_$gapIndex ${gap.index}"

                            val placeholderWidth = (gap.correctWord.length * 9).sp
                            val placeholderHeight = 20.sp

                            inlineContent[id] = InlineTextContent(
                                Placeholder(
                                    width = placeholderWidth,
                                    height = placeholderHeight,
                                    placeholderVerticalAlign = PlaceholderVerticalAlign.AboveBaseline
                                )
                            ) {
                                val widthDp = with(LocalDensity.current) { placeholderWidth.toDp() }
                                Box(
                                    modifier = Modifier
                                        .width(widthDp)
                                        .height(with(LocalDensity.current) { placeholderHeight.toDp() }),
                                    contentAlignment = Alignment.CenterStart
                                ) {
                                    BasicTextField(
                                        value = gap.correctWord,
                                        onValueChange = { new -> onGapChange(gap.copy()) },
                                        modifier = Modifier
                                            .offset(y = 5.dp)
                                            .fillMaxSize()
                                            .border(
                                                1.dp,
                                                Color.DarkGray.copy(alpha = 0.5f),
                                                RoundedCornerShape(4.dp)
                                            )
                                            .padding(horizontal = 4.dp),
                                        textStyle = TextStyle(
                                            fontSize = 15.sp,
                                            color = Color.DarkGray.copy(alpha = 0.8f),
                                            fontWeight = FontWeight.W500
                                        ),
                                        singleLine = true
                                    )
                                }
                            }

                            appendInlineContent(id)

                            startPos = gap.index
                        }

                        if (startPos < sentence.text.length) {
                            append(sentence.text.substring(startPos))
                        }
                    }

                    Text(
                        text = annotated,
                        style = TextStyle(
                            fontSize = 15.sp,
                            color = Color.DarkGray.copy(alpha = 0.7f),
                            fontWeight = FontWeight.W500
                        ),
                        inlineContent = inlineContent
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

        }
    }
} 
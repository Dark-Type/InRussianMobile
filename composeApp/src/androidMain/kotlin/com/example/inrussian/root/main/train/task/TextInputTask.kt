package com.example.inrussian.root.main.train.task

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.example.inrussian.components.main.train.tasks.TextInputTask

//@Preview(showBackground = true, showSystemUi = true, backgroundColor = 0xFFEAEAEA)
@Composable
fun TextInputTask(
    component: TextInputTask
) {
    val state by component.state.subscribeAsState()
    Box(Modifier.fillMaxSize()) {

    }
    LazyColumn(
        modifier = Modifier
            .background(Color.White)
            .padding(16.dp)
    ) {
        itemsIndexed(state.blocks) { blockIndex, element ->
            Text(
                text = element.label,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(8.dp))

            val text = element.text
            val gaps = element.gaps.withIndex().sortedBy { it.value.pos }

            val inlineContent = remember(text, gaps) { mutableMapOf<String, InlineTextContent>() }

            val annotated = buildAnnotatedString {
                var cursor = 0
                for ((gapIndexInList, gapWrapper) in gaps) {
                    val gap = gapWrapper
                    val gapPos = gap.pos.coerceIn(0, text.length) // safety
                    // добавляем текст от cursor до gapPos
                    if (cursor < gapPos) {
                        append(text.substring(cursor, gapPos))
                    }
                    // уникальный id для inline контента
                    val id = "gap_${blockIndex}_$gapIndexInList"

                    // placeholder - ширина делаем на основе текущего текста (answer) или длины correctWord
                    val placeholderChars = maxOf(gap.answer.length, 3)
                    val placeholderWidth = (placeholderChars * 8).sp
                    val placeholderHeight = 20.sp

                    inlineContent[id] = InlineTextContent(
                        Placeholder(
                            width = placeholderWidth,
                            height = placeholderHeight,
                            placeholderVerticalAlign = PlaceholderVerticalAlign.AboveBaseline
                        )
                    ) {
                        // actual inline composable: поле ввода
                        val widthDp = with(LocalDensity.current) { placeholderWidth.toDp() }
                        Box(
                            modifier = Modifier
                                .width(widthDp)
                                .height(with(LocalDensity.current) { placeholderHeight.toDp() })
                                .padding(vertical = 2.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            BasicTextField(
                                value = gap.answer,
                                onValueChange = { new ->
                                    component.onTextChange(
                                        blockIndex,
                                        gapIndexInList,
                                        new
                                    )
                                },
                                singleLine = true,
                                textStyle = TextStyle(
                                    fontSize = 15.sp,
                                    color = Color.DarkGray.copy(alpha = 0.85f),
                                    fontWeight = FontWeight.W500
                                ),
                                modifier = Modifier
                                    .fillMaxSize()
                                    .border(
                                        1.dp,
                                        Color.DarkGray.copy(alpha = 0.5f),
                                        RoundedCornerShape(4.dp)
                                    )
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    appendInlineContent(id)
                    // продвигаем курсор. Предполагаем, что gap занимает 0 символов в исходном тексте,
                    // поэтому cursor = gapPos (если у тебя placeholder заменяет существующие символы,
                    // возможно нужно увеличить cursor на длину замещаемого текста)
                    cursor = gapPos
                }

                // остаток текста после последнего gap
                if (cursor < text.length) {
                    append(text.substring(cursor))
                }
            }

            // отрисовка строки с inline контентом
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

/*LazyColumn(
    Modifier
        .background(Color.White)
        .padding(16.dp)
) {
    items(state.blocks) { element ->
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
}*/

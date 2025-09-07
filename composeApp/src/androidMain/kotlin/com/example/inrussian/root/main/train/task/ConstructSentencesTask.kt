package com.example.inrussian.root.main.train.task

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.inrussian.repository.main.train.ConstructSentenceModel
import kotlinx.serialization.Serializable
import kotlin.math.hypot
import kotlin.math.roundToInt

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ConstructSentenceTask() {
    val sampleTask = ConstructSentenceModel(
                        audio = "https://ru-d1.drivemusic.me/dl/aUXUtDqRsTEr0AyF_pBYhA/1756874921/download_music/2019/11/the-weeknd-blinding-lights.mp3",
                        variants = listOf(
                            "Drag",
                            "and",
                            "drop",
                            "these",
                            "words",
                            "to",
                            "form",
                            "a",
                            "sentence"
                        )
                    )
        ConstructSentenceTaskUI(
            task = sampleTask,
            onResult = { isCorrect ->
                println("Task result: $isCorrect")
            }
        )
}
@Composable
fun DraggableWordChip(
    word: Word,
    index: Int,
    totalWords: Int,
    isDragging: Boolean,
    onDragStart: () -> Unit,
    onDragEnd: (Int) -> Unit,
    onDrag: (Offset) -> Unit,
    onPositionChanged: (Rect) -> Unit,
    dragOffset: MutableState<Offset>,
    dragStartPosition: MutableState<Offset>,
) {
    var localDragOffset by remember { mutableStateOf(Offset.Zero) }
    val currentPosition = remember { mutableStateOf(Offset.Zero) }
    
    Box(
        modifier = Modifier
            .onGloballyPositioned { coordinates ->
                val rect = coordinates.boundsInParent()
                onPositionChanged(rect)
                currentPosition.value = rect.topLeft
            }
    ) {
        WordChip(
            text = word.text,
            isDragging = isDragging,
            modifier = Modifier
                .offset {
                    IntOffset(
                        (if (isDragging) dragOffset.value.x else 0f).roundToInt(),
                        (if (isDragging) dragOffset.value.y else 0f).roundToInt()
                    )
                }
                .zIndex(if (isDragging) 1f else 0f)
                .pointerInput(word.id) {
                    detectDragGestures(
                        onDragStart = {
                            onDragStart()
                            localDragOffset = Offset.Zero
                            dragStartPosition.value = currentPosition.value
                        },
                        onDragEnd = {
                            onDragEnd(index)
                            localDragOffset = Offset.Zero
                            dragOffset.value = Offset.Zero
                        },
                        onDrag = { change, dragAmount ->
                            change.consume()
                            localDragOffset += dragAmount
                            dragOffset.value = localDragOffset
                            
                            val dragPosition = Offset(
                                dragStartPosition.value.x + localDragOffset.x,
                                dragStartPosition.value.y + localDragOffset.y
                            )
                            onDrag(dragPosition)
                        }
                    )
                }
        )
    }
}
@Composable
fun WordChip(
    text: String,
    isDragging: Boolean,
    modifier: Modifier = Modifier
) {
    val elevation by animateDpAsState(
        targetValue = if (isDragging) 8.dp else 2.dp,
        animationSpec = spring(dampingRatio = 0.6f),
        label = "elevation"
    )
    val alpha by animateFloatAsState(
        targetValue = if (isDragging) 0.9f else 1f,
        animationSpec = spring(dampingRatio = 0.6f),
        label = "alpha"
    )
    val backgroundColor = if (isDragging) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }
    
    Surface(
        modifier = modifier
            .alpha(alpha)
            .shadow(elevation, RoundedCornerShape(16.dp))
            .then(
                if (isDragging) Modifier.border(
                    2.dp,
                    MaterialTheme.colorScheme.primary,
                    RoundedCornerShape(16.dp)
                ) else Modifier
            ),
        shape = RoundedCornerShape(16.dp),
        tonalElevation = elevation,
        color = backgroundColor
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            color = if (isDragging) MaterialTheme.colorScheme.onPrimaryContainer
            else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalFoundationApi::class)
@Composable
fun ConstructSentenceTaskUI(
    task: ConstructSentenceModel,
    onResult: (Boolean) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val correctWords = remember(task.variants) {
        task.variants.mapIndexed { idx, text -> Word(idx, text) }
    }
    val userWords = remember { mutableStateListOf<Word>() }
    var result by remember { mutableStateOf(ResultState.None) }
    
    var draggedWordId by remember { mutableIntStateOf(-1) }
    var dropIndicatorIndex by remember { mutableIntStateOf(-1) }
    val wordPositions = remember { mutableMapOf<Int, Rect>() }
    val dragOffset = remember { mutableStateOf(Offset.Zero) }
    val dragStartPosition = remember { mutableStateOf(Offset.Zero) }
    
    LaunchedEffect(task.variants) {
        userWords.clear()
        userWords.addAll(correctWords.shuffled())
    }
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Construct the sentence",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.SemiBold),
            modifier = Modifier.align(Alignment.Start)
        )
        
        Spacer(Modifier.weight(1f))
        
        // Область для перетаскивания с фоном
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(16.dp)
        ) {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                userWords.forEachIndexed { index, word ->
                    if (dropIndicatorIndex == index) {
                        DropIndicator()
                    }
                    
                    DraggableWordChip(
                        word = word,
                        index = index,
                        totalWords = userWords.size,
                        isDragging = draggedWordId == word.id,
                        onDragStart = {
                            draggedWordId = word.id
                            dropIndicatorIndex = -1
                            dragOffset.value = Offset.Zero
                        },
                        onDragEnd = { currentIndex ->
                            val targetIndex = dropIndicatorIndex
                            if (targetIndex != -1 && targetIndex != currentIndex) {
                                // Перемещаем слово на новую позицию
                                val draggedWord = userWords.removeAt(currentIndex)
                                val insertIndex = if (targetIndex > currentIndex) {
                                    targetIndex - 1
                                } else {
                                    targetIndex
                                }
                                userWords.add(insertIndex.coerceIn(0, userWords.size), draggedWord)
                            }
                            draggedWordId = -1
                            dropIndicatorIndex = -1
                            dragOffset.value = Offset.Zero
                        },
                        onDrag = { dragPosition ->
                            val targetIndex = calculateDropIndex(
                                dragPosition,
                                wordPositions,
                                userWords.size
                            )
                            dropIndicatorIndex = targetIndex
                        },
                        onPositionChanged = { rect ->
                            wordPositions[word.id] = rect
                        },
                        dragOffset = dragOffset,
                        dragStartPosition = dragStartPosition
                    )
                }
                
                if (dropIndicatorIndex == userWords.size) {
                    DropIndicator()
                }
            }
        }
        
        Spacer(Modifier.height(24.dp))
        
        RowControls(
            onCheck = {
                val user = userWords.map { it.text }
                val correct = task.variants
                val isCorrect = user == correct
                result = if (isCorrect) ResultState.Correct else ResultState.Incorrect
                onResult(isCorrect)
            },
            onShuffle = {
                userWords.shuffle()
                result = ResultState.None
            },
            onReset = {
                userWords.clear()
                userWords.addAll(correctWords)
                result = ResultState.None
            }
        )
        
        Spacer(Modifier.height(12.dp))
        
        when (result) {
            ResultState.Correct -> ResultText(
                text = "Correct! 🎉",
                color = MaterialTheme.colorScheme.primary
            )
            ResultState.Incorrect -> ResultText(
                text = "Not quite. Try again.",
                color = MaterialTheme.colorScheme.error
            )
            ResultState.None -> Unit
        }
    }
}


fun calculateDropIndex(
    dragPosition: Offset,
    wordPositions: Map<Int, Rect>,
    totalWords: Int
): Int {
    if (wordPositions.isEmpty()) return -1
    
    val positions = wordPositions.entries.toList()
    
    // Ищем ближайшее слово к позиции перетаскивания
    var minDistance = Float.MAX_VALUE
    var closestIndex = -1
    
    positions.forEachIndexed { index, (_, rect) ->
        val center = rect.center
        val distance = hypot(
            (dragPosition.x - center.x).toDouble(),
            (dragPosition.y - center.y).toDouble()
        ).toFloat()
        
        if (distance < minDistance) {
            minDistance = distance
            closestIndex = index
        }
    }
    
    // Если расстояние слишком большое, не показываем индикатор
    if (minDistance > 150f) return -1
    
    return closestIndex
}
@Composable
fun DropIndicator() {
    val alpha by animateFloatAsState(
        targetValue = 0.7f,
        animationSpec = tween(200),
        label = "drop_indicator_alpha"
    )
    
    Box(
        modifier = Modifier
            .width(4.dp)
            .height(32.dp)
            .alpha(alpha)
            .background(
                MaterialTheme.colorScheme.primary,
                RoundedCornerShape(2.dp)
            )
    )
}

@Composable
fun ResultText(text: String, color: Color) {
    Text(
        text = text,
        color = color,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}
@Composable
private fun RowControls(
    onCheck: () -> Unit,
    onShuffle: () -> Unit,
    onReset: () -> Unit,
) {
    androidx.compose.foundation.layout.Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(onClick = onCheck) { Text("Check") }
        Button(onClick = onShuffle) { Text("Shuffle") }
        Button(onClick = onReset) { Text("Reset") }
    }
}
data class Word(val id: Int, val text: String)

private enum class ResultState { None, Correct, Incorrect }


@Serializable
data class SelectWordsModel(
    val audio: String,
    val variants: List<Pair<String, Boolean>>
)
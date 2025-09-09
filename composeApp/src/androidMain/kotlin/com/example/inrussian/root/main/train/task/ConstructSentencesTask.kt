package com.example.inrussian.root.main.train.task

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import sh.calvin.reorderable.rememberReorderableLazyListState

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ConstructSentenceTask() {
    val lazyListState = rememberLazyListState()
    val reorderableLazyListState = rememberReorderableLazyListState(lazyListState) { from, to ->
    
    }
    
}

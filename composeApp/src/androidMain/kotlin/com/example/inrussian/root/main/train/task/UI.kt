package com.example.inrussian.root.main.train.task

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.inrussian.ui.theme.LocalExtraColors

object TaskDimens {
    val rowMinHeight = 76.dp
    val rowRadius = 16.dp
    val rowPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
    val rowSpacing = 12.dp

    val tileRadius = 12.dp
    val tileMinHeight = 56.dp
    val tilePadding = PaddingValues(horizontal = 12.dp, vertical = 10.dp)

    val notchDiameter = 28.dp
    val betweenTiles = 12.dp
    val borderWidth = 1.dp
}

object TaskTypography {
    val primary = TextStyle(fontSize = 13.sp, lineHeight = 14.sp, fontWeight = FontWeight.SemiBold)
    val secondary = TextStyle(fontSize = 12.sp, lineHeight = 14.sp, fontWeight = FontWeight.Normal)
}
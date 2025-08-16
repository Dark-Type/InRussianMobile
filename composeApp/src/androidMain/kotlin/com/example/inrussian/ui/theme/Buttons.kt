package com.example.inrussian.ui.theme

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun CommonButton(text: String, enable: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        enabled = enable,
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Orange,
            disabledContainerColor = DarkGrey
        ), modifier = Modifier.fillMaxWidth()
    ) {
        Text(text, Modifier.padding(vertical = 6.dp), color = Color.White)
    }
}
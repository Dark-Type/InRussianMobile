package com.example.inrussian.ui.theme

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import inrussian.composeapp.generated.resources.Res
import inrussian.composeapp.generated.resources.back
import inrussian.composeapp.generated.resources.back_arrow
import inrussian.composeapp.generated.resources.`continue`
import inrussian.composeapp.generated.resources.front_arror
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource


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

@Composable
fun RowScope.BackButton(enable: Boolean, onClick: () -> Unit) {
    IconButton(onClick) {
        Icon(
            vectorResource(Res.drawable.back_arrow),
            "",
            tint = if (enable) LightBlue else DarkGrey.copy(alpha = 0.8f)
        )
    }
    Text(
        stringResource(Res.string.back),
        fontSize = 18.sp,
        color = if (enable) LightBlue else DarkGrey.copy(alpha = 0.8f),
    )

}



@Composable
fun RowScope.ContinueButton(enable: Boolean, onClick: () -> Unit) {

    Spacer(Modifier.weight(1f))
    Text(
        stringResource(Res.string.`continue`),
        fontSize = 18.sp,
        color = if (enable) LightBlue else DarkGrey.copy(alpha = 0.8f)
    )
    IconButton(onClick) {
        Icon(
            vectorResource(Res.drawable.front_arror),
            "",
            tint = if (enable) LightBlue else DarkGrey.copy(alpha = 0.8f)
        )
    }
}
package com.example.inrussian.ui.theme

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun CommonTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: ImageVector? = null,
    onIconClick: () -> Unit = {},
    visualTransformation: VisualTransformation? = null,
    placeholder: String = "",
    error: String? = null,
) {
    val extraColors = LocalExtraColors.current

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(
                label,
                color = if (error != null) extraColors.errorColor else extraColors.fontCaptive
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .imePadding(),
        shape = RoundedCornerShape(10.dp),
        visualTransformation = visualTransformation ?: VisualTransformation.None,
        trailingIcon = if (icon != null) (@Composable {
            IconButton(onIconClick) {
                Icon(
                    icon, "", Modifier.size(24.dp)
                )
            }
        }) else null,
        placeholder = { Text(placeholder, color = extraColors.fontCaptive) },
        supportingText = {
            if (error != null) Text(error, color = extraColors.errorColor)
        },
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = if (error != null) extraColors.errorColor else extraColors.stroke,
            unfocusedIndicatorColor = if (error != null) extraColors.errorColor else extraColors.stroke,
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedLabelColor = extraColors.stroke,
            unfocusedLabelColor = extraColors.stroke,
            cursorColor = extraColors.stroke
        ),
        textStyle = TextStyle(color = extraColors.fontCaptive)
    )
}
package com.example.inrussian.ui.theme

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
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
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth().imePadding(),
        shape = RoundedCornerShape(10.dp),
        visualTransformation = visualTransformation ?: VisualTransformation.None,
        trailingIcon = if (icon != null) (@Composable {
            IconButton(onIconClick) {
                Icon(
                    icon, "", Modifier
                        .size(24.dp)
                )
            }
        }) else null, placeholder = { Text(placeholder) },
        supportingText = {
            if (error != null) Text(
                error
            )
        }
    )
}

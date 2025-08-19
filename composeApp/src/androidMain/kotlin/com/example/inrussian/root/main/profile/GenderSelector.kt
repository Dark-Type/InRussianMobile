package com.example.inrussian.root.main.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.example.inrussian.components.main.profile.Gender

@Composable
 fun GenderSelector(selected: Gender, onSelect: (Gender) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text("Пол", style = MaterialTheme.typography.labelLarge)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Gender.entries.forEach { g ->
                FilterChip(
                    selected = selected == g,
                    onClick = { onSelect(g) },
                    label = { Text(g.name) }
                )
            }
        }
    }
}
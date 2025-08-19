package com.example.inrussian.root.main.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.inrussian.components.main.profile.ProfileMainState

@Composable
 fun BadgesSection(state: ProfileMainState) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Значки", style = MaterialTheme.typography.titleMedium)
        if (state.badges.isEmpty()) {
            Text("Пока нет значков", style = MaterialTheme.typography.bodyMedium)
        } else {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                items(state.badges) { badge ->
                    Column(
                        horizontalAlignment = Alignment.Companion.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.Companion.widthIn(min = 72.dp)
                    ) {
                        Box(
                            modifier = Modifier.Companion
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            contentAlignment = Alignment.Companion.Center
                        ) {
                            Text(
                                badge.name.firstOrNull()?.uppercase() ?: "?",
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                        Text(
                            badge.name,
                            style = MaterialTheme.typography.labelMedium,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}
package com.example.inrussian.root.main.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.inrussian.components.main.profile.AppTheme
import com.example.inrussian.components.main.profile.ProfileMainComponent
import com.example.inrussian.components.main.profile.ProfileMainState

@Composable
 fun HeaderSection(
    state: ProfileMainState,
    component: ProfileMainComponent
) {
    Row(modifier = Modifier.Companion.fillMaxWidth()) {
        Avatar(avatarId = state.user?.avatarId)
        Spacer(Modifier.Companion.width(16.dp))
        Column(Modifier.Companion.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = state.displayName.ifBlank { "—" },
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Companion.Bold
            )
            Text(
                text = "Дата регистрации: ${state.registrationDate}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Row(
                verticalAlignment = Alignment.Companion.CenterVertically,
                modifier = Modifier.Companion.padding(top = 4.dp)
            ) {
                Text("Уведомления", modifier = Modifier.Companion.weight(1f))
                Switch(
                    checked = state.notificationsEnabled,
                    onCheckedChange = { component.toggleNotifications() }
                )
            }

            OutlinedButton(
                onClick = { component.cycleTheme() },
                modifier = Modifier.Companion.fillMaxWidth()
            ) {
                Text("Тема: ${state.theme.readable()}")
            }
        }
    }
}

fun AppTheme.readable(): String = when (this) {
    AppTheme.SYSTEM -> "СИСТЕМА"
    AppTheme.LIGHT -> "СВЕТЛАЯ"
    AppTheme.DARK -> "ТЕМНАЯ"
}
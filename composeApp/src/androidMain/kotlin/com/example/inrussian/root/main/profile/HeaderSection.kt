package com.example.inrussian.root.main.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.inrussian.components.main.profile.AppTheme
import com.example.inrussian.components.main.profile.ProfileMainComponent
import com.example.inrussian.components.main.profile.ProfileMainState
import inrussian.composeapp.generated.resources.Res
import inrussian.composeapp.generated.resources.profile
import inrussian.composeapp.generated.resources.register_date
import inrussian.composeapp.generated.resources.send_notification
import inrussian.composeapp.generated.resources.your_achievements
import org.jetbrains.compose.resources.stringResource

@Composable
fun HeaderSection(
    state: ProfileMainState, component: ProfileMainComponent
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        LabelText(stringResource(Res.string.profile))
        Spacer(Modifier.height(30.dp))
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Avatar(avatarId = state.user?.avatarId)
            Spacer(Modifier.height(24.dp))
            Text(
                text = state.displayName.ifBlank { "—" },
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(12.dp))
            Text(
                text = "${stringResource(Res.string.register_date)}:\n ${state.registrationDate}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )


        }
        Spacer(Modifier.height(18.dp))
        Text(stringResource(Res.string.your_achievements))

    }
}


fun AppTheme.readable(): String = when (this) {
    AppTheme.SYSTEM -> "СИСТЕМА"
    AppTheme.LIGHT -> "СВЕТЛАЯ"
    AppTheme.DARK -> "ТЕМНАЯ"
}

@Composable
fun LabelText(text: String) {
    Text(text = text, fontSize = 35.sp, fontWeight = FontWeight.W600)
}
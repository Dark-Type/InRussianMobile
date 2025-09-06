package com.example.inrussian.root.main.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.inrussian.components.main.profile.ProfileMainComponent
import com.example.inrussian.components.main.profile.ProfileMainState
import com.example.inrussian.ui.theme.Orange
import inrussian.composeapp.generated.resources.Res
import inrussian.composeapp.generated.resources.logout
import inrussian.composeapp.generated.resources.profile
import inrussian.composeapp.generated.resources.register_date
import inrussian.composeapp.generated.resources.your_achievements
import nekit.corporation.shift_app.ui.theme.LocalExtraColors
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun HeaderSection(
    state: ProfileMainState, component: ProfileMainComponent
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Card(
            modifier = Modifier
                .align(Alignment.TopEnd),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            IconButton(
                onClick = { component.onShowAuth() }
            ) {
                Icon(
                    painter = painterResource(Res.drawable.logout),
                    contentDescription = stringResource(Res.string.logout),
                    tint = Orange
                )
            }
        }
        Column {
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
                    text = "${stringResource(Res.string.register_date)}\n${state.registrationDate}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )


            }
            Spacer(Modifier.height(18.dp))
            Text(stringResource(Res.string.your_achievements))

        }
    }
}

@Composable
fun LabelText(text: String) {
    val currentColors = LocalExtraColors.current
    Text(
        text = text,
        fontSize = 35.sp,
        fontWeight = FontWeight.W600,
        color = currentColors.fontCaptive
    )
}
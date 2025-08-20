package com.example.inrussian.root.main.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.example.inrussian.components.main.profile.AppTheme
import com.example.inrussian.components.main.profile.ProfileMainComponent
import inrussian.composeapp.generated.resources.Res
import inrussian.composeapp.generated.resources.about_app
import inrussian.composeapp.generated.resources.down_arrow
import inrussian.composeapp.generated.resources.edit_profile
import inrussian.composeapp.generated.resources.edit_theme
import inrussian.composeapp.generated.resources.privacy_police
import inrussian.composeapp.generated.resources.send_notification
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun ActionButtons(component: ProfileMainComponent) {
    val state by component.state.subscribeAsState()
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(White)
            .padding(horizontal = 16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.offset(y = (6).dp)) {
            Text(stringResource(Res.string.send_notification))
            Spacer(Modifier.weight(1f))
            Switch(state.notificationsEnabled, component::onNotificationSwitchClick)
        }
        HorizontalDivider()
        ProfileRow(
            stringResource(Res.string.edit_theme),
            state.isEditThemeOpen,
            variants = listOf(
                AppTheme.SYSTEM.toString(),
                AppTheme.LIGHT.toString(),
                AppTheme.DARK.toString()
            ),
            component::onThemeChangeClick,
            onSelect = { component.onSelectTheme(AppTheme.valueOf(it)) }
        )
        HorizontalDivider()
        ProfileRow(
            stringResource(Res.string.edit_profile),
            state.isEditThemeOpen,
            variants = listOf(
                AppTheme.SYSTEM.toString(),
                AppTheme.LIGHT.toString(),
                AppTheme.DARK.toString()
            ),
            { component.onEditClick() },
            onSelect = { component.onSelectTheme(AppTheme.valueOf(it)) }
        )
        HorizontalDivider()

        ProfileCommonRow(stringResource(Res.string.privacy_police), component::onAboutClick)
        HorizontalDivider()

        ProfileCommonRow(stringResource(Res.string.about_app), component::onPrivacyPolicyClick)
        Spacer(Modifier.height(4.dp))
    }
}

@Composable
fun ProfileCommonRow(text: String, onClick: () -> Unit) {
    Row {
        Text(text)
        Spacer(Modifier.weight(1f))
        IconButton(onClick, Modifier.size(20.dp)) {
            Icon(vectorResource(Res.drawable.down_arrow), "")
        }
    }
}

@Composable
fun ProfileRow(
    text: String,
    isOpen: Boolean,
    variants: List<String>,
    onChangeExpanded: (Boolean) -> Unit,
    onSelect: (String) -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text)
        Spacer(Modifier.weight(1f))
        IconButton({ onChangeExpanded(true) }, Modifier.size(20.dp)) {
            Icon(vectorResource(Res.drawable.down_arrow), "")
        }
        DropdownMenu(
            expanded = isOpen,
            onDismissRequest = { onChangeExpanded(false) },
            offset = DpOffset(x = 220.dp, y = 0.dp),
            containerColor = White
        ) {
            variants.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item) },
                    onClick = {
                        onSelect(item)
                        onChangeExpanded(false)
                    }
                )
            }
        }
    }
}
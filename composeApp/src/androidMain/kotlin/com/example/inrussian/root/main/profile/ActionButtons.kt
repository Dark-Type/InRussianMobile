package com.example.inrussian.root.main.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import com.example.inrussian.ui.theme.LightGrey
import com.example.inrussian.ui.theme.Orange
import com.example.inrussian.ui.theme.TabUnselectedColor
import inrussian.composeapp.generated.resources.Res
import inrussian.composeapp.generated.resources.about_app
import inrussian.composeapp.generated.resources.down_arrow
import inrussian.composeapp.generated.resources.edit_profile
import inrussian.composeapp.generated.resources.edit_theme
import inrussian.composeapp.generated.resources.privacy_police
import inrussian.composeapp.generated.resources.send_notification
import inrussian.composeapp.generated.resources.show_onboarding
import inrussian.composeapp.generated.resources.theme_dark
import inrussian.composeapp.generated.resources.theme_light
import inrussian.composeapp.generated.resources.theme_system
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource


@Composable
fun ActionButtons(component: ProfileMainComponent) {
    val state by component.state.subscribeAsState()

    val themeMap = mapOf(
        AppTheme.SYSTEM to stringResource(Res.string.theme_system),
        AppTheme.LIGHT to stringResource(Res.string.theme_light),
        AppTheme.DARK to stringResource(Res.string.theme_dark)
    )
    val themeVariants = themeMap.values.toList()
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(White)
            .padding(horizontal = 16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.offset(y = (6).dp)
        ) {
            Text(stringResource(Res.string.send_notification))
            Spacer(Modifier.weight(1f))
            Switch(
                state.notificationsEnabled,
                component::onNotificationSwitchClick,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = White,
                    uncheckedThumbColor = White,
                    checkedTrackColor = Orange.copy(alpha = 0.8f),
                    uncheckedTrackColor = TabUnselectedColor,
                    uncheckedBorderColor = TabUnselectedColor
                )
            )
        }
        HorizontalDivider()
        ProfileRow(
            stringResource(Res.string.edit_theme),
            state.isEditThemeOpen,
            variants = themeVariants,
            component::onThemeChangeClick,
            onSelect = { selected ->
                val theme = themeMap.entries.find { it.value == selected }?.key ?: AppTheme.SYSTEM
                component.onSelectTheme(theme)
            }
        )
        HorizontalDivider()
        ProfileCommonRow(
            stringResource(Res.string.edit_profile),
        ) { component.onEditClick() }
        HorizontalDivider()

        ProfileCommonRow(stringResource(Res.string.privacy_police), component::onAboutClick)
        HorizontalDivider()

        ProfileCommonRow(stringResource(Res.string.show_onboarding)) {
            component.onShowOnboarding()
        }
        HorizontalDivider()

        ProfileCommonRow(stringResource(Res.string.about_app), component::onPrivacyPolicyClick)
        Spacer(Modifier.height(4.dp))
    }
}

@Composable
fun ProfileCommonRow(text: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp, horizontal = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text)
        Spacer(Modifier.weight(1f))
        Icon(
            vectorResource(Res.drawable.down_arrow),
            contentDescription = "",
            modifier = Modifier.size(10.dp)
        )
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
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onChangeExpanded(true) }
            .padding(vertical = 12.dp, horizontal = 5.dp)
    ) {
        Text(text)
        Spacer(Modifier.weight(1f))
        Icon(
            vectorResource(Res.drawable.down_arrow),
            contentDescription = "",
            modifier = Modifier.size(10.dp)
        )

        DropdownMenu(
            expanded = isOpen,
            onDismissRequest = { onChangeExpanded(false) },
            offset = DpOffset(x = 220.dp, y = 0.dp),
            containerColor = White,
            shape = RoundedCornerShape(12.dp)
        ) {
            variants.forEachIndexed { index, item ->
                DropdownMenuItem(
                    text = { Text(item) },
                    onClick = {
                        onSelect(item)
                        onChangeExpanded(false)
                    }
                )
                if (index < variants.lastIndex) {
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 12.dp),
                        thickness = 1.dp,
                        color = LightGrey
                    )
                }
            }
        }
    }
}
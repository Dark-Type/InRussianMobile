package com.example.inrussian.root.main.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.inrussian.components.main.profile.ProfileMainComponent

@Composable
 fun ActionButtons(component: ProfileMainComponent) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Button(
            onClick = component::onEditClick,
            modifier = Modifier.Companion.fillMaxWidth()
        ) { Text("Редактировать профиль") }

        OutlinedButton(
            onClick = component::onAboutClick,
            modifier = Modifier.Companion.fillMaxWidth()
        ) { Text("О приложении") }

        OutlinedButton(
            onClick = component::onPrivacyPolicyClick,
            modifier = Modifier.Companion.fillMaxWidth()
        ) { Text("Политика конфиденциальности") }
    }
}
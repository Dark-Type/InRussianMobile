package com.example.inrussian.root.auth.sso

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import com.example.inrussian.components.auth.ssoPopover.SsoPopoverComponent

@Composable
fun SsoPopoverUi(component: SsoPopoverComponent) {
    Column(modifier = Modifier.padding(16.dp)) {
        Spacer(modifier = Modifier.fillMaxHeight(0.7f))

        Text("Войти через SSO", style = MaterialTheme.typography.bodySmall)
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { component.onAuthenticationSuccess() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Войти")
        }
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedButton(
            onClick = { component.onBackClicked() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Назад")
        }
    }
}
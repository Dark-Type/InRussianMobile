package com.example.inrussian.root.auth.recovery.email

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.inrussian.components.auth.passwordRecovery.enterEmail.EnterEmailComponent

@Composable
fun EnterEmailUi(component: EnterEmailComponent) {
    var email by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {

        Spacer(modifier = Modifier.fillMaxHeight(0.7f))

        Text("Введите email для восстановления", style = MaterialTheme.typography.bodySmall)
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { component.onEmailEntered(email)},
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Далее")
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
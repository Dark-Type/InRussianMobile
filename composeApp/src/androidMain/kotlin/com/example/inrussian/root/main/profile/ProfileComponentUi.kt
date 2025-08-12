package com.example.inrussian.root.main.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.example.inrussian.components.main.profile.ProfileComponent

@Composable
fun ProfileComponentUi(component: ProfileComponent) {
    val stack by component.stack.subscribeAsState()
    val current = stack.active.instance

    when (current) {
        is ProfileComponent.Child.MainChild -> {
            Column {
                Spacer(modifier = Modifier.fillMaxHeight(0.5f))
                Button(onClick = { current.component.onEditClick() }) {
                    Text("Редактировать профиль")
                }
                Button(onClick = { current.component.onAboutClick() }) {
                    Text("О приложении")
                }
                Button(onClick = { current.component.onPrivacyPolicyClick() }) {
                    Text("Политика конфиденциальности")
                }
            }
        }
        is ProfileComponent.Child.EditProfileChild -> {
            Column {
                Text("Редактирование профиля")
                Button(onClick = { current.component.onBack() }) {
                    Text("Назад")
                }
            }
        }
        is ProfileComponent.Child.AboutChild -> {
            Column {
                Text("О приложении")
                Button(onClick = { current.component.onBack() }) {
                    Text("Назад")
                }
            }
        }
        is ProfileComponent.Child.PrivacyPolicyChild -> {
            Column {
                Text("Политика конфиденциальности")
                Button(onClick = { current.component.onBack() }) {
                    Text("Назад")
                }
            }
        }
    }
}
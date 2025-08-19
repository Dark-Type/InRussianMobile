package com.example.inrussian.root.main.profile

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.example.inrussian.components.main.profile.ProfileComponent

@Composable
fun ProfileComponentUi(component: ProfileComponent) {
    val stack by component.stack.subscribeAsState()
    val current = stack.active.instance

    Surface(modifier = Modifier.fillMaxSize()) {
        when (current) {
            is ProfileComponent.Child.MainChild ->
                ProfileMainScreen(component = current.component)

            is ProfileComponent.Child.EditProfileChild ->
                EditProfileScreen(component = current.component)

            is ProfileComponent.Child.AboutChild ->
                StaticTextScreen(
                    stateValue = current.component.state,
                    onBack = current.component::onBack
                )

            is ProfileComponent.Child.PrivacyPolicyChild ->
                StaticTextScreen(
                    stateValue = current.component.state,
                    onBack = current.component::onBack
                )
        }
    }
}


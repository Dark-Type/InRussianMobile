package com.example.inrussian.root.root

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.example.inrussian.components.root.RootComponent
import com.example.inrussian.root.auth.root.AuthRootComponentUi
import com.example.inrussian.root.main.root.MainRootComponentUi
import com.example.inrussian.root.onboarding.root.OnboardingRootComponentUi

@Composable
fun RootComponentUi(component: RootComponent) {
    Box(Modifier.statusBarsPadding()) {
        val stack by component.stack.subscribeAsState()
        when (val child = stack.active.instance) {
            is RootComponent.Child.AuthChild -> AuthRootComponentUi(child.component)
            is RootComponent.Child.OnboardingChild -> OnboardingRootComponentUi(child.component)
            is RootComponent.Child.MainChild -> MainRootComponentUi(child.component)
        }
    }

}
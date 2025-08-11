package com.example.inrussian.root.root

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.example.inrussian.components.root.RootComponent
import com.example.inrussian.root.auth.root.AuthRootComponentUi
import com.example.inrussian.root.onboarding.root.OnboardingRootComponentUi
import com.example.inrussian.root.main.root.MainRootComponentUi
@Composable
fun RootComponentUi(component: RootComponent) {
    val stack by component.stack.subscribeAsState()
    when (val child = stack.active.instance) {
        is RootComponent.Child.AuthChild -> AuthRootComponentUi(child.component)
        is RootComponent.Child.OnboardingChild -> OnboardingRootComponentUi(child.component)
        is RootComponent.Child.MainChild -> MainRootComponentUi(child.component)
    }
}
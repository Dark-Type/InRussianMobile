package com.example.inrussian.root.onboarding.onboarding

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.inrussian.components.onboarding.interactiveOnboarding.InteractiveOnboardingComponent

@Composable
fun InteractiveOnboardingComponentUi(component: InteractiveOnboardingComponent) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Интерактивный онбординг",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = { component.onFinish() }) {
            Text("Завершить")
        }
    }
}
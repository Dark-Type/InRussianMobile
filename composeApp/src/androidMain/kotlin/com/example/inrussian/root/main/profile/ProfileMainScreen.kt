package com.example.inrussian.root.main.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.example.inrussian.components.main.profile.ProfileMainComponent


@Composable
 fun ProfileMainScreen(component: ProfileMainComponent) {
    val state by component.state.subscribeAsState()

    if (state.isLoading) {
        Box(Modifier.Companion.fillMaxSize(), contentAlignment = Alignment.Companion.Center) {
            CircularProgressIndicator()
        }
        return
    }

    Column(
        modifier = Modifier.Companion
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        HeaderSection(state = state, component = component)
        BadgesSection(state = state)
        ActionButtons(component = component)
    }
}
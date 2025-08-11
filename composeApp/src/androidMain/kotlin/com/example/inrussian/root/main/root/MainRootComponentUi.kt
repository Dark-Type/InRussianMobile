package com.example.inrussian.root.main.root

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.inrussian.components.main.root.MainRootComponent
import com.example.inrussian.navigation.configurations.MainConfiguration
import com.example.inrussian.root.main.home.HomeComponentUi
import com.example.inrussian.root.main.profile.ProfileComponentUi
import com.example.inrussian.root.main.train.TrainComponentUi


@Composable
fun MainRootComponentUi(component: MainRootComponent) {
    val stack = component.stack.value
    val current = stack.active.instance

    Box(modifier = Modifier.fillMaxSize()) {
        when (current) {
            is MainRootComponent.Child.HomeChild -> HomeComponentUi(current.component)
            is MainRootComponent.Child.TrainChild -> TrainComponentUi(current.component)
            is MainRootComponent.Child.ProfileChild -> ProfileComponentUi(current.component)
        }

        NavigationBar(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 8.dp)
        ) {
            NavigationBarItem(
                selected = current is MainRootComponent.Child.HomeChild,
                onClick = { component.selectTab(MainConfiguration.Home) },
                icon = { Icon(Icons.Default.Home, contentDescription = "Дом") },
                label = { Text("Главная") }
            )
            NavigationBarItem(
                selected = current is MainRootComponent.Child.TrainChild,
                onClick = { component.selectTab(MainConfiguration.Train) },
                icon = { Icon(Icons.Default.Info, contentDescription = "Тренировка") },
                label = { Text("Тренировка") }
            )
            NavigationBarItem(
                selected = current is MainRootComponent.Child.ProfileChild,
                onClick = { component.selectTab(MainConfiguration.Profile) },
                icon = { Icon(Icons.Default.Person, contentDescription = "Профиль") },
                label = { Text("Профиль") }
            )
        }
    }
}
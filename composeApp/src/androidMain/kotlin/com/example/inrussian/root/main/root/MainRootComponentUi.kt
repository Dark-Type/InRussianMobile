package com.example.inrussian.root.main.root

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth

import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.example.inrussian.components.main.root.MainRootComponent
import com.example.inrussian.root.main.home.HomeComponentUi
import com.example.inrussian.root.main.profile.ProfileComponentUi
import com.example.inrussian.root.main.train.v2.TrainComponentUi

import com.example.inrussian.ui.theme.Orange
import com.example.inrussian.ui.theme.TabUnselectedColor
import inrussian.composeapp.generated.resources.Res
import inrussian.composeapp.generated.resources.book_pages
import inrussian.composeapp.generated.resources.house
import inrussian.composeapp.generated.resources.main
import inrussian.composeapp.generated.resources.person_crop_circle
import inrussian.composeapp.generated.resources.profile
import inrussian.composeapp.generated.resources.training
import com.example.inrussian.ui.theme.LocalExtraColors
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource


@Composable
fun MainRootComponentUi(component: MainRootComponent) {
    val stack by component.childStack.subscribeAsState()
    val current = stack.active.instance
    val activeTab = component.activeTab.value
    val currentColors = LocalExtraColors.current

    Box(modifier = Modifier.fillMaxSize()) {
        when (current) {
            is MainRootComponent.Child.HomeChild -> HomeComponentUi(current.component)
            is MainRootComponent.Child.TrainChild -> TrainComponentUi(current.component)
            is MainRootComponent.Child.ProfileChild -> ProfileComponentUi(current.component)
        }
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {
            HorizontalDivider(
                thickness = 1.dp,
                color = Color.Black.copy(alpha = 0.1f)
            )

            NavigationBar(
                modifier = Modifier
                    .fillMaxWidth(),
                containerColor = currentColors.tabColor
            ) {
                NavigationBarItem(
                    selected = activeTab == MainRootComponent.Tab.Home,
                    onClick = { component.openTab(MainRootComponent.Tab.Home) },
                    icon = {
                        Icon(
                            painterResource(Res.drawable.house),
                            contentDescription = stringResource(Res.string.main),
                            modifier = Modifier.size(28.dp)
                        )
                    },
                    label = { Text(stringResource(Res.string.main)) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Orange,
                        selectedTextColor = Orange,
                        unselectedIconColor = TabUnselectedColor,
                        unselectedTextColor = TabUnselectedColor,
                        indicatorColor = Color.Transparent
                    )
                )
                NavigationBarItem(
                    selected = activeTab == MainRootComponent.Tab.Train,
                    onClick = { component.openTab(MainRootComponent.Tab.Train) },
                    icon = {
                        Icon(
                            painterResource(Res.drawable.book_pages),
                            contentDescription = stringResource(Res.string.training),
                            modifier = Modifier.size(28.dp)
                        )
                    },
                    label = { Text(stringResource(Res.string.training)) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Orange,
                        selectedTextColor = Orange,
                        unselectedIconColor = TabUnselectedColor,
                        unselectedTextColor = TabUnselectedColor,
                        indicatorColor = Color.Transparent
                    )
                )
                NavigationBarItem(
                    selected = activeTab == MainRootComponent.Tab.Profile,
                    onClick = { component.openTab(MainRootComponent.Tab.Profile) },
                    icon = {
                        Icon(
                            painterResource(Res.drawable.person_crop_circle),
                            contentDescription = stringResource(Res.string.profile),
                            modifier = Modifier.size(28.dp)
                        )
                    },
                    label = { Text(stringResource(Res.string.profile)) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Orange,
                        selectedTextColor = Orange,
                        unselectedIconColor = TabUnselectedColor,
                        unselectedTextColor = TabUnselectedColor,
                        indicatorColor = Color.Transparent
                    )
                )
            }
        }
    }
}

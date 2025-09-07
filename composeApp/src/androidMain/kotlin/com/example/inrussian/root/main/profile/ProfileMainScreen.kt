package com.example.inrussian.root.main.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arkivanov.decompose.value.MutableValue
import com.example.inrussian.components.main.profile.AppTheme
import com.example.inrussian.components.main.profile.Badge
import com.example.inrussian.components.main.profile.BadgeType
import com.example.inrussian.components.main.profile.ProfileMainComponent
import com.example.inrussian.components.main.profile.ProfileMainState
import com.example.inrussian.components.main.profile.SystemLanguage
import com.example.inrussian.components.main.profile.User
import com.example.inrussian.components.main.profile.UserProfile
import com.example.inrussian.ui.theme.reallyLightGrey
import org.openapitools.client.models.UserProfileModel


@Composable
fun ProfileMainScreen(component: ProfileMainComponent) {
    val state by component.state.subscribeAsState()

    if (state.isLoading) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    Column(
        modifier = Modifier
            .background(reallyLightGrey)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        HeaderSection(
            state = state,
            component = component,
        )
        BadgesSection(
            badges = state.badges,
        )
        ActionButtons(
            component = component,
        )
        Spacer(modifier = Modifier.height(82.dp))
    }
}

class ProfileMainScreen : ProfileMainComponent {
    override val state = MutableValue(
        ProfileMainState(
            user = User(),
            isLoading = false,
            profile = UserProfileModel(
                surname = "Иванов",
                name = "Иван",
                patronymic = "Иванович",
                userId = "",
                gender = UserProfileModel.Gender.MALE,
                dob = "",
                dor = "",
                email = "",
                systemLanguage = SystemLanguage.RUSSIAN
            ),
            badges = listOf(
                Badge(
                    id = "",
                    name = "Strike",
                    iconUrl = "",
                    description = null,
                    badgeType = BadgeType.ACHIEVEMENT,
                    createdAt = "",
                    criteria = null
                )
            )
        )
    )

    override fun onEditClick() {
        TODO("Not yet implemented")
    }

    override fun onAboutClick() {
        TODO("Not yet implemented")
    }

    override fun onThemeChangeClick(boolean: Boolean) {
        TODO("Not yet implemented")
    }

    override fun onPrivacyPolicyClick() {
        TODO("Not yet implemented")
    }

    override fun toggleNotifications() {
        TODO("Not yet implemented")
    }

    override fun cycleTheme() {
        TODO("Not yet implemented")
    }

    override fun onSelectTheme(theme: AppTheme) {
        TODO("Not yet implemented")
    }

    override fun onNotificationSwitchClick(enable: Boolean) {
        TODO("Not yet implemented")
    }

    override fun onShowOnboarding() {
        TODO("Not yet implemented")
    }

    override fun onShowAuth() {
        TODO("Not yet implemented")
    }

    @Composable
    @Preview
    fun Preview() {
        ProfileMainScreen(this)
    }
}
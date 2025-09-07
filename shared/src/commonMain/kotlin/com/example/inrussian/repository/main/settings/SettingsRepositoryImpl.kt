package com.example.inrussian.repository.main.settings

import com.example.inrussian.components.main.profile.AppTheme
import com.example.inrussian.components.main.profile.UserProfile
import com.example.inrussian.platformInterfaces.UserConfigurationStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull

class SettingsRepositoryImpl(
    private val userConfigurationStorage: UserConfigurationStorage
) : SettingsRepository {
    private val mutableNotificationsEnabled = MutableStateFlow<Boolean?>(null)
    override val notificationsEnabled: Flow<Boolean> = mutableNotificationsEnabled.filterNotNull()

    private val mutableTheme = MutableStateFlow<AppTheme?>(null)
    override val theme: Flow<AppTheme> = mutableTheme.filterNotNull()

    override fun toggleNotifications() {
        TODO("Not yet implemented")
    }

    override fun setTheme(theme: AppTheme) {
        TODO("Not yet implemented")
    }
}
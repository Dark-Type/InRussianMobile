package com.example.inrussian.repository.main.settings

import com.example.inrussian.components.main.profile.AppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class InMemorySettingsRepository(
    private val scope: CoroutineScope
) : SettingsRepository {

    private val notifications = MutableStateFlow(false)
    private val themeState = MutableStateFlow(AppTheme.SYSTEM)

    override val notificationsEnabled: Flow<Boolean> = notifications
    override val theme: Flow<AppTheme> = themeState

    override fun toggleNotifications() {
        notifications.value = !notifications.value
    }

    override fun setTheme(theme: AppTheme) {
        themeState.value = theme
    }

    init {
        scope.launch {
        }
    }
}
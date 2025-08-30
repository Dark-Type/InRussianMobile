package com.example.inrussian.repository.main.settings

import com.example.inrussian.components.main.profile.AppTheme
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {

    fun toggleNotifications()
    fun setTheme(theme: AppTheme)
}
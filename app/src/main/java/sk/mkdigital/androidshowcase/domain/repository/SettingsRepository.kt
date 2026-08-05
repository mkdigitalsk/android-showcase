package sk.mkdigital.androidshowcase.domain.repository

import sk.mkdigital.androidshowcase.presentation.foundation.ThemeMode

interface SettingsRepository {
    suspend fun getThemeMode(): ThemeMode
    suspend fun setThemeMode(mode: ThemeMode)
}

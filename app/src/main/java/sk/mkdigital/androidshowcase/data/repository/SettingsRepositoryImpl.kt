package sk.mkdigital.androidshowcase.data.repository

import sk.mkdigital.androidshowcase.data.local.preferences.PersistentPreferences
import sk.mkdigital.androidshowcase.domain.repository.SettingsRepository
import sk.mkdigital.androidshowcase.presentation.foundation.ThemeMode
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val persistentPreferences: PersistentPreferences
) : SettingsRepository {

    override suspend fun getThemeMode(): ThemeMode {
        val mode = persistentPreferences.getThemeMode()
        return ThemeMode.entries.find { it.name == mode } ?: ThemeMode.SYSTEM
    }

    override suspend fun setThemeMode(mode: ThemeMode) {
        persistentPreferences.setThemeMode(mode.name)
    }
}

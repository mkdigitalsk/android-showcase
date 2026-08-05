package sk.mkdigital.androidshowcase.domain.useCase.settings

import sk.mkdigital.androidshowcase.domain.repository.SettingsRepository
import sk.mkdigital.androidshowcase.domain.useCase.base.UseCase
import sk.mkdigital.androidshowcase.presentation.foundation.ThemeMode
import javax.inject.Inject

class SetThemeModeUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) : UseCase<ThemeMode, Unit>() {
    override suspend fun run(params: ThemeMode) = settingsRepository.setThemeMode(params)
}

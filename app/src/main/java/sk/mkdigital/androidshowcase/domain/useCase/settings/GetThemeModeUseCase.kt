package sk.mkdigital.androidshowcase.domain.useCase.settings

import sk.mkdigital.androidshowcase.domain.repository.SettingsRepository
import sk.mkdigital.androidshowcase.domain.useCase.base.None
import sk.mkdigital.androidshowcase.domain.useCase.base.UseCase
import sk.mkdigital.androidshowcase.presentation.foundation.ThemeMode
import javax.inject.Inject

class GetThemeModeUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) : UseCase<None, ThemeMode>() {
    override suspend fun run(params: None): ThemeMode = settingsRepository.getThemeMode()
}

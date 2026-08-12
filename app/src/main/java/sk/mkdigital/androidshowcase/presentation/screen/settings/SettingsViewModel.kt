package sk.mkdigital.androidshowcase.presentation.screen.settings

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector
import sk.mkdigital.androidshowcase.BuildConfig
import sk.mkdigital.androidshowcase.R
import sk.mkdigital.androidshowcase.domain.useCase.auth.ClearLocalUserDataUseCase
import sk.mkdigital.androidshowcase.domain.useCase.auth.DeleteAccountUseCase
import sk.mkdigital.androidshowcase.domain.useCase.auth.GetCurrentUserUseCase
import sk.mkdigital.androidshowcase.domain.useCase.base.invoke
import sk.mkdigital.androidshowcase.domain.useCase.crash.RecordExceptionUseCase
import sk.mkdigital.androidshowcase.domain.useCase.settings.GetThemeModeUseCase
import sk.mkdigital.androidshowcase.domain.useCase.settings.SetThemeModeUseCase
import sk.mkdigital.androidshowcase.presentation.base.BaseViewModel
import sk.mkdigital.androidshowcase.presentation.base.NavEvent
import sk.mkdigital.androidshowcase.presentation.foundation.AppIcons
import sk.mkdigital.androidshowcase.presentation.foundation.ThemeMode
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import sk.mkdigital.androidshowcase.util.suspendRunCatching

data class SettingsState(
    val themeModeState: ThemeModeState = ThemeModeState.SYSTEM,
    val currentLanguage: LanguageState = LanguageState.EN,
    val showThemeDialog: Boolean = false,
    val showDeleteAccountDialog: Boolean = false,
    val isDeletingAccount: Boolean = false,
    val deleteAccountFailed: Boolean = false,
    val isDemoAccount: Boolean? = null,
) {
    val showCrashButton: Boolean
        get() = BuildConfig.DEBUG
    val versionName: String = BuildConfig.VERSION_NAME
    val versionCode: String = BuildConfig.VERSION_CODE.toString()
}

enum class ThemeModeState(@get:StringRes val textId: Int, val mode: ThemeMode) {
    LIGHT(R.string.settings_theme_light, ThemeMode.LIGHT),
    DARK(R.string.settings_theme_dark, ThemeMode.DARK),
    SYSTEM(R.string.settings_theme_system, ThemeMode.SYSTEM);

    companion object {
        fun fromMode(mode: ThemeMode): ThemeModeState =
            entries.find { it.mode == mode } ?: SYSTEM
    }
}

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val getThemeModeUseCase: GetThemeModeUseCase,
    private val setThemeModeUseCase: SetThemeModeUseCase,
    private val recordExceptionUseCase: RecordExceptionUseCase,
    private val clearLocalUserDataUseCase: ClearLocalUserDataUseCase,
    private val deleteAccountUseCase: DeleteAccountUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
) : BaseViewModel<SettingsState>(SettingsState()) {

    override fun loadInitialData() {
        loadThemeMode()
        loadCurrentLanguage()
        loadCurrentUser()
    }

    override fun onResume() {
        loadCurrentLanguage()
    }

    private fun loadCurrentLanguage() {
        val appLocales = AppCompatDelegate.getApplicationLocales()
        val locale = appLocales[0] ?: LocaleListCompat.getAdjustedDefault()[0]
        val language = LanguageState.fromCode(locale?.language)
        newState { it.copy(currentLanguage = language) }
    }

    private fun loadThemeMode() {
        execute(
            action = { getThemeModeUseCase() },
            onSuccess = { themeMode -> newState { it.copy(themeModeState = ThemeModeState.fromMode(themeMode)) } }
        )
    }

    private fun loadCurrentUser() {
        execute(
            action = { getCurrentUserUseCase() },
            onSuccess = { user -> newState { it.copy(isDemoAccount = user.isDemo) } }
        )
    }

    fun setThemeMode(themeModeState: ThemeModeState) {
        execute(
            action = { setThemeModeUseCase(themeModeState.mode) },
            onSuccess = { _ ->
                newState { it.copy(themeModeState = themeModeState) }
                navigate(SettingNavEvents.ThemeChanged(themeModeState.mode))
            }
        )
    }

    fun showThemeDialog() {
        newState { it.copy(showThemeDialog = true) }
    }

    fun hideThemeDialog() {
        newState { it.copy(showThemeDialog = false) }
    }

    fun onLanguageSelected(language: LanguageState) {
        newState { it.copy(currentLanguage = language) }
        navigate(SettingNavEvents.SetLocaleTag(language.code))
    }

    fun openWeb() {
        navigate(SettingNavEvents.OpenWeb(STUDIO_URL))
    }

    fun signOut() {
        execute(
            action = { clearLocalUserDataUseCase() },
            onSuccess = { navigate(SettingNavEvents.SignOut) }
        )
    }

    fun showDeleteAccountDialog() {
        newState { it.copy(showDeleteAccountDialog = true, deleteAccountFailed = false) }
    }

    fun hideDeleteAccountDialog() {
        newState { it.copy(showDeleteAccountDialog = false) }
    }

    /**
     * The token authorizes the call, so the account goes first and the device is torn down after.
     * The server's answer decides the outcome — a local store that will not clear cannot turn a
     * completed erasure into "deletion failed" and leave the person on a dead account.
     */
    fun deleteAccount() {
        execute(
            action = {
                deleteAccountUseCase()
                suspendRunCatching { clearLocalUserDataUseCase() }
            },
            onLoading = { newState { it.copy(isDeletingAccount = true, deleteAccountFailed = false) } },
            onSuccess = { _ ->
                newState { it.copy(isDeletingAccount = false, showDeleteAccountDialog = false) }
                navigate(SettingNavEvents.SignOut)
            },
            onError = { _ ->
                newState {
                    it.copy(
                        isDeletingAccount = false,
                        showDeleteAccountDialog = false,
                        deleteAccountFailed = true
                    )
                }
            }
        )
    }

    fun triggerTestCrash() {
        val exception = RuntimeException("Test Crash for Firebase Crashlytics")
        execute(
            action = { recordExceptionUseCase(exception) },
            onSuccess = { throw exception }
        )
    }

    private companion object {
        const val STUDIO_URL = "https://mkdigital.sk"
    }
}

enum class LanguageState(
    @get:StringRes val stringRes: Int,
    val icon: ImageVector,
    val code: String,
) {
    SK(R.string.language_sk, AppIcons.FlagSK, "sk"),
    EN(R.string.language_en, AppIcons.FlagEN, "en");

    companion object {
        fun fromCode(code: String?): LanguageState =
            entries.find { it.code == code } ?: EN
    }
}

sealed interface SettingNavEvents : NavEvent {

    // Android
    data class SetLocaleTag(val tag: String) : SettingNavEvents

    data object SignOut : SettingNavEvents

    data class ThemeChanged(val mode: ThemeMode) : SettingNavEvents

    data class OpenWeb(val url: String) : SettingNavEvents
}

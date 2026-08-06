package sk.mkdigital.androidshowcase.presentation.base

import sk.mkdigital.androidshowcase.presentation.foundation.ThemeMode

data class AppCallbacks(
    // Navigation
    val navigateTo: (Route) -> Unit = {},
    val onBack: () -> Unit = {},
    // External actions
    val openLink: (String) -> Unit = {},
    val dial: (String) -> Unit = {},
    val share: (String, String) -> Unit = { _, _ -> },
    val copyToClipboard: (String) -> Unit = {},
    val sendEmail: (to: String, subject: String, body: String) -> Unit = { _, _, _ -> },
    val openSettings: () -> Unit = {},
    val openNotificationSettings: () -> Unit = {},
    // App settings
    val setLocale: (String) -> Unit = {},
    val setThemeMode: (ThemeMode) -> Unit = {},
)

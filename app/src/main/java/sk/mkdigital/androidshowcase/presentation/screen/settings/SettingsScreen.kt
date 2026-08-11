package sk.mkdigital.androidshowcase.presentation.screen.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BugReport
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.SharedFlow
import sk.mkdigital.androidshowcase.R
import sk.mkdigital.androidshowcase.presentation.base.CollectNavEvents
import sk.mkdigital.androidshowcase.presentation.base.NavEvent
import sk.mkdigital.androidshowcase.presentation.base.NavRouter
import sk.mkdigital.androidshowcase.presentation.base.Route
import sk.mkdigital.androidshowcase.presentation.base.lifecycleAwareViewModel
import sk.mkdigital.androidshowcase.presentation.component.AppAlertDialog
import sk.mkdigital.androidshowcase.presentation.component.AppRadioButton
import sk.mkdigital.androidshowcase.presentation.component.AvatarState
import sk.mkdigital.androidshowcase.presentation.component.AvatarView
import sk.mkdigital.androidshowcase.presentation.component.buttons.AppTextButtonPrimary
import sk.mkdigital.androidshowcase.presentation.component.buttons.AppTextButtonError
import sk.mkdigital.androidshowcase.presentation.component.cards.AppElevatedCard
import sk.mkdigital.androidshowcase.presentation.component.image.AppIconPrimary
import sk.mkdigital.androidshowcase.presentation.component.imagepicker.ImagePickerView
import sk.mkdigital.androidshowcase.presentation.component.imagepicker.ImagePickerViewModel
import sk.mkdigital.androidshowcase.presentation.component.spacers.ColumnSpacer.Spacer2
import sk.mkdigital.androidshowcase.presentation.component.text.bodyLarge.TextBodyLargeNeutral100
import sk.mkdigital.androidshowcase.presentation.component.text.bodyLarge.TextBodyLargePrimary
import sk.mkdigital.androidshowcase.presentation.component.text.bodyMedium.TextBodyMedium
import sk.mkdigital.androidshowcase.presentation.component.text.bodyMedium.TextBodyMediumNeutral80
import sk.mkdigital.androidshowcase.presentation.component.text.bodySmall.TextBodySmallNeutral80
import sk.mkdigital.androidshowcase.presentation.component.text.titleLarge.TextTitleLargePrimary
import sk.mkdigital.androidshowcase.presentation.foundation.AppTheme
import sk.mkdigital.androidshowcase.presentation.foundation.appColorScheme
import sk.mkdigital.androidshowcase.presentation.foundation.floatingNavBarSpace
import sk.mkdigital.androidshowcase.presentation.foundation.space2
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.material3.MaterialTheme
import sk.mkdigital.androidshowcase.presentation.component.image.AppImage
import sk.mkdigital.androidshowcase.presentation.component.text.bodySmall.TextBodySmall
import sk.mkdigital.androidshowcase.presentation.foundation.space4

@Composable
fun SettingsScreen(
    router: NavRouter<Route>,
    viewModel: SettingsViewModel = lifecycleAwareViewModel(),
    imagePickerViewModel: ImagePickerViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val imagePickerState by imagePickerViewModel.state.collectAsStateWithLifecycle()

    val avatarState = when {
        imagePickerState.isLoading -> AvatarState.Loading
        imagePickerState.imageBitmap != null -> AvatarState.Loaded(imagePickerState.imageBitmap!!)
        else -> AvatarState.Empty
    }

    SettingsNavEvents(router, viewModel.navEvent)
    SettingsScreen(
        state = state,
        avatarState = avatarState,
        onProfilePhotoClick = imagePickerViewModel::showDialog,
        onThemeClick = viewModel::showThemeDialog,
        onThemeSelected = viewModel::setThemeMode,
        onThemeDismiss = viewModel::hideThemeDialog,
        onLanguageSelected = viewModel::onLanguageSelected,
        onCrashClick = viewModel::triggerTestCrash,
        onSignOut = viewModel::signOut,
        onDeleteAccountClick = viewModel::showDeleteAccountDialog,
        onDeleteAccountConfirm = viewModel::deleteAccount,
        onDeleteAccountDismiss = viewModel::hideDeleteAccountDialog,
        onWebClick = viewModel::openWeb
    )

    ImagePickerView(viewModel = imagePickerViewModel)
}

@Composable
fun SettingsScreen(
    state: SettingsState,
    avatarState: AvatarState = AvatarState.Empty,
    onProfilePhotoClick: () -> Unit = {},
    onThemeClick: () -> Unit = {},
    onThemeSelected: (ThemeModeState) -> Unit = {},
    onThemeDismiss: () -> Unit = {},
    onLanguageSelected: (LanguageState) -> Unit = {},
    onCrashClick: () -> Unit = {},
    onSignOut: () -> Unit = {},
    onDeleteAccountClick: () -> Unit = {},
    onDeleteAccountConfirm: () -> Unit = {},
    onDeleteAccountDismiss: () -> Unit = {},
    onWebClick: () -> Unit = {}
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = space4,
            end = space4,
            top = space4,
            bottom = floatingNavBarSpace
        ),
        verticalArrangement = Arrangement.spacedBy(space4)
    ) {
        item {
            TextTitleLargePrimary(stringResource(R.string.settings_profile))
        }

        item {
            AppElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                onClick = onProfilePhotoClick
            ) {
                ProfileItem(
                    avatarState = avatarState,
                    title = stringResource(R.string.settings_profile_photo),
                    hint = stringResource(R.string.settings_profile_photo_hint)
                )
            }
        }

        item {
            TextTitleLargePrimary(
                text = stringResource(R.string.settings_appearance),
                modifier = Modifier.padding(top = space4)
            )
        }

        item {
            AppElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                onClick = onThemeClick
            ) {
                SettingsItem(
                    icon = {
                        AppIconPrimary(
                            Icons.Outlined.DarkMode,
                            contentDescription = stringResource(R.string.settings_theme)
                        )
                    },
                    title = stringResource(R.string.settings_theme),
                    value = stringResource(state.themeModeState.textId)
                )
            }
        }

        item {
            LanguageSelector(
                currentLanguage = state.currentLanguage,
                onLanguageSelected = onLanguageSelected
            )
        }

        if (state.showCrashButton) {
            item {
                AppElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onCrashClick
                ) {
                    SettingsItem(
                        icon = {
                            AppIconPrimary(
                                Icons.Outlined.BugReport,
                                contentDescription = stringResource(R.string.settings_test_crash_title)
                            )
                        },
                        title = stringResource(R.string.settings_test_crash_title),
                        value = stringResource(R.string.settings_test_crash_subtitle)
                    )
                }
            }
        }

        item {
            TextTitleLargePrimary(
                text = stringResource(R.string.settings_about),
                modifier = Modifier.padding(top = space4)
            )
        }

        item {
            AppElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                onClick = onWebClick
            ) {
                AboutItem()
            }
        }

        item {
            VersionFooter(
                versionName = state.versionName,
                versionCode = state.versionCode
            )
        }

        item {
            AppTextButtonPrimary(
                text = stringResource(R.string.settings_sign_out),
                onClick = onSignOut,
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            // Null until the account is known. Claiming either way would be a guess, and deletion needs
            // the network the answer comes over, so there is nothing to offer while it is unknown.
            when (state.isDemoAccount) {
                true -> TextBodyMediumNeutral80(
                    text = stringResource(R.string.settings_delete_account_demo),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                false -> AppTextButtonError(
                    text = stringResource(R.string.settings_delete_account),
                    onClick = onDeleteAccountClick,
                    modifier = Modifier.fillMaxWidth()
                )
                null -> Unit
            }
        }

        if (state.deleteAccountFailed) {
            item {
                TextBodyMedium(
                    text = stringResource(R.string.settings_delete_account_error),
                    color = MaterialTheme.appColorScheme.error,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        }
    }

    if (state.showThemeDialog) {
        ThemeSelectionDialog(
            currentTheme = state.themeModeState,
            onThemeSelected = { themeModeState ->
                onThemeSelected(themeModeState)
                onThemeDismiss()
            },
            onDismiss = onThemeDismiss
        )
    }

    if (state.showDeleteAccountDialog) {
        DeleteAccountDialog(
            isDeleting = state.isDeletingAccount,
            onConfirm = onDeleteAccountConfirm,
            onDismiss = onDeleteAccountDismiss
        )
    }
}

@Composable
private fun DeleteAccountDialog(
    isDeleting: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AppAlertDialog(
        title = stringResource(R.string.settings_delete_account_title),
        text = stringResource(R.string.settings_delete_account_text),
        onDismissRequest = onDismiss,
        dismissButton = {
            AppTextButtonPrimary(
                text = stringResource(R.string.button_cancel),
                onClick = onDismiss
            )
        },
        confirmButton = {
            AppTextButtonError(
                text = stringResource(R.string.settings_delete_account_confirm),
                onClick = onConfirm,
                loading = isDeleting
            )
        }
    )
}

@Composable
private fun ProfileItem(
    avatarState: AvatarState,
    title: String,
    hint: String,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(space4),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(space4)
    ) {
        AvatarView(state = avatarState)
        Column(modifier = Modifier.weight(1f)) {
            TextBodyLargePrimary(title)
            Spacer2()
            TextBodyMediumNeutral80(hint)
        }
    }
}

@Composable
private fun SettingsItem(
    icon: @Composable () -> Unit,
    title: String,
    value: String,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(space4),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(space4)
    ) {
        icon()
        Column(modifier = Modifier.weight(1f)) {
            TextBodyLargePrimary(title)
            Spacer2()
            TextBodyMediumNeutral80(value)
        }
    }
}

@Composable
private fun ThemeSelectionDialog(
    currentTheme: ThemeModeState,
    onThemeSelected: (ThemeModeState) -> Unit,
    onDismiss: () -> Unit,
) {
    AppAlertDialog(
        title = stringResource(R.string.settings_theme),
        onDismissRequest = onDismiss,
    ) {
        Column {
            ThemeModeState.entries.forEach { themeModeState ->
                ThemeOption(
                    title = stringResource(themeModeState.textId),
                    selected = currentTheme == themeModeState,
                    onClick = { onThemeSelected(themeModeState) }
                )
            }
        }
    }
}

@Composable
private fun ThemeOption(
    title: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = space4),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AppRadioButton(selected = selected, onClick = onClick)
        TextBodyLargeNeutral100(title)
    }
}

@Composable
private fun AboutItem() {
    Column(modifier = Modifier.fillMaxWidth()) {
        AppImage(
            resource = R.drawable.mk_digital_lockup,
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.FillWidth
        )
        Column(
            modifier = Modifier.fillMaxWidth().padding(space4),
            verticalArrangement = Arrangement.spacedBy(space2)
        ) {
            TextBodyLargeNeutral100(
                text = stringResource(R.string.settings_about_tagline),
                fontWeight = FontWeight.Bold
            )
            TextBodySmall(
                text = stringResource(R.string.settings_about_web),
                color = MaterialTheme.colorScheme.primary,
                textDecoration = TextDecoration.Underline
            )
        }
    }
}

@Composable
private fun VersionFooter(
    versionName: String,
    versionCode: String,
) {
    TextBodySmallNeutral80(
        text = stringResource(R.string.settings_version, versionName, versionCode),
        modifier = Modifier.fillMaxWidth(),
        textAlign = androidx.compose.ui.text.style.TextAlign.End,
    )
}

@Composable
private fun SettingsNavEvents(
    router: NavRouter<Route>,
    navEvent: SharedFlow<NavEvent>,
) {
    CollectNavEvents(navEventFlow = navEvent) { event ->
        when (event) {
            is SettingNavEvents.SetLocaleTag -> router.setLocale(event.tag)
            is SettingNavEvents.SignOut -> router.navigateTo(
                Route.SignIn,
                popUpTo = Route.HomeSection.Home::class,
                inclusive = true
            )

            is SettingNavEvents.ThemeChanged -> router.setThemeMode(event.mode)
            is SettingNavEvents.OpenWeb -> router.openLink(event.url)
        }
    }
}

@Preview
@Preview(uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SettingsScreenPreview(
    @PreviewParameter(SettingsScreenPreviewParams::class) state: SettingsState
) {
    AppTheme {
        SettingsScreen(state = state)
    }
}

internal class SettingsScreenPreviewParams : PreviewParameterProvider<SettingsState> {
    override val values = sequenceOf(
        SettingsState(),
        SettingsState(themeModeState = ThemeModeState.DARK, currentLanguage = LanguageState.SK),
        SettingsState(showThemeDialog = true),
        SettingsState(showDeleteAccountDialog = true),
        SettingsState(showDeleteAccountDialog = true, isDeletingAccount = true),
        SettingsState(isDemoAccount = true)
    )
}

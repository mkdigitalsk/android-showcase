package sk.mkdigital.androidshowcase.presentation.screen.signIn

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.SharedFlow
import sk.mkdigital.androidshowcase.R
import sk.mkdigital.androidshowcase.presentation.base.CollectNavEvents
import sk.mkdigital.androidshowcase.presentation.base.NavEvent
import sk.mkdigital.androidshowcase.presentation.base.NavRouter
import sk.mkdigital.androidshowcase.presentation.base.Route
import sk.mkdigital.androidshowcase.presentation.component.AppPasswordTextField
import sk.mkdigital.androidshowcase.presentation.component.AppTextField
import sk.mkdigital.androidshowcase.presentation.component.biometric.BiometricView
import sk.mkdigital.androidshowcase.presentation.component.buttons.ContainedButton
import sk.mkdigital.androidshowcase.presentation.component.text
import sk.mkdigital.androidshowcase.presentation.component.text.labelLarge.TextButtonError
import sk.mkdigital.androidshowcase.presentation.component.image.AppIconNeutral80
import sk.mkdigital.androidshowcase.presentation.component.spacers.ColumnSpacer.Spacer2
import sk.mkdigital.androidshowcase.presentation.component.spacers.ColumnSpacer.Spacer4
import sk.mkdigital.androidshowcase.presentation.component.spacers.ColumnSpacer.Spacer6
import sk.mkdigital.androidshowcase.presentation.component.spacers.ColumnSpacer.Spacer8
import sk.mkdigital.androidshowcase.presentation.component.text.bodyMedium.TextBodyMediumNeutral80
import sk.mkdigital.androidshowcase.presentation.component.text.bodySmall.TextBodySmallNeutral80
import sk.mkdigital.androidshowcase.presentation.component.text.labelLarge.TextButtonPrimary
import sk.mkdigital.androidshowcase.presentation.component.text.titleLarge.TextTitleLargePrimary
import sk.mkdigital.androidshowcase.presentation.foundation.AppTheme
import sk.mkdigital.androidshowcase.presentation.foundation.appColorScheme
import sk.mkdigital.androidshowcase.presentation.foundation.space2
import sk.mkdigital.androidshowcase.presentation.foundation.space4

@Composable
fun SignInScreen(
    router: NavRouter<Route>,
    viewModel: SignInViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    SignInNavEvents(router, viewModel.navEvent)
    SignInScreen(
        state = state,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onSignIn = viewModel::signIn,
        onSignUp = viewModel::toSignUp,
        onBiometricAuth = viewModel::authenticateWithBiometrics,
        onFillTestAccount = viewModel::fillTestAccount
    )
}

@Composable
fun SignInScreen(
    state: SignInUiState = SignInUiState(),
    onEmailChange: (String) -> Unit = {},
    onPasswordChange: (String) -> Unit = {},
    onSignIn: () -> Unit = {},
    onSignUp: () -> Unit = {},
    onBiometricAuth: () -> Unit = {},
    onFillTestAccount: () -> Unit = {}
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(space4),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextTitleLargePrimary(stringResource(R.string.sign_in_title))

        Spacer8()

        // Email field
        AppTextField(
            value = state.email,
            onValueChange = onEmailChange,
            modifier = Modifier.fillMaxWidth(),
            label = stringResource(R.string.sign_in_email_label),
            placeholder = stringResource(R.string.sign_in_email_placeholder),
            isError = state.emailError != null,
            supportingText = state.emailError?.let { error ->
                when (error) {
                    EmailError.EMPTY -> stringResource(R.string.sign_in_email_empty)
                    EmailError.INVALID_FORMAT -> stringResource(R.string.sign_in_email_invalid)
                }
            },
            leadingIcon = {
                AppIconNeutral80(imageVector = Icons.Filled.Email, contentDescription = null)
            },
            showClearButton = false,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            )
        )

        Spacer2()

        // Password field
        AppPasswordTextField(
            value = state.password,
            onValueChange = onPasswordChange,
            modifier = Modifier.fillMaxWidth(),
            label = stringResource(R.string.sign_in_password_label),
            placeholder = stringResource(R.string.sign_in_password_placeholder),
            isError = state.passwordError != null,
            supportingText = state.passwordError?.let { error ->
                when (error) {
                    PasswordError.EMPTY -> stringResource(R.string.sign_in_password_empty)
                    PasswordError.TOO_SHORT -> stringResource(R.string.sign_in_password_short)
                    PasswordError.WEAK -> stringResource(R.string.sign_in_password_weak)
                }
            },
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    onSignIn()
                }
            )
        )

        Spacer6()

        state.serverError?.let { error ->
            TextButtonError(text = error.text(), modifier = Modifier.fillMaxWidth())
            Spacer4()
        }

        ContainedButton(
            text = stringResource(R.string.sign_in_button),
            onClick = {
                focusManager.clearFocus()
                onSignIn()
            },
            modifier = Modifier.fillMaxWidth(),
            loading = state.isLoading
        )

        Spacer4()

        // SignUp link
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextBodyMediumNeutral80(stringResource(R.string.sign_in_no_account))
            TextButton(onClick = onSignUp) {
                TextButtonPrimary(stringResource(R.string.sign_in_sign_up))
            }
        }

        // Biometric sign-in
        if (state.biometricsAvailable) {
            Spacer6()

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(space2)
            ) {
                HorizontalDivider(modifier = Modifier.weight(1f))
                TextBodyMediumNeutral80(stringResource(R.string.sign_in_or_divider))
                HorizontalDivider(modifier = Modifier.weight(1f))
            }

            Spacer4()

            if (state.biometricsLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(48.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            } else {
                BiometricView(modifier = Modifier, onClick = onBiometricAuth)
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Test account section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.appColorScheme.neutral20,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(space4)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextBodySmallNeutral80(stringResource(R.string.sign_in_test_account_hint))

                Spacer2()

                TextBodyMediumNeutral80(SignInViewModel.TEST_EMAIL)
                TextBodyMediumNeutral80(SignInViewModel.TEST_PASSWORD)

                Spacer2()

                OutlinedButton(
                    onClick = onFillTestAccount
                ) {
                    TextButtonPrimary(stringResource(R.string.sign_in_test_account_fill))
                }
            }
        }

        Spacer4()
    }
}

@Composable
private fun SignInNavEvents(
    router: NavRouter<Route>,
    navEvent: SharedFlow<NavEvent>
) {
    CollectNavEvents(navEventFlow = navEvent) { event ->
        when (event) {
            is SignInNavEvent.ToHome -> router.navigateTo(
                Route.HomeSection.Home,
                popUpTo = Route.SignIn::class,
                inclusive = true
            )

            is SignInNavEvent.ToSignUp -> router.navigateTo(Route.SignUp)
        }
    }
}

@Preview
@Preview(uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SignInScreenPreview(
    @PreviewParameter(SignInScreenPreviewParams::class) state: SignInUiState
) {
    AppTheme {
        SignInScreen(state = state)
    }
}

internal class SignInScreenPreviewParams : PreviewParameterProvider<SignInUiState> {
    override val values = sequenceOf(
        SignInUiState(email = "test@example.com", password = "Test123!"),
        SignInUiState(biometricsAvailable = true),
        SignInUiState(emailError = EmailError.INVALID_FORMAT, passwordError = PasswordError.TOO_SHORT)
    )
}


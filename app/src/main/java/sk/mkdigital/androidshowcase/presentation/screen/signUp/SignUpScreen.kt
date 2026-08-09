package sk.mkdigital.androidshowcase.presentation.screen.signUp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.MaterialTheme
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
import sk.mkdigital.androidshowcase.presentation.component.buttons.ContainedButton
import sk.mkdigital.androidshowcase.presentation.component.text
import sk.mkdigital.androidshowcase.presentation.component.text.labelLarge.TextButtonError
import sk.mkdigital.androidshowcase.presentation.component.image.AppIconNeutral80
import sk.mkdigital.androidshowcase.presentation.component.spacers.ColumnSpacer.Spacer4
import sk.mkdigital.androidshowcase.presentation.component.spacers.ColumnSpacer.Spacer8
import sk.mkdigital.androidshowcase.presentation.component.text.bodyMedium.TextBodyMediumNeutral80
import sk.mkdigital.androidshowcase.presentation.component.text.labelLarge.TextButtonPrimary
import sk.mkdigital.androidshowcase.presentation.component.text.titleLarge.TextTitleLargePrimary
import sk.mkdigital.androidshowcase.presentation.foundation.AppTheme
import sk.mkdigital.androidshowcase.presentation.foundation.space12
import sk.mkdigital.androidshowcase.presentation.foundation.space2
import sk.mkdigital.androidshowcase.presentation.foundation.space4
import sk.mkdigital.androidshowcase.presentation.foundation.space6

@Composable
fun SignUpScreen(
    router: NavRouter<Route>,
    viewModel: SignUpViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    SignUpNavEvents(router, viewModel.navEvent)
    SignUpScreen(
        state = state,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onConfirmPasswordChange = viewModel::onConfirmPasswordChange,
        onSignUp = viewModel::signUp,
        onSignIn = viewModel::toSignIn
    )
}

@Composable
fun SignUpScreen(
    state: SignUpUiState = SignUpUiState(),
    onEmailChange: (String) -> Unit = {},
    onPasswordChange: (String) -> Unit = {},
    onConfirmPasswordChange: (String) -> Unit = {},
    onSignUp: () -> Unit = {},
    onSignIn: () -> Unit = {}
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
        Spacer8()
        TextTitleLargePrimary(stringResource(R.string.sign_up_title))
        Spacer8()

        // Name field
        Spacer(modifier = Modifier.height(space2))

        // Email field
        AppTextField(
            value = state.email,
            onValueChange = onEmailChange,
            modifier = Modifier.fillMaxWidth(),
            label = stringResource(R.string.sign_up_email_label),
            placeholder = stringResource(R.string.sign_up_email_placeholder),
            isError = state.emailError != null,
            supportingText = state.emailError?.let { error ->
                when (error) {
                    SignUpEmailError.EMPTY -> stringResource(R.string.sign_up_email_empty)
                    SignUpEmailError.INVALID_FORMAT -> stringResource(R.string.sign_up_email_invalid)
                    SignUpEmailError.ALREADY_EXISTS -> stringResource(R.string.sign_up_email_already_exists)
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

        Spacer(modifier = Modifier.height(space2))

        // Password field
        AppPasswordTextField(
            value = state.password,
            onValueChange = onPasswordChange,
            modifier = Modifier.fillMaxWidth(),
            label = stringResource(R.string.sign_up_password_label),
            placeholder = stringResource(R.string.sign_up_password_placeholder),
            isError = state.passwordError != null,
            supportingText = state.passwordError?.let { error ->
                when (error) {
                    SignUpPasswordError.EMPTY -> stringResource(R.string.sign_up_password_empty)
                    SignUpPasswordError.TOO_SHORT -> stringResource(R.string.sign_up_password_short)
                    SignUpPasswordError.WEAK -> stringResource(R.string.sign_up_password_weak)
                }
            },
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            )
        )

        Spacer(modifier = Modifier.height(space2))

        // Confirm Password field
        AppPasswordTextField(
            value = state.confirmPassword,
            onValueChange = onConfirmPasswordChange,
            modifier = Modifier.fillMaxWidth(),
            label = stringResource(R.string.sign_up_confirm_password_label),
            placeholder = stringResource(R.string.sign_up_confirm_password_placeholder),
            isError = state.confirmPasswordError != null,
            supportingText = state.confirmPasswordError?.let { error ->
                when (error) {
                    SignUpConfirmPasswordError.EMPTY -> stringResource(R.string.sign_up_confirm_password_empty)
                    SignUpConfirmPasswordError.MISMATCH -> stringResource(R.string.sign_up_confirm_password_mismatch)
                }
            },
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    onSignUp()
                }
            )
        )

        Spacer(modifier = Modifier.height(space6))

        state.error?.let { error ->
            TextButtonError(text = error.text(), modifier = Modifier.fillMaxWidth())
            Spacer4()
        }

        ContainedButton(
                text = stringResource(R.string.sign_up_button),
                onClick = {
                    focusManager.clearFocus()
                    onSignUp()
                },
            modifier = Modifier.fillMaxWidth(),
            loading = state.isLoading
        )

        Spacer4()

        // SignIn link
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextBodyMediumNeutral80(stringResource(R.string.sign_up_has_account))
            TextButton(onClick = onSignIn) {
                TextButtonPrimary(stringResource(R.string.sign_up_sign_in))
            }
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
private fun SignUpNavEvents(
    router: NavRouter<Route>,
    navEvent: SharedFlow<NavEvent>
) {
    CollectNavEvents(navEventFlow = navEvent) { event ->
        when (event) {
            is SignUpNavEvent.ToHome -> router.navigateTo(
                Route.HomeSection.Home,
                popUpTo = Route.SignUp::class,
                inclusive = true
            )

            is SignUpNavEvent.ToSignIn -> router.onBack()
        }
    }
}

@Preview
@Preview(uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SignUpScreenPreview(
    @PreviewParameter(SignUpScreenPreviewParams::class) state: SignUpUiState
) {
    AppTheme {
        SignUpScreen(state = state)
    }
}

internal class SignUpScreenPreviewParams : PreviewParameterProvider<SignUpUiState> {
    override val values = sequenceOf(
        SignUpUiState(),
        SignUpUiState(
            email = "john@example.com",
            password = "Test123!",
            confirmPassword = "Test123!"
        ),
        SignUpUiState(isLoading = true),
        SignUpUiState(
            emailError = SignUpEmailError.INVALID_FORMAT
        )
    )
}

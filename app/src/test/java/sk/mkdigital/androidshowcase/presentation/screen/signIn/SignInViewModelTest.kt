package sk.mkdigital.androidshowcase.presentation.screen.signIn

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import sk.mkdigital.androidshowcase.domain.model.AuthSession
import sk.mkdigital.androidshowcase.domain.useCase.auth.SignInUseCase
import sk.mkdigital.androidshowcase.domain.useCase.auth.SignInWithTokenUseCase
import sk.mkdigital.androidshowcase.domain.useCase.biometric.AuthenticateWithBiometricUseCase
import sk.mkdigital.androidshowcase.domain.useCase.biometric.IsBiometricEnabledUseCase
import sk.mkdigital.androidshowcase.fake.NoOpLogger
import sk.mkdigital.androidshowcase.presentation.base.BaseViewModelTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SignInViewModelTest : BaseViewModelTest<SignInViewModel>() {

    override lateinit var classUnderTest: SignInViewModel

    private val signInUseCase = mockk<SignInUseCase>()
    private val signInWithTokenUseCase = mockk<SignInWithTokenUseCase>()
    private val isBiometricEnabledUseCase = mockk<IsBiometricEnabledUseCase>()
    private val authenticateWithBiometricUseCase = mockk<AuthenticateWithBiometricUseCase>()

    override fun beforeEach() {
        coEvery { signInUseCase(any()) } returns
            AuthSession(token = "token", userId = 1L, email = "test@example.com", name = "Test")
        classUnderTest = SignInViewModel(
            signInUseCase = signInUseCase,
            signInWithTokenUseCase = signInWithTokenUseCase,
            isBiometricEnabledUseCase = isBiometricEnabledUseCase,
            authenticateWithBiometricUseCase = authenticateWithBiometricUseCase,
        ).apply { logger = NoOpLogger }
    }

    @Test
    fun `default state has empty email`() {
        assertEquals("", classUnderTest.state.value.email)
    }

    @Test
    fun `default state has empty password`() {
        assertEquals("", classUnderTest.state.value.password)
    }

    @Test
    fun `default state has no errors`() {
        assertNull(classUnderTest.state.value.emailError)
        assertNull(classUnderTest.state.value.passwordError)
    }

    @Test
    fun `default state has biometrics unavailable`() {
        assertFalse(classUnderTest.state.value.biometricsAvailable)
    }

    @Test
    fun `default state has biometrics not loading`() {
        assertFalse(classUnderTest.state.value.biometricsLoading)
    }

    @Test
    fun `onEmailChange updates email`() {
        classUnderTest.onEmailChange("test@example.com")

        assertEquals("test@example.com", classUnderTest.state.value.email)
    }

    @Test
    fun `onEmailChange clears email error`() {
        classUnderTest.signIn() // Triggers validation error for empty email

        classUnderTest.onEmailChange("test@example.com")

        assertNull(classUnderTest.state.value.emailError)
    }

    @Test
    fun `onPasswordChange updates password`() {
        classUnderTest.onPasswordChange("Test123!")

        assertEquals("Test123!", classUnderTest.state.value.password)
    }

    @Test
    fun `onPasswordChange clears password error`() {
        classUnderTest.signIn() // Triggers validation error for empty password

        classUnderTest.onPasswordChange("Test123!")

        assertNull(classUnderTest.state.value.passwordError)
    }

    @Test
    fun `fillTestAccount sets test email`() {
        classUnderTest.fillTestAccount()

        assertEquals(SignInViewModel.TEST_EMAIL, classUnderTest.state.value.email)
    }

    @Test
    fun `fillTestAccount sets test password`() {
        classUnderTest.fillTestAccount()

        assertEquals(SignInViewModel.TEST_PASSWORD, classUnderTest.state.value.password)
    }

    @Test
    fun `fillTestAccount clears errors`() {
        classUnderTest.signIn() // Triggers validation errors

        classUnderTest.fillTestAccount()

        assertNull(classUnderTest.state.value.emailError)
        assertNull(classUnderTest.state.value.passwordError)
    }

    @Test
    fun `sign in with empty email shows EMPTY error`() {
        classUnderTest.onPasswordChange("Test123!")

        classUnderTest.signIn()

        assertEquals(EmailError.EMPTY, classUnderTest.state.value.emailError)
    }

    @Test
    fun `sign in with invalid email format shows INVALID_FORMAT error`() {
        classUnderTest.onEmailChange("invalid-email")
        classUnderTest.onPasswordChange("Test123!")

        classUnderTest.signIn()

        assertEquals(EmailError.INVALID_FORMAT, classUnderTest.state.value.emailError)
    }

    @Test
    fun `sign in with valid email clears email error`() {
        classUnderTest.onEmailChange("test@example.com")
        classUnderTest.onPasswordChange("Test123!")

        classUnderTest.signIn()

        assertNull(classUnderTest.state.value.emailError)
    }

    @Test
    fun `sign in with empty password shows EMPTY error`() {
        classUnderTest.onEmailChange("test@example.com")

        classUnderTest.signIn()

        assertEquals(PasswordError.EMPTY, classUnderTest.state.value.passwordError)
    }

    @Test
    fun `sign in with short password shows TOO_SHORT error`() {
        classUnderTest.onEmailChange("test@example.com")
        classUnderTest.onPasswordChange("Test1!")

        classUnderTest.signIn()

        assertEquals(PasswordError.TOO_SHORT, classUnderTest.state.value.passwordError)
    }

    @Test
    fun `sign in with weak password shows WEAK error`() {
        classUnderTest.onEmailChange("test@example.com")
        classUnderTest.onPasswordChange("testtest") // No uppercase, digit, or special char

        classUnderTest.signIn()

        assertEquals(PasswordError.WEAK, classUnderTest.state.value.passwordError)
    }

    @Test
    fun `sign in with valid credentials clears all errors`() {
        classUnderTest.onEmailChange("test@example.com")
        classUnderTest.onPasswordChange("Test123!")

        classUnderTest.signIn()

        assertNull(classUnderTest.state.value.emailError)
        assertNull(classUnderTest.state.value.passwordError)
    }

    @Test
    fun `email without at symbol is invalid`() {
        classUnderTest.onEmailChange("testexample.com")
        classUnderTest.onPasswordChange("Test123!")

        classUnderTest.signIn()

        assertEquals(EmailError.INVALID_FORMAT, classUnderTest.state.value.emailError)
    }

    @Test
    fun `email without domain is invalid`() {
        classUnderTest.onEmailChange("test@")
        classUnderTest.onPasswordChange("Test123!")

        classUnderTest.signIn()

        assertEquals(EmailError.INVALID_FORMAT, classUnderTest.state.value.emailError)
    }

    @Test
    fun `email with valid format is accepted`() {
        classUnderTest.onEmailChange("user.name+tag@example.co.uk")
        classUnderTest.onPasswordChange("Test123!")

        classUnderTest.signIn()

        assertNull(classUnderTest.state.value.emailError)
    }

    @Test
    fun `password without uppercase is weak`() {
        classUnderTest.onEmailChange("test@example.com")
        classUnderTest.onPasswordChange("test123!")

        classUnderTest.signIn()

        assertEquals(PasswordError.WEAK, classUnderTest.state.value.passwordError)
    }

    @Test
    fun `password without lowercase is weak`() {
        classUnderTest.onEmailChange("test@example.com")
        classUnderTest.onPasswordChange("TEST123!")

        classUnderTest.signIn()

        assertEquals(PasswordError.WEAK, classUnderTest.state.value.passwordError)
    }

    @Test
    fun `password without digit is weak`() {
        classUnderTest.onEmailChange("test@example.com")
        classUnderTest.onPasswordChange("TestTest!")

        classUnderTest.signIn()

        assertEquals(PasswordError.WEAK, classUnderTest.state.value.passwordError)
    }

    @Test
    fun `password without special character is weak`() {
        classUnderTest.onEmailChange("test@example.com")
        classUnderTest.onPasswordChange("Test1234")

        classUnderTest.signIn()

        assertEquals(PasswordError.WEAK, classUnderTest.state.value.passwordError)
    }

    @Test
    fun `strong password is accepted`() {
        classUnderTest.onEmailChange("test@example.com")
        classUnderTest.onPasswordChange("StrongP@ss1")

        classUnderTest.signIn()

        assertNull(classUnderTest.state.value.passwordError)
    }

    @Test
    fun `SignInUiState default values are correct`() {
        val state = SignInUiState()
        assertEquals("", state.email)
        assertEquals("", state.password)
        assertNull(state.emailError)
        assertNull(state.passwordError)
        assertFalse(state.biometricsAvailable)
        assertFalse(state.biometricsLoading)
    }

    @Test
    fun `EmailError has EMPTY value`() {
        assertEquals(EmailError.EMPTY, EmailError.valueOf("EMPTY"))
    }

    @Test
    fun `EmailError has INVALID_FORMAT value`() {
        assertEquals(EmailError.INVALID_FORMAT, EmailError.valueOf("INVALID_FORMAT"))
    }

    @Test
    fun `PasswordError has EMPTY value`() {
        assertEquals(PasswordError.EMPTY, PasswordError.valueOf("EMPTY"))
    }

    @Test
    fun `PasswordError has TOO_SHORT value`() {
        assertEquals(PasswordError.TOO_SHORT, PasswordError.valueOf("TOO_SHORT"))
    }

    @Test
    fun `PasswordError has WEAK value`() {
        assertEquals(PasswordError.WEAK, PasswordError.valueOf("WEAK"))
    }

    @Test
    fun `TEST_EMAIL is valid email format`() {
        assertTrue(SignInViewModel.TEST_EMAIL.contains("@"))
        assertTrue(SignInViewModel.TEST_EMAIL.contains("."))
    }

    @Test
    fun `TEST_PASSWORD meets all requirements`() {
        val password = SignInViewModel.TEST_PASSWORD
        assertTrue(password.length >= 8)
        assertTrue(password.any { it.isUpperCase() })
        assertTrue(password.any { it.isLowerCase() })
        assertTrue(password.any { it.isDigit() })
        assertTrue(password.any { !it.isLetterOrDigit() })
    }
}

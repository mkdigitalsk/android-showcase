package sk.mkdigital.androidshowcase.presentation.screen.signUp

import app.cash.turbine.test
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import sk.mkdigital.androidshowcase.domain.useCase.auth.SignUpUseCase
import sk.mkdigital.androidshowcase.presentation.base.BaseViewModelTest

@OptIn(ExperimentalCoroutinesApi::class)
class SignUpViewModelTest : BaseViewModelTest<SignUpViewModel>() {

    override lateinit var classUnderTest: SignUpViewModel

    private val signUpUseCase = mockk<SignUpUseCase>()

    override fun beforeEach() {
        classUnderTest = SignUpViewModel(signUpUseCase = signUpUseCase)
    }

    @Test
    fun `openTerms opens the terms of use`() = runTest {
        classUnderTest.navEvent.test {
            classUnderTest.openTerms()

            assertEquals(SignUpNavEvent.OpenTerms("https://showcase.mkdigital.sk/terms"), awaitItem())
        }
    }

    @Test
    fun `openPrivacy opens the privacy notice`() = runTest {
        classUnderTest.navEvent.test {
            classUnderTest.openPrivacy()

            assertEquals(SignUpNavEvent.OpenPrivacy("https://showcase.mkdigital.sk/privacy"), awaitItem())
        }
    }
}

package sk.mkdigital.androidshowcase.presentation.screen.settings

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import sk.mkdigital.androidshowcase.domain.exceptions.base.NetworkException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import sk.mkdigital.androidshowcase.domain.exceptions.base.ApiException
import sk.mkdigital.androidshowcase.domain.exceptions.base.DataException
import sk.mkdigital.androidshowcase.domain.model.User
import sk.mkdigital.androidshowcase.domain.useCase.analytics.RecordExceptionUseCase
import sk.mkdigital.androidshowcase.domain.useCase.auth.ClearLocalUserDataUseCase
import sk.mkdigital.androidshowcase.domain.useCase.auth.DeleteAccountUseCase
import sk.mkdigital.androidshowcase.domain.useCase.auth.GetCurrentUserUseCase
import sk.mkdigital.androidshowcase.domain.useCase.base.None
import sk.mkdigital.androidshowcase.domain.useCase.settings.GetThemeModeUseCase
import sk.mkdigital.androidshowcase.domain.useCase.settings.SetThemeModeUseCase
import sk.mkdigital.androidshowcase.fake.NoOpLogger
import sk.mkdigital.androidshowcase.presentation.base.BaseViewModelTest
import sk.mkdigital.androidshowcase.presentation.base.NavEvent

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest : BaseViewModelTest<SettingsViewModel>() {

    override lateinit var classUnderTest: SettingsViewModel

    private val getThemeModeUseCase = mockk<GetThemeModeUseCase>()
    private val setThemeModeUseCase = mockk<SetThemeModeUseCase>()
    private val recordExceptionUseCase = mockk<RecordExceptionUseCase>()
    private val clearLocalUserDataUseCase = mockk<ClearLocalUserDataUseCase>()
    private val deleteAccountUseCase = mockk<DeleteAccountUseCase>()
    private val getCurrentUserUseCase = mockk<GetCurrentUserUseCase>()

    private val demo = User(id = 1, email = "test01@mkdigital.sk", isDemo = true)
    private val normal = User(id = 2, email = "someone@mkdigital.sk", isDemo = false)

    private val steps = mutableListOf<String>()

    override fun beforeEach() {
        coEvery { deleteAccountUseCase(any()) } coAnswers { steps += DELETE }
        coEvery { clearLocalUserDataUseCase(any()) } coAnswers { steps += CLEAR }
        classUnderTest = SettingsViewModel(
            getThemeModeUseCase = getThemeModeUseCase,
            setThemeModeUseCase = setThemeModeUseCase,
            recordExceptionUseCase = recordExceptionUseCase,
            clearLocalUserDataUseCase = clearLocalUserDataUseCase,
            deleteAccountUseCase = deleteAccountUseCase,
            getCurrentUserUseCase = getCurrentUserUseCase,
        ).apply {
            logger = NoOpLogger
            trackScreenUseCase = mockk(relaxed = true)
        }
    }

    @Test
    fun `default state hides the delete account dialog`() {
        assertFalse(classUnderTest.state.value.showDeleteAccountDialog)
        assertFalse(classUnderTest.state.value.isDeletingAccount)
        assertFalse(classUnderTest.state.value.deleteAccountFailed)
    }

    @Test
    fun `the demo flag the server sent reaches the state`() = runTest {
        coEvery { getCurrentUserUseCase(None) } returns demo

        classUnderTest.afterInit()

        assertEquals(true, classUnderTest.state.value.isDemoAccount)
    }

    @Test
    fun `a normal account leaves the state unflagged`() = runTest {
        coEvery { getCurrentUserUseCase(None) } returns normal

        classUnderTest.afterInit()

        assertEquals(false, classUnderTest.state.value.isDemoAccount)
    }

    @Test
    fun `an account that cannot be read is offered nothing rather than the wrong thing`() = runTest {
        coEvery { getCurrentUserUseCase(None) } throws NetworkException(message = "offline")

        classUnderTest.afterInit()

        assertNull(
            classUnderTest.state.value.isDemoAccount,
            "false would offer a demo account a control the server refuses",
        )
    }

    @Test
    fun `deleting the account calls the endpoint then clears local data then navigates`() = runTest {
        val events = recordNavEvents()

        classUnderTest.deleteAccount()

        assertEquals(listOf(DELETE, CLEAR), steps)
        assertEquals(listOf<NavEvent>(SettingNavEvents.SignOut), events)
    }

    @Test
    fun `a failed local teardown does not report the deletion as failed`() = runTest {
        coEvery { clearLocalUserDataUseCase(any()) } throws DataException(message = "disk full")
        val events = recordNavEvents()

        classUnderTest.deleteAccount()

        assertTrue(events.isNotEmpty(), "the server erased the account, so the person must not be left on it")
        assertFalse(classUnderTest.state.value.deleteAccountFailed, "the deletion did not fail")
    }

    @Test
    fun `deleting the account is pending while the endpoint is in flight`() = runTest {
        var pendingDuringCall = false
        coEvery { deleteAccountUseCase(any()) } coAnswers {
            pendingDuringCall = classUnderTest.state.value.isDeletingAccount
        }

        classUnderTest.deleteAccount()

        assertTrue(pendingDuringCall)
        assertFalse(classUnderTest.state.value.isDeletingAccount)
    }

    @Test
    fun `a failed delete surfaces the failure and keeps local data`() = runTest {
        coEvery { deleteAccountUseCase(any()) } throws ApiException(httpCode = 500, message = "HTTP error")
        val events = recordNavEvents()

        classUnderTest.deleteAccount()

        assertTrue(classUnderTest.state.value.deleteAccountFailed, "no failure message")
        assertFalse(classUnderTest.state.value.isDeletingAccount)
        coVerify(exactly = 0) { clearLocalUserDataUseCase(any()) }
        assertTrue(events.isEmpty(), "navigated away")
    }

    @Test
    fun `signing out clears local data before it navigates`() = runTest {
        val events = recordNavEvents()

        classUnderTest.signOut()

        assertEquals(listOf(CLEAR), steps)
        assertEquals(listOf<NavEvent>(SettingNavEvents.SignOut), events)
    }

    @Test
    fun `showing the delete account dialog drops a previous failure`() = runTest {
        coEvery { deleteAccountUseCase(any()) } throws ApiException(httpCode = 500, message = "HTTP error")
        classUnderTest.deleteAccount()

        classUnderTest.showDeleteAccountDialog()

        assertTrue(classUnderTest.state.value.showDeleteAccountDialog)
        assertFalse(classUnderTest.state.value.deleteAccountFailed)
    }

    @Test
    fun `hiding the delete account dialog closes it`() {
        classUnderTest.showDeleteAccountDialog()

        classUnderTest.hideDeleteAccountDialog()

        assertFalse(classUnderTest.state.value.showDeleteAccountDialog)
    }

    private fun TestScope.recordNavEvents(): List<NavEvent> {
        val events = mutableListOf<NavEvent>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            classUnderTest.navEvent.collect { events += it }
        }
        return events
    }

    private companion object {
        const val DELETE = "delete"
        const val CLEAR = "clear"
    }
}

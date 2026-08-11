package sk.mkdigital.androidshowcase.domain.useCase.auth

import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import sk.mkdigital.androidshowcase.base.BaseTest
import sk.mkdigital.androidshowcase.data.client.AuthClient
import sk.mkdigital.androidshowcase.data.local.StorageLocalStoreImpl
import sk.mkdigital.androidshowcase.data.local.preferences.PersistentPreferencesImpl
import sk.mkdigital.androidshowcase.data.local.preferences.SessionPreferencesImpl
import sk.mkdigital.androidshowcase.data.notification.NotificationRepositoryImpl
import sk.mkdigital.androidshowcase.data.repository.AuthRepositoryImpl
import sk.mkdigital.androidshowcase.data.repository.SettingsRepositoryImpl
import sk.mkdigital.androidshowcase.data.repository.storage.StorageRepositoryImpl
import sk.mkdigital.androidshowcase.domain.model.Note
import sk.mkdigital.androidshowcase.domain.model.StorageData
import sk.mkdigital.androidshowcase.domain.useCase.base.invoke
import sk.mkdigital.androidshowcase.fake.FakeNoteRepository
import sk.mkdigital.androidshowcase.fake.FakePreferences
import sk.mkdigital.androidshowcase.presentation.foundation.ThemeMode

/**
 * Real preferences and repositories over fake stores: the point is which keys survive, and a faked
 * repository would answer that question with the test's own opinion.
 */
class ClearLocalUserDataUseCaseTest : BaseTest<ClearLocalUserDataUseCase>() {

    override lateinit var classUnderTest: ClearLocalUserDataUseCase

    @MockK
    private lateinit var authClient: AuthClient

    private val persistentPreferences = PersistentPreferencesImpl(FakePreferences())
    private val sessionPreferences = SessionPreferencesImpl(FakePreferences())
    private val storageLocalStore = StorageLocalStoreImpl(sessionPreferences, persistentPreferences)
    private val settingsRepository = SettingsRepositoryImpl(persistentPreferences)
    private val noteRepository = FakeNoteRepository()

    override fun beforeEach() {
        classUnderTest = ClearLocalUserDataUseCase(
            authRepository = AuthRepositoryImpl(authClient, persistentPreferences),
            noteRepository = noteRepository,
            storageRepository = StorageRepositoryImpl(storageLocalStore),
            notificationRepository = NotificationRepositoryImpl(persistentPreferences),
        )
    }

    @Test
    fun `clearing removes the auth token`() = runTest {
        signInAndUseTheApp()

        classUnderTest()

        assertNull(persistentPreferences.getToken())
    }

    @Test
    fun `clearing removes the notes saved on the device`() = runTest {
        signInAndUseTheApp()

        classUnderTest()

        assertEquals(0L, noteRepository.count())
    }

    @Test
    fun `clearing zeroes the persistent and session counters`() = runTest {
        signInAndUseTheApp()

        classUnderTest()

        assertEquals(0, persistentPreferences.getPersistentCounter())
        assertEquals(0, sessionPreferences.getSessionCounter())
        assertEquals(StorageData(), storageLocalStore.data.first())
    }

    @Test
    fun `clearing removes the stored push token`() = runTest {
        signInAndUseTheApp()

        classUnderTest()

        assertNull(persistentPreferences.getFcmToken())
    }

    @Test
    fun `a store that cannot be cleared still ends the session`() = runTest {
        signInAndUseTheApp()
        noteRepository.failOnDeleteAll = true

        classUnderTest()

        assertNull(persistentPreferences.getToken(), "a surviving token leaves the person signed in")
    }

    @Test
    fun `clearing keeps the theme mode`() = runTest {
        signInAndUseTheApp()

        classUnderTest()

        assertEquals(ThemeMode.DARK, settingsRepository.getThemeMode())
    }

    private suspend fun signInAndUseTheApp() {
        persistentPreferences.setToken("auth-token")
        persistentPreferences.setFcmToken("fcm-token")
        persistentPreferences.setPersistentCounter(3)
        persistentPreferences.setThemeMode(ThemeMode.DARK.name)
        sessionPreferences.setSessionCounter(2)
        noteRepository.insert(Note(id = 1L, title = "Groceries", content = "milk", createdAt = 0L))
        storageLocalStore.load()
    }
}

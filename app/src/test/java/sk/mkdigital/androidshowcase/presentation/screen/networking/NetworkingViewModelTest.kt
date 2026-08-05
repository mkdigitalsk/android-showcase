package sk.mkdigital.androidshowcase.presentation.screen.networking

import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import sk.mkdigital.androidshowcase.domain.model.User
import sk.mkdigital.androidshowcase.domain.useCase.GetUsersUseCase
import sk.mkdigital.androidshowcase.domain.useCase.base.None
import sk.mkdigital.androidshowcase.fake.NoOpLogger
import sk.mkdigital.androidshowcase.presentation.base.BaseViewModelTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NetworkingViewModelTest : BaseViewModelTest<NetworkingViewModel>() {

    override lateinit var classUnderTest: NetworkingViewModel

    private val getUsersUseCase = mockk<GetUsersUseCase>()

    private val alice = User(id = 1, email = "alice@mk.sk", name = "Alice")

    override fun beforeEach() {
        classUnderTest = NetworkingViewModel(getUsersUseCase).apply { logger = NoOpLogger }
    }

    @Test
    fun `fetchUsers maps domain users into UI models`() = runTest {
        coEvery { getUsersUseCase(None) } returns listOf(alice)

        classUnderTest.fetchUsers()

        val state = classUnderTest.state.value
        assertFalse(state.isLoading)
        assertEquals(listOf(UserUiModel(id = 1, name = "Alice", email = "alice@mk.sk")), state.users)
    }

    @Test
    fun `fetchUsers failure sets error and stops loading`() = runTest {
        coEvery { getUsersUseCase(None) } throws RuntimeException("boom")

        classUnderTest.fetchUsers()

        val state = classUnderTest.state.value
        assertFalse(state.isLoading)
        assertNotNull(state.error)
    }

    @Test
    fun `fetchUsers emits loading then success`() = runTest {
        val gate = CompletableDeferred<Unit>()
        coEvery { getUsersUseCase(None) } coAnswers { gate.await(); listOf(alice) }

        classUnderTest.state.test {
            assertEquals(NetworkingUiState(), awaitItem())
            classUnderTest.fetchUsers()
            assertTrue(awaitItem().isLoading)
            gate.complete(Unit)
            assertFalse(awaitItem().isLoading)
        }
    }
}

package sk.mkdigital.androidshowcase.data.repository.user

import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import sk.mkdigital.androidshowcase.base.BaseTest
import sk.mkdigital.androidshowcase.data.dto.user.ThemeModeDTO
import sk.mkdigital.androidshowcase.data.dto.user.UserResponseDTO
import sk.mkdigital.androidshowcase.domain.exceptions.base.ApiException
import sk.mkdigital.androidshowcase.util.suspendRunCatching

class UserRepositoryImplTest : BaseTest<UserRepositoryImpl>() {

    override lateinit var classUnderTest: UserRepositoryImpl

    @MockK
    private lateinit var client: UserClient

    override fun beforeEach() {
        classUnderTest = UserRepositoryImpl(client)
    }

    @Test
    fun `the current user carries the demo flag the server sent`() = runTest {
        coEvery { client.fetchMe() } returns me(demo = true)
        assertTrue(classUnderTest.getCurrentUser().isDemo)

        coEvery { client.fetchMe() } returns me(demo = false)
        assertFalse(classUnderTest.getCurrentUser().isDemo)
    }

    @Test
    fun `deleting an account rethrows a not found`() = runTest {
        coEvery { client.deleteMe() } throws notFound()

        val thrown = suspendRunCatching { classUnderTest.deleteAccount() }.exceptionOrNull()

        assertInstanceOf(
            ApiException::class.java,
            thrown,
            "the route answers 204 whether or not the row was there, so a 404 is a route that is not there",
        )
    }

    @Test
    fun `deleting an account rethrows a server failure`() = runTest {
        coEvery { client.deleteMe() } throws serverError()

        val thrown = suspendRunCatching { classUnderTest.deleteAccount() }.exceptionOrNull()

        assertInstanceOf(ApiException::class.java, thrown)
        assertEquals(500, (thrown as ApiException).httpCode)
    }

    private fun me(demo: Boolean) = UserResponseDTO(
        id = 1,
        email = "test01@mkdigital.sk",
        createdAt = 0,
        themeMode = ThemeModeDTO.SYSTEM,
        locale = "en",
        demo = demo,
    )

    private fun notFound() = ApiException(httpCode = 404, message = "HTTP error: Not Found")

    private fun serverError() = ApiException(httpCode = 500, message = "HTTP error: Server Error")
}

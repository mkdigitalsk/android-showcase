package com.mk.androidshowcase.presentation.screen.networking

import io.mockk.impl.annotations.MockK
import com.mk.androidshowcase.domain.useCase.GetUsersUseCase
import com.mk.androidshowcase.presentation.base.BaseViewModelTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class NetworkingViewModelTest : BaseViewModelTest<NetworkingViewModel>() {

    override lateinit var classUnderTest: NetworkingViewModel

    @MockK
    private lateinit var getUsersUseCase: GetUsersUseCase

    override fun beforeEach() {
        classUnderTest = NetworkingViewModel(getUsersUseCase)
    }

    @Test
    fun `default state has empty users list`() {
        assertTrue(classUnderTest.state.value.users.isEmpty())
    }

    @Test
    fun `default state is not loading`() {
        assertFalse(classUnderTest.state.value.isLoading)
    }

    @Test
    fun `default state has no error`() {
        assertNull(classUnderTest.state.value.error)
    }

    @Test
    fun `NetworkingUiState default values are correct`() {
        val state = NetworkingUiState()
        assertFalse(state.isLoading)
        assertTrue(state.users.isEmpty())
        assertNull(state.error)
    }

    @Test
    fun `NetworkingUiState can hold users`() {
        val users = listOf(createTestUserUiModel(1), createTestUserUiModel(2))
        val state = NetworkingUiState(users = users)
        assertEquals(2, state.users.size)
    }

    @Test
    fun `NetworkingUiState can hold error`() {
        val state = NetworkingUiState(error = "Network error")
        assertEquals("Network error", state.error)
    }

    @Test
    fun `NetworkingUiState can have loading state`() {
        val state = NetworkingUiState(isLoading = true)
        assertTrue(state.isLoading)
    }

    private fun createTestUserUiModel(id: Long = 1, name: String = "John Doe") = UserUiModel(
        id = id,
        name = name,
        email = "john@example.com",
    )
}

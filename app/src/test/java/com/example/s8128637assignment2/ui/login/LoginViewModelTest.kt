package com.example.s8128637assignment2.ui.login

import com.example.s8128637assignment2.MainDispatcherRule
import com.example.s8128637assignment2.data.ApiResult
import com.example.s8128637assignment2.data.repository.AuthRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class LoginViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val authRepository: AuthRepository = mockk()

    @Test
    fun `login with blank fields emits error without calling repository`() = runTest {
        val viewModel = LoginViewModel(authRepository)

        viewModel.login("", "password")
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is LoginUiState.Error)
    }

    @Test
    fun `successful login emits keypass`() = runTest {
        coEvery { authRepository.login("s1234567", "Jacob") } returns ApiResult.Success("topicName")
        val viewModel = LoginViewModel(authRepository)

        viewModel.login("s1234567", "Jacob")
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is LoginUiState.Success)
        assertEquals("topicName", (state as LoginUiState.Success).keypass)
    }

    @Test
    fun `failed login emits error message from repository`() = runTest {
        coEvery { authRepository.login(any(), any()) } returns ApiResult.Error("Invalid username or password")
        val viewModel = LoginViewModel(authRepository)

        viewModel.login("s1234567", "wrong")
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is LoginUiState.Error)
        assertEquals("Invalid username or password", (state as LoginUiState.Error).message)
    }
}

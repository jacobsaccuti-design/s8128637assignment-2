package com.example.s8128637assignment2.ui.dashboard

import com.example.s8128637assignment2.MainDispatcherRule
import com.example.s8128637assignment2.data.ApiResult
import com.example.s8128637assignment2.data.remote.dto.DashboardResponse
import com.example.s8128637assignment2.data.repository.DashboardRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class DashboardViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val dashboardRepository: DashboardRepository = mockk()

    @Test
    fun `successful load with entities emits Success state`() = runTest {
        val entities = listOf(mapOf("property1" to "value1", "description" to "desc"))
        coEvery { dashboardRepository.getDashboard("topic") } returns
            ApiResult.Success(DashboardResponse(entities, 1))
        val viewModel = DashboardViewModel(dashboardRepository)

        viewModel.loadDashboard("topic")
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is DashboardUiState.Success)
        assertEquals(1, (state as DashboardUiState.Success).entityTotal)
    }

    @Test
    fun `successful load with no entities emits Empty state`() = runTest {
        coEvery { dashboardRepository.getDashboard("topic") } returns
            ApiResult.Success(DashboardResponse(emptyList(), 0))
        val viewModel = DashboardViewModel(dashboardRepository)

        viewModel.loadDashboard("topic")
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value is DashboardUiState.Empty)
    }

    @Test
    fun `failed load emits Error state`() = runTest {
        coEvery { dashboardRepository.getDashboard("topic") } returns
            ApiResult.Error("Unable to reach the server. Check your connection.")
        val viewModel = DashboardViewModel(dashboardRepository)

        viewModel.loadDashboard("topic")
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is DashboardUiState.Error)
        assertEquals("Unable to reach the server. Check your connection.", (state as DashboardUiState.Error).message)
    }
}

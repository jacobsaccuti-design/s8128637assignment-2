package com.example.s8128637assignment2.ui.dashboard

sealed class DashboardUiState {
    data object Loading : DashboardUiState()
    data object Empty : DashboardUiState()
    data class Success(val entities: List<Map<String, Any>>, val entityTotal: Int) : DashboardUiState()
    data class Error(val message: String) : DashboardUiState()
}

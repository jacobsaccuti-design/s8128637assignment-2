package com.example.s8128637assignment2.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.s8128637assignment2.data.ApiResult
import com.example.s8128637assignment2.data.repository.DashboardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val dashboardRepository: DashboardRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<DashboardUiState>(DashboardUiState.Loading)
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    fun loadDashboard(keypass: String) {
        viewModelScope.launch {
            _uiState.value = DashboardUiState.Loading
            when (val result = dashboardRepository.getDashboard(keypass)) {
                is ApiResult.Success -> {
                    _uiState.value = if (result.data.entities.isEmpty()) {
                        DashboardUiState.Empty
                    } else {
                        DashboardUiState.Success(result.data.entities, result.data.entityTotal)
                    }
                }
                is ApiResult.Error -> _uiState.value = DashboardUiState.Error(result.message)
            }
        }
    }
}

package com.example.s8128637assignment2.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.s8128637assignment2.data.ApiResult
import com.example.s8128637assignment2.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun login(username: String, password: String) {
        if (username.isBlank() || password.isBlank()) {
            _uiState.value = LoginUiState.Error("Username and password are required")
            return
        }

        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading
            when (val result = authRepository.login(username.trim(), password)) {
                is ApiResult.Success -> _uiState.value = LoginUiState.Success(result.data)
                is ApiResult.Error -> _uiState.value = LoginUiState.Error(result.message)
            }
        }
    }
}

package com.example.infoquizapp.presentation.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.infoquizapp.domain.auth.usecases.LoginUseCase
import com.example.infoquizapp.domain.auth.usecases.RegisterUseCase
import com.example.infoquizapp.domain.auth.usecases.TeacherLoginUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class AuthUiState {
    object Idle : AuthUiState()
    object Loading : AuthUiState()
    data class Success(val token: String) : AuthUiState()
    data class Error(val message: String) : AuthUiState()
}

class AuthViewModel(
    private val registerUseCase: RegisterUseCase,
    private val loginUseCase: LoginUseCase,
    private val teacherLoginUseCase: TeacherLoginUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState

    fun register(username: String, email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            val result = registerUseCase(username, email, password)
            _uiState.value = if (result.token != null) {
                AuthUiState.Success(result.token)
            } else {
                AuthUiState.Error(result.error ?: "Неизвестная ошибка")
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            val result = loginUseCase(email, password)
            _uiState.value = if (result.token != null) {
                AuthUiState.Success(result.token)
            } else {
                AuthUiState.Error(result.error ?: "Неизвестная ошибка")
            }
        }
    }

    fun teacherLogin(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            val result = teacherLoginUseCase(email, password)
            _uiState.value = if (result.token != null) {
                AuthUiState.Success(result.token)
            } else {
                AuthUiState.Error(result.error ?: "Неизвестная ошибка")
            }
        }
    }

    fun logout() {
        _uiState.value = AuthUiState.Idle
    }
}
package com.example.infoquizapp.presentation.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.infoquizapp.data.profile.model.UserOut
import com.example.infoquizapp.data.profile.network.Response
import com.example.infoquizapp.domain.profile.usecases.GetProfileUseCase
import com.example.infoquizapp.domain.profile.usecases.ProfileResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class MainUiState {
    object Idle : MainUiState()
    object Loading : MainUiState()
    data class Success(val user: UserOut) : MainUiState()
    data class Error(val message: String) : MainUiState()
}

class MainViewModel(private val getProfileUseCase: GetProfileUseCase) : ViewModel() {

    private val _uiState = MutableStateFlow<MainUiState>(MainUiState.Idle)
    private val uiState: StateFlow<MainUiState> = _uiState

    fun loadProfile(token: String) {
        viewModelScope.launch {
            _uiState.value = MainUiState.Loading

            val result: ProfileResult = getProfileUseCase(token)
            if (result.error != null) {
                _uiState.value = MainUiState.Error(result.error)
            } else {
                val response = result.profile
                when(response) {
                    is Response.Error -> {
                        _uiState.value = MainUiState.Error(
                            response.error.message ?: "Ошибка получения профиля"
                        )
                    }
                    is Response.Succes -> {
                        _uiState.value = MainUiState.Success(response.result)
                    }
                    null -> _uiState.value = MainUiState.Error("Пустой ответ профиля")
                }
            }
        }
    }
}
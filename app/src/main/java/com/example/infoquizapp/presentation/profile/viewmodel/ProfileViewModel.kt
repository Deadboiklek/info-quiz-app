package com.example.infoquizapp.presentation.profile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.infoquizapp.domain.profile.usecases.GetProfileUseCase
import com.example.infoquizapp.domain.profile.usecases.ProfileResult
import com.example.infoquizapp.data.profile.network.Response
import com.example.infoquizapp.data.profile.model.UserOut
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class ProfileUiState {
    object Idle : ProfileUiState()
    object Loading : ProfileUiState()
    data class Success(val user: UserOut) : ProfileUiState()
    data class Error(val message: String) : ProfileUiState()
}

class ProfileViewModel(
    private val getProfileUseCase: GetProfileUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Idle)
    val uiState: StateFlow<ProfileUiState> = _uiState

    fun loadProfile(token: String) {
        viewModelScope.launch {
            _uiState.value = ProfileUiState.Loading

            val result: ProfileResult = getProfileUseCase(token)
            if (result.error != null) {
                _uiState.value = ProfileUiState.Error(result.error)
            } else {
                val response = result.profile
                when (response) {
                    is Response.Succes -> {
                        _uiState.value = ProfileUiState.Success(response.result)
                    }
                    is Response.Error -> {
                        _uiState.value = ProfileUiState.Error(
                            response.error.message ?: "Ошибка получения профиля"
                        )
                    }

                    null -> _uiState.value = ProfileUiState.Error("Пустой ответ профиля")
                }
            }
        }
    }
}
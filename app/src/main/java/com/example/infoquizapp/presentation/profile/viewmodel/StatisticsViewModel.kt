package com.example.infoquizapp.presentation.profile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.infoquizapp.data.profile.model.UserStatistics
import com.example.infoquizapp.data.profile.network.Response
import com.example.infoquizapp.domain.profile.usecases.GetStatisticsUseCase
import com.example.infoquizapp.domain.profile.usecases.StatisticsResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class StatisticsUiState {
    object Idle : StatisticsUiState()
    object Loading : StatisticsUiState()
    data class Success(val user: UserStatistics) : StatisticsUiState()
    data class Error(val message: String) : StatisticsUiState()
}

class StatisticsViewModel(
    private val getStatisticsUseCase: GetStatisticsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<StatisticsUiState>(StatisticsUiState.Idle)
    val uiState: StateFlow<StatisticsUiState> = _uiState

    fun getStatistics(token: String) {
        viewModelScope.launch {
            _uiState.value = StatisticsUiState.Loading

            val result: StatisticsResult = getStatisticsUseCase(token)

            if (result.error != null) {
                _uiState.value = StatisticsUiState.Error(result.error)
            } else {
                val response = result.statistics
                when (response) {
                    is Response.Succes -> {
                        _uiState.value = StatisticsUiState.Success(response.result)
                    }
                    is Response.Error -> {
                        _uiState.value = StatisticsUiState.Error(
                            response.error.message ?: "Ошибка получения статистики"
                        )
                    }

                    null -> _uiState.value = StatisticsUiState.Error("Пустой ответ профиля")
                }
            }
        }
    }
}
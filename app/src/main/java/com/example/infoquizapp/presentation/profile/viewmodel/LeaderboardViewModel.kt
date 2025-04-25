package com.example.infoquizapp.presentation.profile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.infoquizapp.data.profile.model.LeaderboardEntry
import com.example.infoquizapp.data.profile.network.Response
import com.example.infoquizapp.domain.profile.usecases.GetLeaderboardUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class LeaderboardUiState {
    object Idle : LeaderboardUiState()
    object Loading : LeaderboardUiState()
    data class Success(val players: List<LeaderboardEntry>) : LeaderboardUiState()
    data class Error(val message: String) : LeaderboardUiState()
}

class LeaderboardViewModel(
    private val getLeaderboard: GetLeaderboardUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<LeaderboardUiState>(LeaderboardUiState.Idle)
    val uiState: StateFlow<LeaderboardUiState> = _uiState

    fun loadLeaderboard(token: String) = viewModelScope.launch {
        _uiState.value = LeaderboardUiState.Loading
        when (val resp = getLeaderboard(token)) {
            is Response.Succes -> {
                _uiState.value = LeaderboardUiState.Success(resp.result.topPlayers)
            }
            is Response.Error -> {
                _uiState.value = LeaderboardUiState.Error(resp.error.message ?: "Ошибка")
            }
        }
    }
}
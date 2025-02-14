package com.example.infoquizapp.presentation.achievement.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.infoquizapp.data.achievement.model.AchievementOut
import com.example.infoquizapp.domain.achievement.usecases.GetAllAchievementsUseCase
import com.example.infoquizapp.domain.achievement.usecases.GetUserAchievementsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// состояния для достижений текущего пользователя
sealed class UserAchievementsUiState {
    object Idle : UserAchievementsUiState()
    object Loading : UserAchievementsUiState()
    data class Success(val achievements: List<AchievementOut>) : UserAchievementsUiState()
    data class Error(val message: String) : UserAchievementsUiState()
}

// состояния для получения всех достижений
sealed class AllAchievementsUiState {
    object Idle : AllAchievementsUiState()
    object Loading : AllAchievementsUiState()
    data class Success(val achievements: List<AchievementOut>) : AllAchievementsUiState()
    data class Error(val message: String) : AllAchievementsUiState()
}

class AchievementsViewModel(
    private val getUserAchievementsUseCase: GetUserAchievementsUseCase,
    private val getAllAchievementsUseCase: GetAllAchievementsUseCase
) : ViewModel() {

    private val _userAchievementsUiState = MutableStateFlow<UserAchievementsUiState>(UserAchievementsUiState.Idle)
    val userAchievementsUiState: StateFlow<UserAchievementsUiState> = _userAchievementsUiState

    private val _allAchievementsUiState = MutableStateFlow<AllAchievementsUiState>(AllAchievementsUiState.Idle)
    val allAchievementsUiState: StateFlow<AllAchievementsUiState> = _allAchievementsUiState

    fun loadUserAchievements(token: String) {
        viewModelScope.launch {
            _userAchievementsUiState.value = UserAchievementsUiState.Loading
            val result = getUserAchievementsUseCase(token)
            if (result.error != null) {
                _userAchievementsUiState.value = UserAchievementsUiState.Error(result.error)
            } else {
                _userAchievementsUiState.value = UserAchievementsUiState.Success(result.achievements ?: emptyList())
            }
        }
    }

    fun loadAllAchievements() {
        viewModelScope.launch {
            _allAchievementsUiState.value = AllAchievementsUiState.Loading
            val result = getAllAchievementsUseCase()
            if (result.error != null) {
                _allAchievementsUiState.value = AllAchievementsUiState.Error(result.error)
            } else {
                _allAchievementsUiState.value = AllAchievementsUiState.Success(result.achievements ?: emptyList())
            }
        }
    }
}
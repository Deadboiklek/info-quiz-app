package com.example.infoquizapp.presentation.achievement.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.infoquizapp.data.achievement.model.AchievementOut
import com.example.infoquizapp.domain.achievement.usecases.GetAllAchievementsUseCase
import com.example.infoquizapp.domain.achievement.usecases.GetUserAchievementsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
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
    private val userAchievementsUiState: StateFlow<UserAchievementsUiState> = _userAchievementsUiState

    private val _allAchievementsUiState = MutableStateFlow<AllAchievementsUiState>(AllAchievementsUiState.Idle)
    private val allAchievementsUiState: StateFlow<AllAchievementsUiState> = _allAchievementsUiState

    // объединённое состояние
    // для каждого достижения из allAchievements проверяется, присутствует ли его id среди достижений пользователя

    val combinedAchievementsState: StateFlow<CombinedAchievementsState> =
        combine(userAchievementsUiState, allAchievementsUiState) { userState, allState ->

            // если все достижения успешно загружены, берем их, иначе пустой список
            val allList = when (allState) {
                is AllAchievementsUiState.Success -> allState.achievements
                else -> emptyList()
            }

            val userList = when (userState) {
                is UserAchievementsUiState.Success -> userState.achievements
                else -> emptyList()
            }
            val userAchievementIds = userList.map { it.id }.toSet()

            // для каждого достижения устанавливаем isObtained = true, если его id присутствует в списке пользователя
            val uiModels = allList.map { achievement ->
                AchievementsUiModel(
                    achievement = achievement,
                    isObtained = achievement.id in userAchievementIds
                )
            }
            CombinedAchievementsState(achievementsUiModels = uiModels)
        }.stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            CombinedAchievementsState(emptyList())
        )

    fun loadUserAchievements(token: String) {
        viewModelScope.launch {
            _userAchievementsUiState.value = UserAchievementsUiState.Loading
            val result = getUserAchievementsUseCase(token)
            if (result.error != null) {
                _userAchievementsUiState.value = UserAchievementsUiState.Error(result.error)
            } else {
                _userAchievementsUiState.value =
                    UserAchievementsUiState.Success(result.achievements ?: emptyList())
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
                _allAchievementsUiState.value =
                    AllAchievementsUiState.Success(result.achievements ?: emptyList())
            }
        }
    }
}
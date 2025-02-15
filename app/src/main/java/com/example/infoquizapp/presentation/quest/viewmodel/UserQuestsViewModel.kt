package com.example.infoquizapp.presentation.quest.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.infoquizapp.data.quest.model.QuestOut
import com.example.infoquizapp.domain.quest.usecases.CompleteQuestResult
import com.example.infoquizapp.domain.quest.usecases.CompleteQuestUseCase
import com.example.infoquizapp.domain.quest.usecases.GetUserQuestsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class UserQuestsUiState {
    object Idle : UserQuestsUiState()
    object Loading : UserQuestsUiState()
    data class Success(val quests: List<QuestOut>) : UserQuestsUiState()
    data class Error(val message: String) : UserQuestsUiState()
}

class UserQuestsViewModel(
    private val getUserQuestsUseCase: GetUserQuestsUseCase,
    private val completeQuestUseCase: CompleteQuestUseCase
) : ViewModel() {

    private val _userQuestsState = MutableStateFlow<UserQuestsUiState>(UserQuestsUiState.Idle)
    val userQuestsState: StateFlow<UserQuestsUiState> = _userQuestsState

    fun loadUserQuests(token: String) {
        viewModelScope.launch {
            _userQuestsState.value = UserQuestsUiState.Loading
            val result = getUserQuestsUseCase(token)
            if (result.error != null) {
                _userQuestsState.value = UserQuestsUiState.Error(result.error)
            } else {
                _userQuestsState.value = UserQuestsUiState.Success(result.quests ?: emptyList())
            }
        }
    }

    fun completeQuest(questId: Int, token: String, onComplete: (CompleteQuestResult) -> Unit) {
        viewModelScope.launch {
            val result = completeQuestUseCase(questId, token)
            onComplete(result)
            // обновляем список квестов
            loadUserQuests(token)
        }
    }
}
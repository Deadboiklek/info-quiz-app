package com.example.infoquizapp.presentation.quest.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.infoquizapp.data.quest.model.QuestOut
import com.example.infoquizapp.domain.quest.usecases.CompleteQuestResult
import com.example.infoquizapp.domain.quest.usecases.CompleteQuestUseCase
import com.example.infoquizapp.domain.quest.usecases.GetAllQuestsUseCase
import com.example.infoquizapp.domain.quest.usecases.GetUserQuestsUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class UserQuestsUiState {
    object Idle : UserQuestsUiState()
    object Loading : UserQuestsUiState()
    data class Success(val quests: List<QuestOut>) : UserQuestsUiState()
    data class Error(val message: String) : UserQuestsUiState()
}

sealed class AllQuestsUiState {
    object Idle : AllQuestsUiState()
    object Loading : AllQuestsUiState()
    data class Success(val quests: List<QuestOut>) : AllQuestsUiState()
    data class Error(val message: String) : AllQuestsUiState()
}

class QuestsViewModel(
    private val getUserQuestsUseCase: GetUserQuestsUseCase,
    private val getAllQuestsUseCase: GetAllQuestsUseCase,
    private val completeQuestUseCase: CompleteQuestUseCase
) : ViewModel() {

    private val _userQuestsUiState = MutableStateFlow<UserQuestsUiState>(UserQuestsUiState.Idle)
    private val userQuestsUiState: StateFlow<UserQuestsUiState> = _userQuestsUiState

    private val _allQuestsUiState = MutableStateFlow<AllQuestsUiState>(AllQuestsUiState.Idle)
    private val allQuestsUiState: StateFlow<AllQuestsUiState> = _allQuestsUiState

    // Канал событий для отслеживания новых завершённых квестов
    private val _questCompletedEvent = MutableSharedFlow<QuestOut>()
    val questCompletedEvent: SharedFlow<QuestOut> = _questCompletedEvent.asSharedFlow()

    // Запоминаем ранее выполненные квесты – их ID
    private var previousUserQuestIds: Set<Int> = emptySet()

    // Объединённое состояние: для каждого квеста из общего списка определяем, выполнен ли он пользователем
    val combinedQuestsState: StateFlow<CombinedQuestsState> =
        combine(userQuestsUiState, allQuestsUiState) { userState, allState ->
            val allList = when (allState) {
                is AllQuestsUiState.Success -> allState.quests
                else -> emptyList()
            }
            val userList = when (userState) {
                is UserQuestsUiState.Success -> userState.quests
                else -> emptyList()
            }
            val userQuestIds = userList.map { it.id }.toSet()
            val uiModels = allList.map { quest ->
                QuestsUiModel(
                    quest = quest,
                    isCompleted = quest.id in userQuestIds
                )
            }
            CombinedQuestsState(questsUiModels = uiModels)
        }.stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            CombinedQuestsState(emptyList())
        )

    fun loadUserQuests(token: String) {
        viewModelScope.launch {
            _userQuestsUiState.value = UserQuestsUiState.Loading
            val result = getUserQuestsUseCase(token)
            if (result.error != null) {
                _userQuestsUiState.value = UserQuestsUiState.Error(result.error)
            } else {
                val currentUserQuests = result.quests ?: emptyList()
                _userQuestsUiState.value = UserQuestsUiState.Success(currentUserQuests)
                // Определяем новые выполненные квесты
                val currentIds = currentUserQuests.map { it.id }.toSet()
                val newQuestIds = currentIds - previousUserQuestIds
                previousUserQuestIds = currentIds
                currentUserQuests.filter { it.id in newQuestIds }.forEach { quest ->
                    _questCompletedEvent.emit(quest)
                }
            }
        }
    }

    fun loadAllQuests() {
        viewModelScope.launch {
            _allQuestsUiState.value = AllQuestsUiState.Loading
            val result = getAllQuestsUseCase()
            if (result.error != null) {
                _allQuestsUiState.value = AllQuestsUiState.Error(result.error)
            } else {
                _allQuestsUiState.value = AllQuestsUiState.Success(result.quests ?: emptyList())
            }
        }
    }

    fun completeQuest(questId: Int, token: String, onComplete: (CompleteQuestResult) -> Unit) {
        viewModelScope.launch {
            val result = completeQuestUseCase(questId, token)
            onComplete(result)
            loadUserQuests(token)
        }
    }
}
package com.example.infoquizapp.presentation.practice.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.infoquizapp.data.practice.PracticeEntity
import com.example.infoquizapp.domain.practice.usecases.GetAllPracticeUseCase
import com.example.infoquizapp.domain.practice.usecases.GetPracticeUseCase
import com.example.infoquizapp.domain.practice.usecases.MarkPracticeAsDoneUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class PracticeUiState {
    object Idle : PracticeUiState()
    object Loading : PracticeUiState()
    data class Success(val practice: PracticeEntity?) : PracticeUiState()
    data class Error(val message: String) : PracticeUiState()
}

sealed class AllPracticeUiState {
    object Idle : AllPracticeUiState()
    object Loading : AllPracticeUiState()
    data class Success(val practices: List<PracticeEntity>) : AllPracticeUiState()
    data class Error(val message: String) : AllPracticeUiState()
}

class PracticeViewModel(
    private val getPracticeUseCase: GetPracticeUseCase,
    private val getAllPracticeUseCase: GetAllPracticeUseCase,
    private val markPracticeAsReadUseCase: MarkPracticeAsDoneUseCase
) : ViewModel() {

    private val _practiceState = MutableStateFlow<PracticeUiState>(PracticeUiState.Idle)
    val practiceState: StateFlow<PracticeUiState> = _practiceState

    private val _allPracticeState = MutableStateFlow<AllPracticeUiState>(AllPracticeUiState.Idle)
    val allPracticeState: StateFlow<AllPracticeUiState> = _allPracticeState

    val combinedState: StateFlow<CombinedPracticeUiState> = combine(
        practiceState,
        allPracticeState
    ) { practice, allPractice ->
        CombinedPracticeUiState(practice, allPractice)
    }.stateIn(viewModelScope, SharingStarted.Lazily, CombinedPracticeUiState())

    // Загрузка одной практики
    fun loadPractice(id: Int = 1) {
        viewModelScope.launch {
            _practiceState.value = PracticeUiState.Loading
            try {
                val practice = getPracticeUseCase(id)
                _practiceState.value = PracticeUiState.Success(practice)
            } catch (e: Exception) {
                _practiceState.value = PracticeUiState.Error(e.localizedMessage ?: "Ошибка загрузки практики")
            }
        }
    }

    // Загрузка всех практик
    fun loadAllPractices() {
        viewModelScope.launch {
            _allPracticeState.value = AllPracticeUiState.Loading
            try {
                val practices = getAllPracticeUseCase()
                _allPracticeState.value = AllPracticeUiState.Success(practices)
            } catch (e: Exception) {
                _allPracticeState.value = AllPracticeUiState.Error(e.localizedMessage ?: "Ошибка загрузки практик")
            }
        }
    }

    // Отметить практику как пройденную
    fun markPracticeAsDone(id: Int) {
        viewModelScope.launch {
            try {
                markPracticeAsReadUseCase(id)
                val updatedPractice = getPracticeUseCase(id)
                _practiceState.value = PracticeUiState.Success(updatedPractice)
            } catch (e: Exception) {
                _practiceState.value = PracticeUiState.Error(e.localizedMessage ?: "Ошибка обновления статуса практики")
            }
        }
    }
}
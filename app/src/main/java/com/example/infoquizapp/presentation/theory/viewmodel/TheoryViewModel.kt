package com.example.infoquizapp.presentation.theory.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.infoquizapp.data.theory.TheoryEntity
import com.example.infoquizapp.domain.theory.usecases.GetAllTheoryUseCase
import com.example.infoquizapp.domain.theory.usecases.GetTheoryUseCase
import com.example.infoquizapp.domain.theory.usecases.MarkTheoryAsReadUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class TheoryUiState {
    object Idle : TheoryUiState()
    object Loading : TheoryUiState()
    data class Success(val theory: TheoryEntity?) : TheoryUiState()
    data class Error(val message: String) : TheoryUiState()
}

sealed class AllTheoryUiState {
    object Idle : AllTheoryUiState()
    object Loading : AllTheoryUiState()
    data class Success(val theories: List<TheoryEntity>) : AllTheoryUiState()
    data class Error(val message: String) : AllTheoryUiState()
}

class TheoryViewModel(
    private val getTheoryUseCase: GetTheoryUseCase,
    private val getAllTheoryUseCase: GetAllTheoryUseCase,
    private val markTheoryAsReadUseCase: MarkTheoryAsReadUseCase
) : ViewModel() {

    private val _theoryState = MutableStateFlow<TheoryUiState>(TheoryUiState.Idle)
    val theoryState: StateFlow<TheoryUiState> = _theoryState

    private val _allTheoryState = MutableStateFlow<AllTheoryUiState>(AllTheoryUiState.Idle)
    val allTheoryState: StateFlow<AllTheoryUiState> = _allTheoryState

    val combinedState: StateFlow<CombinedTheoryUiState> = combine(
        theoryState,
        allTheoryState
    ) { theory, allTheory ->
        CombinedTheoryUiState(theory, allTheory)
    }.stateIn(viewModelScope, SharingStarted.Lazily, CombinedTheoryUiState())

    // Загрузка одной теории
    fun loadTheory(id: Int = 1) {
        viewModelScope.launch {
            _theoryState.value = TheoryUiState.Loading
            try {
                val theory = getTheoryUseCase(id)
                _theoryState.value = TheoryUiState.Success(theory)
            } catch (e: Exception) {
                _theoryState.value = TheoryUiState.Error(e.localizedMessage ?: "Ошибка загрузки теории")
            }
        }
    }

    // Отметить теорию как прочитанную
    fun markTheoryAsRead(id: Int) {
        viewModelScope.launch {
            try {
                markTheoryAsReadUseCase(id)
                val updatedTheory = getTheoryUseCase(id)
                _theoryState.value = TheoryUiState.Success(updatedTheory)
            } catch (e: Exception) {
                _theoryState.value = TheoryUiState.Error(e.localizedMessage ?: "Ошибка обновления статуса теории")
            }
        }
    }

    // Загрузка всех теорий
    fun loadAllTheory() {
        viewModelScope.launch {
            _allTheoryState.value = AllTheoryUiState.Loading
            try {
                val theories = getAllTheoryUseCase()
                _allTheoryState.value = AllTheoryUiState.Success(theories)
            } catch (e: Exception) {
                _allTheoryState.value = AllTheoryUiState.Error(e.localizedMessage ?: "Ошибка загрузки всех теорий")
            }
        }
    }

}
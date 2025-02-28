package com.example.infoquizapp.presentation.theory.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.infoquizapp.data.theory.TheoryEntity
import com.example.infoquizapp.domain.theory.usecases.GetAllTheoryUseCase
import com.example.infoquizapp.domain.theory.usecases.GetTheoryUseCase
import com.example.infoquizapp.domain.theory.usecases.MarkTheoryAsReadUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class TheoryUiState {
    object Idle : TheoryUiState()
    object Loading : TheoryUiState()
    data class Success(val theory: TheoryEntity?) : TheoryUiState()
    data class SuccessAll(val theories: List<TheoryEntity>) : TheoryUiState()
    data class Error(val message: String) : TheoryUiState()
}

class TheoryViewModel(
    private val getTheoryUseCase: GetTheoryUseCase,
    private val markTheoryAsReadUseCase: MarkTheoryAsReadUseCase,
    private val getAllTheoryUseCase: GetAllTheoryUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<TheoryUiState>(TheoryUiState.Idle)
    val uiState: StateFlow<TheoryUiState> = _uiState

    // загрузка теории по id
    fun loadTheory(id: Int = 1) {
        viewModelScope.launch {
            _uiState.value = TheoryUiState.Loading
            try {
                val theory = getTheoryUseCase(id)
                _uiState.value = TheoryUiState.Success(theory)
            } catch (e: Exception) {
                _uiState.value = TheoryUiState.Error(e.localizedMessage ?: "Ошибка загрузки теории")
            }
        }
    }

    fun markTheoryAsRead(id: Int) {
        viewModelScope.launch {
            try {
                markTheoryAsReadUseCase(id)
                // после обновления статуса перезагружаем теорию для обновления UI
                val updatedTheory = getTheoryUseCase(id)
                _uiState.value = TheoryUiState.Success(updatedTheory)
            } catch (e: Exception) {
                _uiState.value = TheoryUiState.Error(e.localizedMessage ?: "Ошибка обновления статуса теории")
            }
        }
    }

    fun getAllTheory() {
        viewModelScope.launch {
            _uiState.value = TheoryUiState.Loading
            try {
                val theories = getAllTheoryUseCase()
                _uiState.value = TheoryUiState.SuccessAll(theories)
            } catch (e: Exception) {
                _uiState.value = TheoryUiState.Error(e.localizedMessage ?: "Ошибка загрузки теории")
            }
        }
    }
}
package com.example.infoquizapp.presentation.theory.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.infoquizapp.data.theory.TheoryEntity
import com.example.infoquizapp.domain.theory.usecases.GetTheoryUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class TheoryUiState {
    object Idle : TheoryUiState()
    object Loading : TheoryUiState()
    data class Success(val theory: TheoryEntity?) : TheoryUiState()
    data class Error(val message: String) : TheoryUiState()
}

class TheoryViewModel(
    private val getTheoryUseCase: GetTheoryUseCase,
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
}
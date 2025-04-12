package com.example.infoquizapp.presentation.teacher.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.infoquizapp.data.teacher.model.StudentStatistics
import com.example.infoquizapp.domain.teacher.usecases.GetStudentStatisticsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class StudentStatisticsUiState {
    object Loading : StudentStatisticsUiState()
    data class Success(val statistics: StudentStatistics) : StudentStatisticsUiState()
    data class Error(val message: String) : StudentStatisticsUiState()
}

class StudentStatisticsViewModel(
    private val getStudentStatisticsUseCase: GetStudentStatisticsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<StudentStatisticsUiState>(StudentStatisticsUiState.Loading)
    val uiState: StateFlow<StudentStatisticsUiState> = _uiState

    fun loadStudentStatistics(token: String, studentId: Int) {
        viewModelScope.launch {
            val result = getStudentStatisticsUseCase(token, studentId)
            _uiState.value = result.statistics?.let {
                StudentStatisticsUiState.Success(it)
            } ?: StudentStatisticsUiState.Error(result.error ?: "Ошибка загрузки статистики")
        }
    }
}
package com.example.infoquizapp.presentation.teacher.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.infoquizapp.data.teacher.model.TeacherProfile
import com.example.infoquizapp.data.teacher.network.Response
import com.example.infoquizapp.domain.teacher.usecases.GetTeacherProfileUseCase
import com.example.infoquizapp.domain.teacher.usecases.TeacherProfileResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class TeacherProfileUiState {
    object Idle: TeacherProfileUiState()
    object Loading: TeacherProfileUiState()
    data class Success(val teacher: TeacherProfile): TeacherProfileUiState()
    data class Error(val message: String): TeacherProfileUiState()
}

class TeacherProfileViewModel (private val getTeacherProfileUseCase: GetTeacherProfileUseCase) : ViewModel() {
    private val _uiState = MutableStateFlow<TeacherProfileUiState>(TeacherProfileUiState.Idle)
    val uiState: StateFlow<TeacherProfileUiState> = _uiState

    fun loadTeacherProfile(token: String) {
        viewModelScope.launch {
            _uiState.value = TeacherProfileUiState.Loading

            val result: TeacherProfileResult = getTeacherProfileUseCase(token)
            if (result.error != null) {
                _uiState.value = TeacherProfileUiState.Error(result.error)
            } else {
                val response = result.teacherProfile
                when (response) {
                    is Response.Succes -> {
                        _uiState.value = TeacherProfileUiState.Success(response.result)
                    }
                    is Response.Error -> {
                        _uiState.value = TeacherProfileUiState.Error(
                            response.error.message ?: "Ошибка получения профиля"
                        )
                    }

                    null -> _uiState.value = TeacherProfileUiState.Error("Пустой ответ профиля")
                }
            }
        }
    }
}
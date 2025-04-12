package com.example.infoquizapp.presentation.teacher.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.infoquizapp.data.quiz.model.QuizOut
import com.example.infoquizapp.data.teacher.model.TeacherCreateQuiz
import com.example.infoquizapp.data.teacher.network.Response
import com.example.infoquizapp.data.teacher.network.TeacherApiService
import com.example.infoquizapp.domain.teacher.usecases.PostTeacherQuizResult
import com.example.infoquizapp.domain.teacher.usecases.PostTeacherQuizUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class PostTeacherQuizUiState {
    object Idle : PostTeacherQuizUiState()
    object Loading : PostTeacherQuizUiState()
    data class Success(val quiz: QuizOut) : PostTeacherQuizUiState()
    data class Error(val message: String) : PostTeacherQuizUiState()
}

class PostTeacherQuizViewModel(private val postTeacherQuizUseCase: PostTeacherQuizUseCase) : ViewModel() {

    private val _uiState = MutableStateFlow<PostTeacherQuizUiState>(PostTeacherQuizUiState.Idle)
    val uiState: StateFlow<PostTeacherQuizUiState> = _uiState

    fun createQuiz(token: String, quiz: TeacherCreateQuiz) {
        viewModelScope.launch {
            _uiState.value = PostTeacherQuizUiState.Loading

            val result: PostTeacherQuizResult = postTeacherQuizUseCase(token, quiz)
            if(result.error != null) {
                _uiState.value = PostTeacherQuizUiState.Error(result.error)
            } else {
                val response = result.response
                when (response) {

                    is Response.Error -> {
                        _uiState.value = PostTeacherQuizUiState.Error(
                            response.error.message ?: "Ошибка добавления квиза"
                        )
                    }

                    is Response.Succes -> {
                        _uiState.value = PostTeacherQuizUiState.Success(response.result)
                    }
                    null -> _uiState.value = PostTeacherQuizUiState.Error("Пустой ответ квиза")
                }
            }
        }
    }

    fun resetState() {
        _uiState.value = PostTeacherQuizUiState.Idle
    }
}
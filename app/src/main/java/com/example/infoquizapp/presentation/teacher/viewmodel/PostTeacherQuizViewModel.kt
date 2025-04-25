package com.example.infoquizapp.presentation.teacher.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.infoquizapp.data.quiz.model.QuizOut
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

class PostTeacherQuizViewModel(private val useCase: PostTeacherQuizUseCase) : ViewModel() {

    private val _state = MutableStateFlow<PostTeacherQuizUiState>(PostTeacherQuizUiState.Idle)
    val state: StateFlow<PostTeacherQuizUiState> = _state

    fun createQuiz(
        token: String,
        question: String,
        correctAnswer: String,
        experienceReward: Int,
        type: String,
        imageBytes: ByteArray?
    ) {
        viewModelScope.launch {
            _state.value = PostTeacherQuizUiState.Loading
            val result = useCase(token, question, correctAnswer, experienceReward, type, imageBytes)
            _state.value = result.quiz?.let { PostTeacherQuizUiState.Success(it) }
                ?: PostTeacherQuizUiState.Error(result.error ?: "Unknown error")
        }
    }

    fun resetState() {
        _state.value = PostTeacherQuizUiState.Idle
    }
}
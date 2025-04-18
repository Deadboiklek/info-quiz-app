package com.example.infoquizapp.presentation.teacher.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.infoquizapp.data.quiz.model.QuizOut
import com.example.infoquizapp.domain.teacher.usecases.GetTeacherQuizzesUseCase
import com.example.infoquizapp.domain.teacher.usecases.UpdateTeacherQuizUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EditQuizViewModel(
    private val updateUseCase: UpdateTeacherQuizUseCase,
    private val getUseCase: GetTeacherQuizzesUseCase
) : ViewModel() {

    // поля формы
    var question by mutableStateOf("")
    var correctAnswer by mutableStateOf("")
    var expReward by mutableStateOf("")
    var type by mutableStateOf("")
    var imageBytes: ByteArray? = null

    // состояние UI
    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> = _uiState

    sealed class UiState {
        object Idle : UiState()
        object Loading : UiState()
        object Success : UiState()
        data class Error(val msg: String) : UiState()
    }

    fun loadQuiz(quiz: QuizOut) {
        question       = quiz.question
        correctAnswer  = quiz.correctAnswer
        expReward      = quiz.experienceReward.toString()
        type           = quiz.type
        // imageBytes можно декодировать из Base64, если нужно
    }

    fun saveChanges(token: String, quizId: Int) = viewModelScope.launch {
        _uiState.value = UiState.Loading
        val result = updateUseCase(
            token, quizId, question, correctAnswer,
            expReward.toIntOrNull() ?: 0, type, imageBytes
        )
        _uiState.value = if (result.quiz != null) UiState.Success
        else UiState.Error(result.error ?: "Unknown error")
    }

    /** Сбрасывает состояние на Idle, чтобы при повторном входе не было старого Success */
    fun resetState() {
        _uiState.value = UiState.Idle
    }
}
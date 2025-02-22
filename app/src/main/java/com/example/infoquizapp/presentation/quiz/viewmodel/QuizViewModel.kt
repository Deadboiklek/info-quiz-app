package com.example.infoquizapp.presentation.quiz.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.infoquizapp.data.quiz.model.AnswerIn
import com.example.infoquizapp.data.quiz.model.AnswerOut
import com.example.infoquizapp.data.quiz.model.QuizOut
import com.example.infoquizapp.domain.quiz.usecases.GetTestQuizzesUseCase
import com.example.infoquizapp.domain.quiz.usecases.SubmitAnswerResult
import com.example.infoquizapp.domain.quiz.usecases.SubmitAnswerUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class TestQuizzesUiState {
    object Idle : TestQuizzesUiState()
    object Loading : TestQuizzesUiState()
    data class Success(val quizzes: List<QuizOut>) : TestQuizzesUiState()
    data class Error(val message: String) : TestQuizzesUiState()
}

class QuizViewModel(
    private val getTestQuizzesUseCase: GetTestQuizzesUseCase,
    private val submitAnswerUseCase: SubmitAnswerUseCase
) : ViewModel() {

    private val _testQuizzesState = MutableStateFlow<TestQuizzesUiState>(TestQuizzesUiState.Idle)
    val testQuizzesState: StateFlow<TestQuizzesUiState> = _testQuizzesState

    fun loadTest(quizType: String, token: String) {
        viewModelScope.launch {
            _testQuizzesState.value = TestQuizzesUiState.Loading
            val result = getTestQuizzesUseCase(quizType, token)
            if (result.error != null) {
                _testQuizzesState.value = TestQuizzesUiState.Error(result.error)
            } else {
                _testQuizzesState.value = TestQuizzesUiState.Success(result.quizzes ?: emptyList())
            }
        }
    }

    fun submitAnswer(quizId: Int, answer: String, token: String, onComplete: (SubmitAnswerResult) -> Unit) {
        viewModelScope.launch {

            val answerIn = AnswerIn(quizId = quizId, userAnswer = answer)
            val result = submitAnswerUseCase(answerIn, token)
            onComplete(result)

        }
    }
}
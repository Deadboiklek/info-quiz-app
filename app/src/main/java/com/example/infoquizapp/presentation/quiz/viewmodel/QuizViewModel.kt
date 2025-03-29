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

sealed class TestResultUiState {
    object Idle : TestResultUiState()
    object Loading : TestResultUiState()
    data class Success(val correctAnswers: Int, val totalQuestions: Int) : TestResultUiState()
    data class Error(val message: String) : TestResultUiState()
}

class QuizViewModel(
    private val getTestQuizzesUseCase: GetTestQuizzesUseCase,
    private val submitAnswerUseCase: SubmitAnswerUseCase
) : ViewModel() {

    private val _testQuizzesState = MutableStateFlow<TestQuizzesUiState>(TestQuizzesUiState.Idle)
    val testQuizzesState: StateFlow<TestQuizzesUiState> = _testQuizzesState

    private val _testResultState = MutableStateFlow<TestResultUiState>(TestResultUiState.Idle)
    val testResultState: StateFlow<TestResultUiState> = _testResultState

    private var _userAnswers = mutableMapOf<Int, String>()
    val userAnswers: Map<Int, String> get() = _userAnswers

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

    // Новый метод для отправки всех ответов и подсчета правильных
    fun submitTest(userAnswers: Map<Int, String>, token: String) {
        viewModelScope.launch {
            var correctAnswers = 0
            userAnswers.forEach { (quizId, answer) ->
                val result = submitAnswerUseCase(AnswerIn(quizId, answer), token)
                if (result.response?.isCorrect == true) correctAnswers++
            }
            _testResultState.value = TestResultUiState.Success(correctAnswers, userAnswers.size)
        }
    }

    // Новый метод для сброса состояния
    fun resetTest() {
        _userAnswers.clear()  // Очистить ответы
        _testResultState.value = TestResultUiState.Idle
        _testQuizzesState.value = TestQuizzesUiState.Idle
    }
}
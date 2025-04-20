package com.example.infoquizapp.presentation.trial.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.infoquizapp.data.quiz.model.AnswerIn
import com.example.infoquizapp.data.quiz.model.AnswerOut
import com.example.infoquizapp.data.quiz.model.QuizOut
import com.example.infoquizapp.domain.quiz.usecases.GetTrialTestUseCase
import com.example.infoquizapp.domain.quiz.usecases.SubmitAnswerResult
import com.example.infoquizapp.domain.quiz.usecases.SubmitAnswerUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class TrialUiState {
    object Idle : TrialUiState()
    object Loading : TrialUiState()
    data class Success(val quizzes: List<QuizOut>) : TrialUiState()
    data class Error(val message: String) : TrialUiState()
}

sealed class TrialResultState {
    object Idle : TrialResultState()
    object Submitting : TrialResultState()
    data class Completed(val correctCount: Int, val total: Int) : TrialResultState()
}

class TrialViewModel(
    private val getTrialTestUseCase: GetTrialTestUseCase,
    private val submitAnswerUseCase: SubmitAnswerUseCase
) : ViewModel() {
    private val _trialState = MutableStateFlow<TrialUiState>(TrialUiState.Idle)
    val trialState: StateFlow<TrialUiState> = _trialState

    // Результаты ответов: quizId -> AnswerOut
    private val _answerResults = MutableStateFlow<Map<Int, AnswerOut>>(emptyMap())
    val answerResults: StateFlow<Map<Int, AnswerOut>> = _answerResults

    // Состояние результата пробника
    private val _trialResultState = MutableStateFlow<TrialResultState>(TrialResultState.Idle)
    val trialResultState: StateFlow<TrialResultState> = _trialResultState

    fun loadTrial(token: String) {
        viewModelScope.launch {
            _trialState.value = TrialUiState.Loading
            val result = getTrialTestUseCase(token)
            if (result.error != null) {
                _trialState.value = TrialUiState.Error(result.error)
            } else {
                _trialState.value = TrialUiState.Success(result.quizzes ?: emptyList())
            }
        }
    }

    fun submitTrial(answers: Map<Int, String>, token: String) {
        viewModelScope.launch {
            _trialResultState.value = TrialResultState.Submitting
            val results = mutableMapOf<Int, AnswerOut>()
            var correctCount = 0
            answers.forEach { (quizId, answer) ->
                val response = submitAnswerUseCase(AnswerIn(quizId, answer), token)
                response.response?.let { ansOut ->
                    results[quizId] = ansOut
                    if (ansOut.isCorrect) correctCount++
                }
            }
            _answerResults.value = results
            _trialResultState.value = TrialResultState.Completed(correctCount, answers.size)
        }
    }

    fun resetTrial() {
        _answerResults.value = emptyMap()
        _trialResultState.value = TrialResultState.Idle
    }
}
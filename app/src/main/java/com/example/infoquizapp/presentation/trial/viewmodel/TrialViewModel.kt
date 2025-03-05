package com.example.infoquizapp.presentation.trial.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.infoquizapp.data.quiz.model.AnswerIn
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

class TrialViewModel(
    private val getTrialTestUseCase: GetTrialTestUseCase,
    private val submitAnswerUseCase: SubmitAnswerUseCase
) : ViewModel() {

    private val _trialState = MutableStateFlow<TrialUiState>(TrialUiState.Idle)
    val trialState: StateFlow<TrialUiState> = _trialState

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

    fun submitAnswer(quizId: Int, answer: String, token: String, onComplete: (SubmitAnswerResult) -> Unit) {
        viewModelScope.launch {

            val answerIn = AnswerIn(quizId = quizId, userAnswer = answer)
            val result = submitAnswerUseCase(answerIn, token)
            onComplete(result)

        }
    }
}
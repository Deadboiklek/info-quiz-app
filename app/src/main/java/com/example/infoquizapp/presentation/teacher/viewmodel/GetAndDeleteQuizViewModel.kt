package com.example.infoquizapp.presentation.teacher.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.infoquizapp.data.quiz.model.QuizOut
import com.example.infoquizapp.domain.teacher.usecases.DeleteTeacherQuizUseCase
import com.example.infoquizapp.domain.teacher.usecases.GetTeacherQuizzesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GetAndDeleteQuizViewModel(
    private val getTeacherQuizzesUseCase: GetTeacherQuizzesUseCase,
    private val deleteTeacherQuizUseCase: DeleteTeacherQuizUseCase
) : ViewModel() {

    private val _quizzesState = MutableStateFlow<List<QuizOut>>(emptyList())
    val quizzesState: StateFlow<List<QuizOut>> = _quizzesState

    private val _errorState = MutableStateFlow<String?>(null)
    val errorState: StateFlow<String?> = _errorState

    fun loadTeacherQuizzes(token: String) {
        viewModelScope.launch {
            val result = getTeacherQuizzesUseCase(token)
            result.quizzes?.let {
                _quizzesState.value = it
                _errorState.value = null
            } ?: run {
                _errorState.value = result.error
            }
        }
    }

    fun deleteQuiz(token: String, quizId: Int) {
        viewModelScope.launch {
            val result = deleteTeacherQuizUseCase(token, quizId)
            if (result.deletedQuiz != null) {
                // Обновляем список после успешного удаления
                _quizzesState.value = _quizzesState.value.filterNot { it.id == quizId }
                _errorState.value = null
            } else {
                _errorState.value = result.error
            }
        }
    }
}
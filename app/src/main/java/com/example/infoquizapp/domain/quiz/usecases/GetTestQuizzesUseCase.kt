package com.example.infoquizapp.domain.quiz.usecases

import com.example.infoquizapp.data.quiz.network.Response
import com.example.infoquizapp.domain.quiz.repository.QuizRepository

class GetTestQuizzesUseCase(private val repository: QuizRepository) {
    suspend operator fun invoke(quizType: String, token: String): GetTestQuizzesResult {

        return when(val response = repository.getTestQuizzes(quizType, token)) {
            is Response.Success -> GetTestQuizzesResult(response.result, null)
            is Response.Error -> GetTestQuizzesResult(null, response.error.message)
        }
    }
}
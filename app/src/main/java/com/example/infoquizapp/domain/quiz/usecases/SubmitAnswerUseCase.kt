package com.example.infoquizapp.domain.quiz.usecases

import com.example.infoquizapp.data.quiz.model.AnswerIn
import com.example.infoquizapp.data.quiz.network.Response
import com.example.infoquizapp.domain.quiz.repository.QuizRepository

class SubmitAnswerUseCase(private val repository: QuizRepository) {
    suspend operator fun invoke(answer: AnswerIn, token: String): SubmitAnswerResult {

        return when(val response = repository.submitAnswer(answer, token)){
            is Response.Success -> SubmitAnswerResult(response.result, null)
            is Response.Error -> SubmitAnswerResult(null, response.error.message)
        }
    }
}
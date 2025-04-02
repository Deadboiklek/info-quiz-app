package com.example.infoquizapp.domain.gamequiz.usecases

import com.example.infoquizapp.data.gamequiz.network.Response
import com.example.infoquizapp.domain.gamequiz.repository.GameQuizRepository

class GetGameQuizUseCase(private val repository: GameQuizRepository) {
    suspend operator fun invoke(gameQuizType: String, token: String): GetGameQuizResult {

        return when(val response = repository.getGameQuiz(token)) {
            is Response.Success -> GetGameQuizResult(response.result, null)
            is Response.Error -> GetGameQuizResult(null, response.error.message)
        }
    }
}
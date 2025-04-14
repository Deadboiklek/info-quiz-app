package com.example.infoquizapp.data.gamequiz.repository

import com.example.infoquizapp.data.gamequiz.model.CompleteGameQuizResponse
import com.example.infoquizapp.data.gamequiz.model.GameQuizOut
import com.example.infoquizapp.data.gamequiz.network.ApiGameQuizService
import com.example.infoquizapp.data.gamequiz.network.Response
import com.example.infoquizapp.domain.gamequiz.repository.GameQuizRepository

class GameQuizRepositoryImpl(private val apiGameQuizService: ApiGameQuizService) :
    GameQuizRepository {

    override suspend fun getGameQuiz(difficulty: String, token: String): Response<GameQuizOut> {
        return apiGameQuizService.getGameQuiz(difficulty ,token)
    }
    override suspend fun completeGameQuiz(experience: Int, gameQuizId: Int, token: String): Response<CompleteGameQuizResponse> {
        return apiGameQuizService.completeGameQuiz(experience, gameQuizId, token)
    }
}
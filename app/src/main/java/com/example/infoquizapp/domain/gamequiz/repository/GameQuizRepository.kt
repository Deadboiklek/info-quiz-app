package com.example.infoquizapp.domain.gamequiz.repository

import com.example.infoquizapp.data.gamequiz.model.CompleteGameQuizResponse
import com.example.infoquizapp.data.gamequiz.model.GameQuizOut
import com.example.infoquizapp.data.gamequiz.network.Response

interface GameQuizRepository {
    suspend fun getGameQuiz(difficulty: String, token: String): Response<GameQuizOut>
    suspend fun completeGameQuiz(experience: Int,gameQuizId: Int , token: String): Response<CompleteGameQuizResponse>
}
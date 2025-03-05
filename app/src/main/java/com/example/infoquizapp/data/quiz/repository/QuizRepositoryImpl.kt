package com.example.infoquizapp.data.quiz.repository

import com.example.infoquizapp.data.quiz.model.AnswerIn
import com.example.infoquizapp.data.quiz.model.AnswerOut
import com.example.infoquizapp.data.quiz.model.QuizOut
import com.example.infoquizapp.data.quiz.network.QuizApiService
import com.example.infoquizapp.data.quiz.network.Response
import com.example.infoquizapp.domain.quiz.repository.QuizRepository

class QuizRepositoryImpl(private val apiService: QuizApiService) : QuizRepository {
    override suspend fun getTestQuizzes(quizType: String, token: String): Response<List<QuizOut>> {
        return apiService.getTestQuizzes(quizType, token)
    }

    override suspend fun getTrialTest(token: String): Response<List<QuizOut>> {
        return apiService.getTrialTest(token)
    }

    override suspend fun submitAnswer(answer: AnswerIn, token: String): Response<AnswerOut> {
        return apiService.submitAnswer(answer, token)
    }
}
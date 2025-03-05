package com.example.infoquizapp.domain.quiz.repository

import com.example.infoquizapp.data.quiz.model.AnswerIn
import com.example.infoquizapp.data.quiz.model.AnswerOut
import com.example.infoquizapp.data.quiz.model.QuizOut
import com.example.infoquizapp.data.quiz.network.Response

interface QuizRepository {
    suspend fun getTestQuizzes(quizType: String, token: String): Response<List<QuizOut>>
    suspend fun getTrialTest(token: String): Response<List<QuizOut>>
    suspend fun submitAnswer(answer: AnswerIn, token: String): Response<AnswerOut>
}
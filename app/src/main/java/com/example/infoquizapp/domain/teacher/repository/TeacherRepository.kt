package com.example.infoquizapp.domain.teacher.repository

import com.example.infoquizapp.data.quiz.model.QuizOut
import com.example.infoquizapp.data.teacher.model.TeacherCreateQuiz
import com.example.infoquizapp.data.teacher.model.TeacherProfile
import com.example.infoquizapp.data.teacher.network.Response

interface TeacherRepository {
    suspend fun getTeacherProfile(token: String): Response<TeacherProfile>
    suspend fun postTeacherQuiz(token: String, quiz: TeacherCreateQuiz) : Response<QuizOut>
    suspend fun getTeacherQuizzes(token: String): Response<List<QuizOut>>
    suspend fun deleteTeacherQuiz(token: String, quizId: Int): Response<QuizOut>
}
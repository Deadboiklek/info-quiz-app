package com.example.infoquizapp.domain.teacher.repository

import com.example.infoquizapp.data.quiz.model.QuizOut
import com.example.infoquizapp.data.teacher.model.StudentInfo
import com.example.infoquizapp.data.teacher.model.StudentStatistics
import com.example.infoquizapp.data.teacher.model.TeacherCreateQuiz
import com.example.infoquizapp.data.teacher.model.TeacherProfile
import com.example.infoquizapp.data.teacher.network.Response

interface TeacherRepository {
    suspend fun getTeacherProfile(token: String): Response<TeacherProfile>
    suspend fun postTeacherQuiz(
        token: String,
        question: String,
        correctAnswer: String,
        experienceReward: Int,
        type: String,
        imageBytes: ByteArray?
    ): Response<QuizOut>
    suspend fun getTeacherQuizzes(token: String): Response<List<QuizOut>>
    suspend fun deleteTeacherQuiz(token: String, quizId: Int): Response<QuizOut>
    suspend fun getTeacherStudents(token: String): Response<List<StudentInfo>>
    suspend fun getStudentStatistics(token: String, studentId: Int): Response<StudentStatistics>
}
package com.example.infoquizapp.data.teacher.repository

import com.example.infoquizapp.data.teacher.model.TeacherCreateQuiz
import com.example.infoquizapp.data.teacher.network.TeacherApiService
import com.example.infoquizapp.domain.teacher.repository.TeacherRepository

class TeacherRepositoryImpl(private val teacherApiService: TeacherApiService) : TeacherRepository {
    override suspend fun getTeacherProfile(token: String) = teacherApiService.getTeacherProfile(token)
    override suspend fun postTeacherQuiz(token: String, quiz: TeacherCreateQuiz) = teacherApiService.postQuiz(token, quiz)
    override suspend fun getTeacherQuizzes(token: String) = teacherApiService.getTeacherQuizzes(token)
    override suspend fun deleteTeacherQuiz(token: String, quizId: Int) = teacherApiService.deleteTeacherQuiz(token, quizId)
}
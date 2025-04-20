package com.example.infoquizapp.data.teacher.repository

import com.example.infoquizapp.data.teacher.model.StudentInfo
import com.example.infoquizapp.data.teacher.model.StudentStatistics
import com.example.infoquizapp.data.teacher.model.TeacherCreateQuiz
import com.example.infoquizapp.data.teacher.model.TeacherUpdate
import com.example.infoquizapp.data.teacher.network.Response
import com.example.infoquizapp.data.teacher.network.TeacherApiService
import com.example.infoquizapp.domain.teacher.repository.TeacherRepository

class TeacherRepositoryImpl(private val teacherApiService: TeacherApiService) : TeacherRepository {
    override suspend fun getTeacherProfile(token: String) = teacherApiService.getTeacherProfile(token)
    override suspend fun postTeacherQuiz(
        token: String,
        question: String,
        correctAnswer: String,
        experienceReward: Int,
        type: String,
        imageBytes: ByteArray?
    ) = teacherApiService.postQuiz(token, question, correctAnswer, experienceReward, type, imageBytes)

    override suspend fun updateTeacherQuiz(
        token: String,
        quizId: Int,
        question: String,
        correctAnswer: String,
        experienceReward: Int,
        type: String,
        imageBytes: ByteArray?
    ) = teacherApiService.updateQuiz(token, quizId, question, correctAnswer, experienceReward, type, imageBytes)

    override suspend fun getTeacherQuizzes(token: String) = teacherApiService.getTeacherQuizzes(token)
    override suspend fun deleteTeacherQuiz(token: String, quizId: Int) = teacherApiService.deleteTeacherQuiz(token, quizId)
    override suspend fun getTeacherStudents(token: String): Response<List<StudentInfo>> =
        teacherApiService.getTeacherStudents(token)

    override suspend fun updateProfile(token: String, update: TeacherUpdate) =
        teacherApiService.updateTeacherProfile(token, update)

    override suspend fun getStudentStatistics(token: String, studentId: Int): Response<StudentStatistics> =
        teacherApiService.getStudentStatistics(token, studentId)
}
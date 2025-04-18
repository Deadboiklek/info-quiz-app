package com.example.infoquizapp.domain.teacher.usecases

import com.example.infoquizapp.data.teacher.model.TeacherCreateQuiz
import com.example.infoquizapp.data.teacher.network.Response
import com.example.infoquizapp.domain.teacher.repository.TeacherRepository

class PostTeacherQuizUseCase(private val teacherRepository: TeacherRepository) {
    suspend operator fun invoke(
        token: String,
        question: String,
        correctAnswer: String,
        experienceReward: Int,
        type: String,
        imageBytes: ByteArray?
    ): PostTeacherQuizResult {
        return when (val resp = teacherRepository.postTeacherQuiz(
            token, question, correctAnswer, experienceReward, type, imageBytes
        )) {
            is Response.Succes -> PostTeacherQuizResult(resp.result, null)
            is Response.Error  -> PostTeacherQuizResult(null, resp.error.message)
        }
    }
}
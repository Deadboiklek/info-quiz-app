package com.example.infoquizapp.domain.teacher.usecases

import com.example.infoquizapp.data.teacher.network.Response
import com.example.infoquizapp.domain.teacher.repository.TeacherRepository

class UpdateTeacherQuizUseCase(
    private val repo: TeacherRepository
) {
    suspend operator fun invoke(
        token: String,
        quizId: Int,
        question: String,
        correctAnswer: String,
        experienceReward: Int,
        type: String,
        imageBytes: ByteArray?
    ): UpdateQuizResult = when(val r = repo.updateTeacherQuiz(
        token, quizId, question, correctAnswer, experienceReward, type, imageBytes
    )) {
        is Response.Succes -> UpdateQuizResult(r.result, null)
        is Response.Error  -> UpdateQuizResult(null, r.error.message)
    }
}
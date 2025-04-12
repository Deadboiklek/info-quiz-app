package com.example.infoquizapp.domain.teacher.usecases

import com.example.infoquizapp.data.teacher.network.Response
import com.example.infoquizapp.domain.teacher.repository.TeacherRepository

class DeleteTeacherQuizUseCase(private val teacherRepository: TeacherRepository) {
    suspend operator fun invoke(token: String, quizId: Int): DeleteTeacherQuizResult {
        return when (val result = teacherRepository.deleteTeacherQuiz(token, quizId)) {
            is Response.Succes -> DeleteTeacherQuizResult(deletedQuiz = result.result, error = null)
            is Response.Error -> DeleteTeacherQuizResult(deletedQuiz = null, error = result.error.message)
        }
    }
}
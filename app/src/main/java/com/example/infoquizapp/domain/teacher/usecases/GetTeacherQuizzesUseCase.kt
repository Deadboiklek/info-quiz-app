package com.example.infoquizapp.domain.teacher.usecases

import com.example.infoquizapp.data.teacher.network.Response
import com.example.infoquizapp.domain.teacher.repository.TeacherRepository

class GetTeacherQuizzesUseCase(private val teacherRepository: TeacherRepository) {
    suspend operator fun invoke(token: String): GetTeacherQuizzesResult {
        return when (val result = teacherRepository.getTeacherQuizzes(token)) {
            is Response.Succes -> GetTeacherQuizzesResult(quizzes = result.result, error = null)
            is Response.Error -> GetTeacherQuizzesResult(quizzes = null, error = result.error.message)
        }
    }
}
package com.example.infoquizapp.domain.teacher.usecases

import com.example.infoquizapp.data.teacher.model.TeacherCreateQuiz
import com.example.infoquizapp.data.teacher.network.Response
import com.example.infoquizapp.domain.teacher.repository.TeacherRepository

class PostTeacherQuizUseCase(private val teacherRepository: TeacherRepository) {
    suspend operator fun invoke(token: String, quiz: TeacherCreateQuiz): PostTeacherQuizResult {
        return when(val postTeacherQuiz = teacherRepository.postTeacherQuiz(token, quiz)) {
            is Response.Error -> PostTeacherQuizResult(response = null, error = postTeacherQuiz.error.message)
            is Response.Succes -> PostTeacherQuizResult(response = postTeacherQuiz, error = null)
        }
    }
}
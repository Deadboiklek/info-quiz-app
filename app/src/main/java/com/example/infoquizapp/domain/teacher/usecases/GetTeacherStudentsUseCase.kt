package com.example.infoquizapp.domain.teacher.usecases

import com.example.infoquizapp.data.teacher.network.Response
import com.example.infoquizapp.domain.teacher.repository.TeacherRepository

class GetTeacherStudentsUseCase(private val teacherRepository: TeacherRepository) {
    suspend operator fun invoke(token: String): GetTeacherStudentsResult {
        return when(val result = teacherRepository.getTeacherStudents(token)) {
            is Response.Succes -> GetTeacherStudentsResult(students = result.result, error = null)
            is Response.Error -> GetTeacherStudentsResult(students = null, error = result.error.message)
        }
    }
}
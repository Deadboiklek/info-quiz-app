package com.example.infoquizapp.domain.teacher.usecases

import com.example.infoquizapp.data.teacher.network.Response
import com.example.infoquizapp.domain.teacher.repository.TeacherRepository

class GetStudentStatisticsUseCase(private val teacherRepository: TeacherRepository) {
    suspend operator fun invoke(token: String, studentId: Int): GetStudentStatisticsResult {
        return when(val result = teacherRepository.getStudentStatistics(token, studentId)) {
            is Response.Succes -> GetStudentStatisticsResult(statistics = result.result, error = null)
            is Response.Error -> GetStudentStatisticsResult(statistics = null, error = result.error.message)
        }
    }
}
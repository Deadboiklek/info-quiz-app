package com.example.infoquizapp.domain.teacher.usecases

import com.example.infoquizapp.data.teacher.network.Response
import com.example.infoquizapp.domain.teacher.repository.TeacherRepository

class GetTeacherProfileUseCase(private val teacherRepository: TeacherRepository) {
    suspend operator fun invoke(token: String): TeacherProfileResult {
        return when (val teacherProfile = teacherRepository.getTeacherProfile(token)) {
            is Response.Error -> TeacherProfileResult(teacherProfile = null, error = teacherProfile.error.message)
            is Response.Succes -> TeacherProfileResult(teacherProfile = teacherProfile, error = null)
        }
    }
}
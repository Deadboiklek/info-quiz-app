package com.example.infoquizapp.data.teacher.repository

import com.example.infoquizapp.data.teacher.network.TeacherApiService
import com.example.infoquizapp.domain.teacher.TeacherRepository

class TeacherRepositoryImpl(private val teacherApiService: TeacherApiService) : TeacherRepository {
    override suspend fun getTeacherProfile(token: String) = teacherApiService.getTeacherProfile(token)
}
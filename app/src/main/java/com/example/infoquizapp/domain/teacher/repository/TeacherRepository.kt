package com.example.infoquizapp.domain.teacher.repository

import com.example.infoquizapp.data.teacher.model.TeacherProfile
import com.example.infoquizapp.data.teacher.network.Response

interface TeacherRepository {
    suspend fun getTeacherProfile(token: String): Response<TeacherProfile>
}
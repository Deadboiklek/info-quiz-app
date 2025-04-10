package com.example.infoquizapp.data.teacher.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TeacherProfile(
    val id: Int,
    val username: String,
    val firstname: String,
    val surname: String,
    val patronymic: String,
    val email: String,
    @SerialName("teacher_code") val teacherCode: String
)
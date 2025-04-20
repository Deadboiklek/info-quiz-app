package com.example.infoquizapp.data.teacher.model

import kotlinx.serialization.Serializable

@Serializable
data class TeacherUpdate(
    val username: String? = null,
    val email: String? = null,
    val firstname: String? = null,
    val surname: String? = null,
    val patronymic: String? = null,
    val password: String? = null,
)

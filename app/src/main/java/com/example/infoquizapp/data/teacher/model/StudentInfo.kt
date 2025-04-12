package com.example.infoquizapp.data.teacher.model

import kotlinx.serialization.Serializable

@Serializable
data class StudentInfo(
    val id: Int,
    val username: String,
    val email: String
)

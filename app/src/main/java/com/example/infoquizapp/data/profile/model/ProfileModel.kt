package com.example.infoquizapp.data.profile.model

data class UserOut(
    val id: Int,
    val username: String,
    val email: String,
    val level: Int,
    val experience: Int,
    val createdAt: String
)
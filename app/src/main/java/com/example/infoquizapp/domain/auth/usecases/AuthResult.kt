package com.example.infoquizapp.domain.auth.usecases

data class AuthResult(
    val token: String?,
    val error: String?
)

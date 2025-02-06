package com.example.infoquizapp.domain.auth.usecases

import com.example.infoquizapp.domain.auth.repository.AuthRepository

class LoginUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String): AuthResult {
        return try {
            val token = repository.login(email, password)
            AuthResult(token = token, error = null)
        } catch (e: Exception) {
            AuthResult(token = null, error = e.message ?: "Ошибка авторизации")
        }
    }
}
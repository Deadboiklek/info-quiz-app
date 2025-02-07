package com.example.infoquizapp.domain.auth.usecases

import com.example.infoquizapp.domain.auth.repository.AuthRepository

class RegisterUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(username: String, email: String, password: String): AuthResult {
        return try {
            val token = repository.register(username, email, password)
            AuthResult(token = token, error = null)
        } catch (e: Exception) {
            AuthResult(token = null, error = e.message ?: "Ошибка регистрации")
        }
    }
}
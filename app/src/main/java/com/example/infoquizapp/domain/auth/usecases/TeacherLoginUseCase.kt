package com.example.infoquizapp.domain.auth.usecases

import com.example.infoquizapp.data.auth.network.Response
import com.example.infoquizapp.domain.auth.repository.AuthRepository

class TeacherLoginUseCase (private val repository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String): AuthResult {
        val token = repository.teacherLogin(email, password)
        return when(token) {
            is Response.Error -> AuthResult(token = null, error = token.error.message)
            is Response.Succes -> AuthResult(token = token.result.accessToken, error = null)
        }
    }
}
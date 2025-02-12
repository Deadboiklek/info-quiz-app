package com.example.infoquizapp.domain.auth.repository

import com.example.infoquizapp.data.auth.model.TokenResponse
import com.example.infoquizapp.data.auth.network.Response

interface AuthRepository {
    suspend fun register(username: String, email: String, password: String): Response<TokenResponse> // напоминалочка! тут возвращаем токен
    suspend fun login(email: String, password: String): Response<TokenResponse>
}
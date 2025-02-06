package com.example.infoquizapp.data.auth.repository

import com.example.infoquizapp.data.auth.model.UserCreate
import com.example.infoquizapp.data.auth.model.UserLogin
import com.example.infoquizapp.data.auth.network.ApiAuthService
import com.example.infoquizapp.domain.auth.repository.AuthRepository

class AuthRepositoryImpl(private val apiAuthService: ApiAuthService) : AuthRepository {
    override suspend fun register(username: String, email: String, password: String): String {
        val tokenResponse = apiAuthService.register(UserCreate(username, email, password))
        return tokenResponse.access_token
    }

    override suspend fun login(email: String, password: String): String {
        val tokenResponse = apiAuthService.login(UserLogin(email, password))
        return tokenResponse.access_token
    }
}
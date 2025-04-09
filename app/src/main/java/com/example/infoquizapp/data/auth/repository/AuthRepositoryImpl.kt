package com.example.infoquizapp.data.auth.repository

import com.example.infoquizapp.data.auth.model.TeacherLogin
import com.example.infoquizapp.data.auth.model.TokenResponse
import com.example.infoquizapp.data.auth.model.UserCreate
import com.example.infoquizapp.data.auth.model.UserLogin
import com.example.infoquizapp.data.auth.network.ApiAuthService
import com.example.infoquizapp.data.auth.network.Response
import com.example.infoquizapp.domain.auth.repository.AuthRepository

class AuthRepositoryImpl(private val apiAuthService: ApiAuthService) : AuthRepository {
    override suspend fun register(username: String, email: String, password: String, teacherCode: String?): Response<TokenResponse> {
        val tokenResponse = apiAuthService.register(UserCreate(username, email, password, teacherCode))
        return tokenResponse
    }

    override suspend fun login(email: String, password: String): Response<TokenResponse> {
        val tokenResponse = apiAuthService.login(UserLogin(email, password))
        return tokenResponse
    }

    override suspend fun teacherLogin(email: String, password: String): Response<TokenResponse> {
        val tokenResponse = apiAuthService.teacherLogin(TeacherLogin(email, password))
        return tokenResponse
    }
}
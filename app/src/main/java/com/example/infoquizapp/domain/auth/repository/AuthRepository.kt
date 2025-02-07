package com.example.infoquizapp.domain.auth.repository

interface AuthRepository {
    suspend fun register(username: String, email: String, password: String): String // напоминалочка! тут возвращаем токен
    suspend fun login(email: String, password: String): String
}
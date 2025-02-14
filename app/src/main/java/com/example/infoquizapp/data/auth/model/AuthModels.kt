package com.example.infoquizapp.data.auth.model

data class TokenResponse(val accessToken: String, val tokenType: String)

data class UserCreate(val username: String, val email: String, val password: String)
data class UserLogin(val email: String, val password: String)

package com.example.infoquizapp.data.auth.model

data class TokenResponse(val access_token: String, val token_type: String)

data class UserCreate(val username: String, val email: String, val password: String)
data class UserLogin(val email: String, val password: String)

package com.example.infoquizapp.data.model

data class RegisterRequest(val username: String, val email: String, val password: String)
data class LoginRequest(val email: String, val password: String)
data class AuthResponse(val accessToken: String, val refreshToken: String)
data class ResetPasswordRequest(val email: String)
data class ApiResponse(val message: String)

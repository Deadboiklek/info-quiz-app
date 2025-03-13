package com.example.infoquizapp.data.auth.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
@Serializable
data class TokenResponse(
    @SerialName("access_token") val accessToken: String,
    @SerialName("token_type") val tokenType: String
)

@Serializable
data class UserCreate(val username: String, val email: String, val password: String)

@Serializable
data class UserLogin(val email: String, val password: String)

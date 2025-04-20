package com.example.infoquizapp.data.profile.model

import kotlinx.serialization.Serializable

@Serializable
data class UserUpdate(
    val username: String? = null,
    val email: String? = null,
    val password: String? = null
)

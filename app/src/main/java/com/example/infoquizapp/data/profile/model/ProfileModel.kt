package com.example.infoquizapp.data.profile.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserOut(
    val id: Int,
    val username: String,
    val email: String,
    val level: Int? = null,
    val experience: Int? = null,
    @SerialName("created_at") val createdAt: String? = null
)
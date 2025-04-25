package com.example.infoquizapp.data.profile.model

import kotlinx.serialization.Serializable

@Serializable
data class LeaderboardEntry(
    val id: Int,
    val username: String,
    val level: Int,
    val experience: Int
)



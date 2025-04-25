package com.example.infoquizapp.data.profile.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LeaderboardOut(
    @SerialName("top_players")
    val topPlayers: List<LeaderboardEntry>
)

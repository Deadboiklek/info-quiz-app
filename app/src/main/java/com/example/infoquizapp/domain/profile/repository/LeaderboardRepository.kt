package com.example.infoquizapp.domain.profile.repository

import com.example.infoquizapp.data.profile.model.LeaderboardOut
import com.example.infoquizapp.data.profile.network.Response

interface LeaderboardRepository {
    suspend fun getLeaderboard(token: String): Response<LeaderboardOut>
}
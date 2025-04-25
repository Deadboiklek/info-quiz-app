package com.example.infoquizapp.data.profile.repository

import com.example.infoquizapp.data.profile.network.ApiProfileService
import com.example.infoquizapp.domain.profile.repository.LeaderboardRepository

class LeaderboardRepositoryImpl(
    private val api: ApiProfileService
) : LeaderboardRepository {
    override suspend fun getLeaderboard(token: String) =
        api.getLeaderboard(token)
}
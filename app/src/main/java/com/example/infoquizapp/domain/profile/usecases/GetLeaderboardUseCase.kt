package com.example.infoquizapp.domain.profile.usecases

import com.example.infoquizapp.data.profile.model.LeaderboardOut
import com.example.infoquizapp.data.profile.network.Response
import com.example.infoquizapp.domain.profile.repository.LeaderboardRepository

class GetLeaderboardUseCase(
    private val repo: LeaderboardRepository
) {
    suspend operator fun invoke(token: String): Response<LeaderboardOut> =
        repo.getLeaderboard(token)
}
package com.example.infoquizapp.domain.achievement.usecases

import com.example.infoquizapp.data.achievement.network.Response
import com.example.infoquizapp.domain.achievement.repository.AchievementRepository

class GetUserAchievementsUseCase(
    private val repository: AchievementRepository
) {
    suspend operator fun invoke(token: String): AchievementsResult {
        return when (val response = repository.getUserAchievements(token)) {
            is Response.Success -> AchievementsResult(response.result, null)
            is Response.Error -> AchievementsResult(null, response.error.message)
        }
    }
}
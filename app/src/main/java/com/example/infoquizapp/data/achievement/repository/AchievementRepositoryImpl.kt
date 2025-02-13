package com.example.infoquizapp.data.achievement.repository

import com.example.infoquizapp.data.achievement.model.AchievementOut
import com.example.infoquizapp.data.achievement.network.ApiAchievementsService
import com.example.infoquizapp.data.achievement.network.Response
import com.example.infoquizapp.domain.achievement.repository.AchievementRepository

class AchievementsRepositoryImpl(
    private val apiAchievementsService: ApiAchievementsService
) : AchievementRepository {
    override suspend fun getAchievements(): Response<List<AchievementOut>> {
        return apiAchievementsService.getAchievements()
    }

    override suspend fun getUserAchievements(token: String): Response<List<AchievementOut>> {
        return apiAchievementsService.getUserAchievements(token)
    }
}
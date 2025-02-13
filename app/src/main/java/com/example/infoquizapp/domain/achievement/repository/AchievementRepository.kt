package com.example.infoquizapp.domain.achievement.repository

import com.example.infoquizapp.data.achievement.model.AchievementOut
import com.example.infoquizapp.data.achievement.network.Response

interface AchievementRepository {
    suspend fun getAchievements(): Response<List<AchievementOut>>
    suspend fun getUserAchievements(token: String): Response<List<AchievementOut>>
}
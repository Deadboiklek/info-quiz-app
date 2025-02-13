package com.example.infoquizapp.domain.achievement.usecases

import com.example.infoquizapp.data.achievement.model.AchievementOut

data class AchievementsResult(
    val achievements: List<AchievementOut>?,
    val error: String?
)

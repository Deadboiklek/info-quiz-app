package com.example.infoquizapp.presentation.achievement.viewmodel

import com.example.infoquizapp.data.achievement.model.AchievementOut

data class AchievementsUiModel(
    val achievement: AchievementOut,
    val isObtained: Boolean
)

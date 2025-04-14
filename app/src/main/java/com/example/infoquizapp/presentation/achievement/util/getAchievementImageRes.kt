package com.example.infoquizapp.presentation.achievement.util

import com.example.infoquizapp.R

fun getAchievementImageRes(imageName: String): Int {
    return when (imageName) {
        "achievement_start" -> R.drawable.achievement_start
        "achievement_astronaut" -> R.drawable.achievement_astronaut
        "achievement_real" -> R.drawable.achievement_real
        "achievement_legend" -> R.drawable.achievement_legend
        "achievement_octopus" -> R.drawable.achievement_octopus
        else -> R.drawable.achievement_default
    }
}
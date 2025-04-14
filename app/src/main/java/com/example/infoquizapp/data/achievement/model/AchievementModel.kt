package com.example.infoquizapp.data.achievement.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AchievementOut(
    val id: Int,
    val name: String,
    val description: String,
    @SerialName("experience_bonus") val experienceBonus: Int,
    @SerialName("image_name") val imageName: String
)

package com.example.infoquizapp.data.quest.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QuestOut(
    val id: Int,
    val title: String,
    val description: String,
    @SerialName("experience_reward") val experienceReward: Int,
    @SerialName("is_active") val isActive: Boolean,
    @SerialName("image_name") val imageName: String
)

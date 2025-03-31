package com.example.infoquizapp.data.quest.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CompleteQuestResponse(
    val message: String,
    @SerialName("new_level") val newLevel: Int,
    val experience: Int,
    @SerialName("level_up") val levelUp: Int
)

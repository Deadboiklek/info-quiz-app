package com.example.infoquizapp.data.quiz.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QuizOut(
    val id: Int,
    val question: String,
    @SerialName("experience_reward") val experienceReward: Int,
    val type: String,
    val image: String? = null
)

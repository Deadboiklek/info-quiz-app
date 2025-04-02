package com.example.infoquizapp.data.gamequiz.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GameQuizUserOut(
    val id: Int,
    val question: String,
    val option1: String,
    val option2: String,
    @SerialName("correct_answer") val correctAnswer: String,
    @SerialName("experience_reward") val experienceReward: Int
)
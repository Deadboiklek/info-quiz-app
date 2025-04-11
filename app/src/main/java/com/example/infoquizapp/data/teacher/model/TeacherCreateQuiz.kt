package com.example.infoquizapp.data.teacher.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TeacherCreateQuiz(
    val question: String,
    val options: List<String>? = null,
    @SerialName("correct_answer") val correctAnswer: String,
    @SerialName("experience_reward") val experienceReward: Int,
    val type: String
)
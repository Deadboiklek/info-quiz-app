package com.example.infoquizapp.data.profile.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserStatistics(
    @SerialName("student_id") val studentId: Int,
    val username: String,
    val email: String,
    @SerialName("total_quests_completed") val totalQuestsCompleted: Int,
    @SerialName("total_quiz_correct") val totalQuizCorrect: Int,
    @SerialName("total_experience") val totalExperience: Int,
    val level: Int
)

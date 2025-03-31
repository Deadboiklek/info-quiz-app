package com.example.infoquizapp.data.quiz.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AnswerIn(
    @SerialName("quiz_id") val quizId: Int,
    @SerialName("user_answer") val userAnswer: String
)

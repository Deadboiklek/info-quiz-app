package com.example.infoquizapp.data.quiz.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AnswerOut(
    @SerialName("is_correct") val isCorrect: Boolean,
    @SerialName("correct_answer") val correctAnswer: String
)

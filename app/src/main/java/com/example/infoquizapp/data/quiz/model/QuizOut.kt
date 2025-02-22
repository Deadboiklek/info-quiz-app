package com.example.infoquizapp.data.quiz.model

data class QuizOut(
    val id: Int,
    val question: String,
    val options: List<String>,
    val experienceReward: Int,
    val type: String
)

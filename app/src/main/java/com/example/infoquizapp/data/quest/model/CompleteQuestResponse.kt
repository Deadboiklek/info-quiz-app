package com.example.infoquizapp.data.quest.model

data class CompleteQuestResponse(
    val message: String,
    val newLevel: Int,
    val experience: Int,
    val levelUp: Int
)

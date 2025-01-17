package com.example.infoquizapp.view.component.practicecontentscreencomponent.data

data class PracticeContentData(
    val id: Int,
    val questionText: String,
    val answers: List<String>,
    val correctAnswerIndex: Int
)

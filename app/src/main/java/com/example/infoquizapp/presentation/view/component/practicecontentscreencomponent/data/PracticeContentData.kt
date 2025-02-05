package com.example.infoquizapp.presentation.view.component.practicecontentscreencomponent.data

data class PracticeContentData(
    val id: Int,
    val questionText: String,
    val answers: List<String>,
    val correctAnswerIndex: Int
)

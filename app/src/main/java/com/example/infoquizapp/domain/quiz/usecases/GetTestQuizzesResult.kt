package com.example.infoquizapp.domain.quiz.usecases

import com.example.infoquizapp.data.quiz.model.QuizOut

data class GetTestQuizzesResult(
    val quizzes: List<QuizOut>?,
    val error: String?
)

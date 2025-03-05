package com.example.infoquizapp.domain.quiz.usecases

import com.example.infoquizapp.data.quiz.model.QuizOut

data class GetTrialTestResult(
    val quizzes: List<QuizOut>?,
    val error: String?
)

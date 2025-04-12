package com.example.infoquizapp.domain.teacher.usecases

import com.example.infoquizapp.data.quiz.model.QuizOut

data class GetTeacherQuizzesResult(
    val quizzes: List<QuizOut>? = null,
    val error: String?
)

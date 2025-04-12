package com.example.infoquizapp.domain.teacher.usecases

import com.example.infoquizapp.data.quiz.model.QuizOut

data class DeleteTeacherQuizResult(
    val deletedQuiz: QuizOut? = null,
    val error: String?
)
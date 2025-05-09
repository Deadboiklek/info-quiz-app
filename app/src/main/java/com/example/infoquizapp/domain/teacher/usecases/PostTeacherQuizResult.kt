package com.example.infoquizapp.domain.teacher.usecases

import com.example.infoquizapp.data.quiz.model.QuizOut
import com.example.infoquizapp.data.teacher.network.Response

data class PostTeacherQuizResult(
    val quiz: QuizOut?,
    val error: String?
)

package com.example.infoquizapp.domain.teacher.usecases

import com.example.infoquizapp.data.quiz.model.QuizOut

data class UpdateQuizResult(val quiz: QuizOut?, val error: String?)

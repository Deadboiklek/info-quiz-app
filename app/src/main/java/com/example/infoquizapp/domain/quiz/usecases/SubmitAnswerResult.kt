package com.example.infoquizapp.domain.quiz.usecases

import com.example.infoquizapp.data.quiz.model.AnswerOut

data class SubmitAnswerResult(
    val response: AnswerOut?,
    val error: String?
)

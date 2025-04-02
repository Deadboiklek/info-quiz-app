package com.example.infoquizapp.domain.gamequiz.usecases

import com.example.infoquizapp.data.gamequiz.model.GameQuizOut

data class GetGameQuizResult(
    val gameQuiz: GameQuizOut?,
    val error: String?
)
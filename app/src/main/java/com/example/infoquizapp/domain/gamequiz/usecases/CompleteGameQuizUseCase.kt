package com.example.infoquizapp.domain.gamequiz.usecases

import com.example.infoquizapp.domain.gamequiz.repository.GameQuizRepository

class CompleteGameQuizUseCase(private val repository: GameQuizRepository) {
    suspend operator fun invoke(experience: Int, token: String) {
        repository.completeGameQuiz(experience, token)
    }
}
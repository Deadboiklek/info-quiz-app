package com.example.infoquizapp.domain.quiz.usecases

import com.example.infoquizapp.data.quiz.network.Response
import com.example.infoquizapp.domain.quiz.repository.QuizRepository

class GetTrialTestUseCase(private val repository: QuizRepository) {
    suspend operator fun invoke(token: String): GetTrialTestResult {
        return when(val respons = repository.getTrialTest(token)) {
            is Response.Success -> GetTrialTestResult(respons.result, null)
            is Response.Error -> GetTrialTestResult(null, respons.error.message)
        }
    }
}
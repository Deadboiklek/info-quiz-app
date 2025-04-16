package com.example.infoquizapp.domain.profile.usecases

import com.example.infoquizapp.data.profile.network.Response
import com.example.infoquizapp.domain.profile.repository.ProfileRepository

class GetStatisticsUseCase(private val profileRepository: ProfileRepository) {
    suspend operator fun invoke(token: String): StatisticsResult {
        return when(val statistics = profileRepository.getStatistics(token)) {
            is Response.Error -> StatisticsResult(statistics = null, error = statistics.error.message)
            is Response.Succes -> StatisticsResult(statistics = statistics, error = null)
        }
    }
}
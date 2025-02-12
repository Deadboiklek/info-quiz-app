package com.example.infoquizapp.domain.profile.usecases

import com.example.infoquizapp.data.profile.network.Response
import com.example.infoquizapp.domain.profile.repository.ProfileRepository

class GetProfileUseCase(private val profileRepository: ProfileRepository) {
    suspend operator fun invoke(token: String): ProfileResult {
        return when(val profile = profileRepository.getProfile(token)) {
            is Response.Error -> ProfileResult(profile = null, error = profile.error.message)
            is Response.Succes -> ProfileResult(profile = profile, error = null)
        }
    }
}
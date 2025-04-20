package com.example.infoquizapp.domain.profile.usecases

import com.example.infoquizapp.data.profile.model.UserUpdate
import com.example.infoquizapp.data.profile.network.Response
import com.example.infoquizapp.domain.profile.repository.ProfileRepository

class UpdateProfileUseCase(
    private val repo: ProfileRepository
) {
    suspend operator fun invoke(token: String, update: UserUpdate): ProfileUpdateResult {
        return when (val resp = repo.updateProfile(token, update)) {
            is Response.Succes -> ProfileUpdateResult(resp.result, null)
            is Response.Error  -> ProfileUpdateResult(null, resp.error.message)
        }
    }
}
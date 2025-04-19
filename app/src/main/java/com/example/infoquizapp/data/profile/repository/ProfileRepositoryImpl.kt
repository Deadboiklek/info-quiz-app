package com.example.infoquizapp.data.profile.repository

import com.example.infoquizapp.data.profile.model.UserOut
import com.example.infoquizapp.data.profile.model.UserStatistics
import com.example.infoquizapp.data.profile.model.UserUpdate
import com.example.infoquizapp.data.profile.network.ApiProfileService
import com.example.infoquizapp.data.profile.network.Response
import com.example.infoquizapp.domain.profile.repository.ProfileRepository

class ProfileRepositoryImpl(private val apiProfileService: ApiProfileService) : ProfileRepository {
    override suspend fun getProfile(token: String): Response<UserOut> {
        return apiProfileService.getProfile(token)
    }

    override suspend fun getStatistics(token: String): Response<UserStatistics> {
        return apiProfileService.getStatistics(token)
    }

    override suspend fun updateProfile(token: String, update: UserUpdate) =
        apiProfileService.updateProfile(token, update)
}
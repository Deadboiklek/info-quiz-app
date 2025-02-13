package com.example.infoquizapp.data.profile.repository

import com.example.infoquizapp.data.profile.model.UserOut
import com.example.infoquizapp.data.profile.network.ApiProfileService
import com.example.infoquizapp.data.profile.network.Response
import com.example.infoquizapp.domain.profile.repository.ProfileRepository

class ProfileRepositoryImpl(private val apiProfileService: ApiProfileService) : ProfileRepository {
    override suspend fun getProfile(token: String): Response<UserOut> {
        return apiProfileService.getProfile(token)
    }
}
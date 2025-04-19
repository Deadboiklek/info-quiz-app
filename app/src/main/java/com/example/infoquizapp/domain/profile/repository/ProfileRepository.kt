package com.example.infoquizapp.domain.profile.repository

import com.example.infoquizapp.data.profile.model.UserOut
import com.example.infoquizapp.data.profile.model.UserStatistics
import com.example.infoquizapp.data.profile.model.UserUpdate
import com.example.infoquizapp.data.profile.network.Response

interface ProfileRepository {
    suspend fun getProfile(token: String): Response<UserOut>
    suspend fun updateProfile(token: String, update: UserUpdate): Response<UserOut>
    suspend fun getStatistics(token: String): Response<UserStatistics>

}
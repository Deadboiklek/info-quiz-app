package com.example.infoquizapp.domain.profile.repository

import com.example.infoquizapp.data.profile.model.UserOut
import com.example.infoquizapp.data.profile.network.Response

interface ProfileRepository {
    suspend fun getProfile(token: String): Response<UserOut>
}
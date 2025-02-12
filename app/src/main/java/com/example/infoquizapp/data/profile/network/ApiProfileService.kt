package com.example.infoquizapp.data.profile.network

import com.example.infoquizapp.data.auth.model.TokenResponse
import com.example.infoquizapp.data.profile.model.UserOut
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header

class ApiProfileService(private val client: HttpClient, private val baseUrl: String) {

    suspend fun getProfile(token: String): UserOut {
        return client.get("$baseUrl/profile") {
            header("Authorization", "Bearer $token")
        }.body()
    }
}
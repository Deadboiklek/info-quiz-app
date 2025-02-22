package com.example.infoquizapp.data.profile.network

import com.example.infoquizapp.data.profile.model.UserOut
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header

sealed class Response<T>{
    data class Succes<T>(
        val result: T
    ): Response<T>()

    data class Error<T>(
        val error: ProfileError
    ) : Response<T>()
}

sealed class ProfileError (val message: String?) {
    data object GetProfileError: ProfileError("Ошибка получения профиля")
}

class ApiProfileService(private val client: HttpClient, private val baseUrl: String) {

    suspend fun getProfile(token: String): Response<UserOut> {
        return kotlin.runCatching {
            Response.Succes(client.get("$baseUrl/profile") {
                header("Authorization", "Bearer $token")
            }.body<UserOut>())
        }.getOrDefault(Response.Error(ProfileError.GetProfileError))
    }
}
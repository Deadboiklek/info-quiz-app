package com.example.infoquizapp.data.profile.network

import android.util.Log
import com.example.infoquizapp.data.profile.model.UserOut
import com.example.infoquizapp.data.profile.model.UserStatistics
import com.example.infoquizapp.data.profile.model.UserUpdate
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.patch
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.headers

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
    data object GetStatisticsError: ProfileError("Ошибка получения статистики")
    object UsernameTaken : ProfileError("Username уже занят")
    object EmailTaken    : ProfileError("Email уже зарегистрирован")
    object TooShortPassword : ProfileError("Пароль слишком короткий")
    object Unknown       : ProfileError("Ошибка обновления профиля")
}

class ApiProfileService(private val client: HttpClient, private val baseUrl: String) {

    suspend fun getProfile(token: String): Response<UserOut> {
        return kotlin.runCatching {
            Response.Succes(client.get("$baseUrl/user/profile") {
                header("Authorization", "Bearer $token")
            }.body<UserOut>())
        }.fold(
            onSuccess = { it },
            onFailure = { ex ->
                Log.e("ApiProfileService", "Ошибка запроса профиля: ${ex.localizedMessage}", ex)
                Response.Error(ProfileError.GetProfileError)
            }
        )
    }

    suspend fun getStatistics(token: String): Response<UserStatistics>{
        return kotlin.runCatching {
            Response.Succes(client.get("$baseUrl/user/studentstats") {
                header("Authorization", "Bearer $token")
            }.body<UserStatistics>())
        }.fold(
            onSuccess = { it },
            onFailure = { ex ->
                Log.e ("ApiProfileService", "Ошибка запроса статистики: ${ex.localizedMessage}", ex)
                Response.Error(ProfileError.GetStatisticsError)
            }
        )
    }

    suspend fun updateProfile(
        token: String,
        payload: UserUpdate
    ): Response<UserOut> = kotlin.runCatching {
        Response.Succes(client.patch("$baseUrl/user/profileupdate") {
            header("Authorization", "Bearer $token")
            contentType(ContentType.Application.Json)
            setBody(payload)
        }.body<UserOut>())
    }.fold(
        onSuccess = { it },
        onFailure = {
            Response.Error(ProfileError.Unknown)
        }
    )

}
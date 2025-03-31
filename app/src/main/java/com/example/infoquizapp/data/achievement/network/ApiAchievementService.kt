package com.example.infoquizapp.data.achievement.network

import android.util.Log
import com.example.infoquizapp.data.achievement.model.AchievementOut
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header

sealed class Response<T> {
    data class Success<T>(val result: T) : Response<T>()
    data class Error<T>(val error: AchievementError) : Response<T>()
}

sealed class AchievementError(val message: String?) {
    object GetAchievementsError : AchievementError("Ошибка получения достижений")
}

class ApiAchievementsService(
    private val client: HttpClient,
    private val baseUrl: String
) {
    //получение всего списка достижений
    suspend fun getAchievements(): Response<List<AchievementOut>> {
        return kotlin.runCatching {
            Response.Success(client.get("$baseUrl/achievements/") {
            }.body<List<AchievementOut>>())
        }.fold(
            onSuccess = {it},
            onFailure = { ex ->
                Log.e("AchievementAuthService",
                    "Ошибка запроса достижений: ${ex.localizedMessage}", ex)
                Response.Error(AchievementError.GetAchievementsError)
            }
        )
    }

    //получение достижений текущего пользователя
    suspend fun getUserAchievements(token: String): Response<List<AchievementOut>> {
        return kotlin.runCatching {
            Response.Success(client.get("$baseUrl/achievements/my") {
                header("Authorization", "Bearer $token")
            }.body<List<AchievementOut>>())
        }.fold(
            onSuccess = {it},
            onFailure = { ex ->
                Log.e("AchievementAuthService",
                    "Ошибка запроса достижений: ${ex.localizedMessage}", ex)
                Response.Error(AchievementError.GetAchievementsError)
            }
        )
    }
}
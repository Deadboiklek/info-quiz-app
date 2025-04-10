package com.example.infoquizapp.data.teacher.network

import android.util.Log
import com.example.infoquizapp.data.teacher.model.TeacherProfile
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header

sealed class Response<T>{
    data class Succes<T>(
        val result: T
    ): Response<T>()

    data class Error<T>(
        val error: TeacherError
    ) : Response<T>()
}

sealed class TeacherError (val message: String?) {
    data object GetTeacherProfileError: TeacherError("Ошибка получения профиля")
}

class TeacherApiService(private val client: HttpClient, private val baseUrl: String) {

    suspend fun getTeacherProfile(token: String): Response<TeacherProfile> {
        return kotlin.runCatching {
            Response.Succes(client.get("$baseUrl/teacher/profile") {
                header("Authorization", "Bearer $token")
            }.body<TeacherProfile>())
        }.fold(
            onSuccess = { it },
            onFailure = { ex ->
                Log.e("TeacherApiService", "Ошибка запроса профиля: ${ex.localizedMessage}", ex)
                Response.Error(TeacherError.GetTeacherProfileError)
            }
        )
    }

}
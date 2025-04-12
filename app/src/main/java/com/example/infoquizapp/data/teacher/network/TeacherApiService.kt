package com.example.infoquizapp.data.teacher.network

import android.util.Log
import com.example.infoquizapp.data.quiz.model.QuizOut
import com.example.infoquizapp.data.teacher.model.TeacherCreateQuiz
import com.example.infoquizapp.data.teacher.model.TeacherProfile
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

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
    data object PostTeacherQuizError: TeacherError("Ошибка создания квиза")
    data object GetTeacherQuizzesError: TeacherError("Ошибка получения квизов")
    data object DeleteTeacherQuizError: TeacherError("Ошибка удаления квиза")
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

    suspend fun postQuiz(token: String, quiz: TeacherCreateQuiz): Response<QuizOut> {
        return runCatching {
            Response.Succes(client.post("$baseUrl/teacher/postquizzes") {
                header("Authorization", "Bearer $token")
                contentType(ContentType.Application.Json)
                setBody(quiz)
            }.body<QuizOut>())
        }.fold(
            onSuccess = { it },
            onFailure = { ex ->
                Log.e("TeacherApiService", "Ошибка создания квиза: ${ex.localizedMessage}", ex)
                Response.Error(TeacherError.PostTeacherQuizError)
            }
        )
    }

    suspend fun getTeacherQuizzes(token: String): Response<List<QuizOut>> {
        return runCatching {
            Response.Succes(
                client.get("$baseUrl/teacher/getquizzes") {
                    header("Authorization", "Bearer $token")
                }.body<List<QuizOut>>()
            )
        }.fold(
            onSuccess = { it },
            onFailure = { ex ->
                Log.e("TeacherApiService", "Ошибка запроса квизов: ${ex.localizedMessage}", ex)
                Response.Error(TeacherError.GetTeacherQuizzesError)
            }
        )
    }

    suspend fun deleteTeacherQuiz(token: String, quizId: Int): Response<QuizOut> {
        return runCatching {
            Response.Succes(
                client.delete("$baseUrl/teacher/deletequizzes/$quizId") {
                    header("Authorization", "Bearer $token")
                }.body<QuizOut>()
            )
        }.fold(
            onSuccess = { it },
            onFailure = { ex ->
                Log.e("TeacherApiService", "Ошибка удаления квиза: ${ex.localizedMessage}", ex)
                Response.Error(TeacherError.DeleteTeacherQuizError)
            }
        )
    }

}
package com.example.infoquizapp.data.gamequiz.network

import android.util.Log
import com.example.infoquizapp.data.gamequiz.model.CompleteGameQuizResponse
import com.example.infoquizapp.data.gamequiz.model.GameQuizOut
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

sealed class Response<T>{
    data class Success<T>(
        val result: T
    ): Response<T>()

    data class Error<T>(
        val error: GameQuizError
    ) : Response<T>()
}

sealed class GameQuizError (val message: String?) {
    data object GetGameQuizError: GameQuizError("Ошибка получения профиля")
    data object PostGameQuizError: GameQuizError("Ошибка завершения профиля")
}

class ApiGameQuizService(private val client: HttpClient, private val baseUrl: String) {

    suspend fun getGameQuiz(token: String): Response<GameQuizOut> {
        return kotlin.runCatching {
            Response.Success(client.get("$baseUrl/game_quizzes/quiz") {
                header("Authorization", "Bearer $token")
            }.body<GameQuizOut>())
        }.fold(
            onSuccess = { it },
            onFailure = { ex ->
                Log.e("ApiGameQuizService", "Ошибка запроса вопроса: ${ex.localizedMessage}", ex)
                Response.Error(GameQuizError.GetGameQuizError)
            }
        )
    }

    suspend fun completeGameQuiz(experience: Int, token: String): Response<CompleteGameQuizResponse> {
        return kotlin.runCatching {
            Response.Success(client.post("$baseUrl/game_quizzes/experience?experience=$experience") {
                header("Authorization", "Bearer $token")
            }.body<CompleteGameQuizResponse>())
        }.fold(
            onSuccess = { it },
            onFailure = { ex ->
                Log.e("ApiGameQuizService", "Ошибка завершения вопроса: ${ex.localizedMessage}", ex)
                Response.Error(GameQuizError.PostGameQuizError)
            }
        )
    }
}
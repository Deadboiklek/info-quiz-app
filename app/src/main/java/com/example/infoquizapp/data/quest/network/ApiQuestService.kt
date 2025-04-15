package com.example.infoquizapp.data.quest.network

import android.util.Log
import com.example.infoquizapp.data.quest.model.CompleteQuestResponse
import com.example.infoquizapp.data.quest.model.QuestOut
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post

// Определяем универсальный sealed-класс для обработки ответов от API для квестов
sealed class Response<T> {
    data class Success<T>(val result: T) : Response<T>()
    data class Error<T>(val error: QuestError) : Response<T>()
}

// Объекты ошибок для работы с квестами
sealed class QuestError(val message: String?) {
    object GetQuestsError : QuestError("Ошибка получения квестов")
    object GetUserQuestsError : QuestError("Ошибка получения квестов пользователя")
    object PostQuestError : QuestError("Ошибка завершения квеста")
}

// Сервис для работы с квестами
class ApiQuestsService(
    private val client: HttpClient,
    private val baseUrl: String
) {
    // Получение всего списка квестов (например, GET /quests/)
    suspend fun getQuests(): Response<List<QuestOut>> {
        return kotlin.runCatching {
            Response.Success(
                client.get("$baseUrl/quests/") {
                    // Здесь можно добавить header или другие настройки запроса, если требуется
                }.body<List<QuestOut>>()
            )
        }.fold(
            onSuccess = { it },
            onFailure = { ex ->
                Log.e("ApiQuestsService", "Ошибка запроса квестов: ${ex.localizedMessage}", ex)
                Response.Error(QuestError.GetQuestsError)
            }
        )
    }

    // Получение квестов текущего пользователя (GET /quests/user)
    suspend fun getUserQuests(token: String): Response<List<QuestOut>> {
        return kotlin.runCatching {
            Response.Success(
                client.get("$baseUrl/quests/user") {
                    header("Authorization", "Bearer $token")
                }.body<List<QuestOut>>()
            )
        }.fold(
            onSuccess = { it },
            onFailure = { ex ->
                Log.e("ApiQuestsService", "Ошибка запроса квестов пользователя: ${ex.localizedMessage}", ex)
                Response.Error(QuestError.GetUserQuestsError)
            }
        )
    }

    // Завершение квеста (POST /quests/complete/{questId})
    suspend fun completeQuest(questId: Int, token: String): Response<CompleteQuestResponse> {
        return kotlin.runCatching {
            Response.Success(
                client.post("$baseUrl/quests/complete/$questId") {
                    header("Authorization", "Bearer $token")
                }.body<CompleteQuestResponse>()
            )
        }.fold(
            onSuccess = { it },
            onFailure = { ex ->
                Log.e("ApiQuestsService", "Ошибка завершения квеста: ${ex.localizedMessage}", ex)
                Response.Error(QuestError.PostQuestError)
            }
        )
    }
}
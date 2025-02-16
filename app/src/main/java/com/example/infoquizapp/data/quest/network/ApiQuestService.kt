package com.example.infoquizapp.data.quest.network

import com.example.infoquizapp.data.quest.model.CompleteQuestResponse
import com.example.infoquizapp.data.quest.model.QuestOut
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post

sealed class Response<T> {
    data class Success<T>(val result: T) : Response<T>()
    data class Error<T>(val error: QuestError) : Response<T>()
}

sealed class QuestError(val message: String?) {
    object GetQuestError : QuestError("Ошибка получения квеста")
    object PostQuestError : QuestError("Ошибка завершения квеста")
}

class ApiQuestService(
    val client: HttpClient,
    val baseUrl: String
) {
    // Получение квестов пользователя (GET /quests/user)
    suspend fun getUserQuests(token: String): Response<List<QuestOut>> {
        return kotlin.runCatching {
            Response.Success(client.get("$baseUrl/quests/user") {
                header("Authorization", "Bearer $token")
            }.body<List<QuestOut>>())
        }.getOrElse { Response.Error(QuestError.GetQuestError) }
    }

    // Завершение квеста (POST /quests/complete/{questId})
    suspend fun completeQuest(questId: Int, token: String): Response<CompleteQuestResponse> {
        return kotlin.runCatching {
            Response.Success(client.post("$baseUrl/quests/complete/$questId") {
                header("Authorization", "Bearer $token")
            }.body<CompleteQuestResponse>())
        }.getOrElse { Response.Error(QuestError.PostQuestError) }
    }
}
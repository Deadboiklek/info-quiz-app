package com.example.infoquizapp.data.quiz.network

import com.example.infoquizapp.data.quiz.model.AnswerIn
import com.example.infoquizapp.data.quiz.model.AnswerOut
import com.example.infoquizapp.data.quiz.model.QuizOut
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

sealed class Response<T> {
    data class Success<T>(val result: T) : Response<T>()
    data class Error<T>(val error: QuizError) : Response<T>()
}

sealed class QuizError(val message: String?) {
    object GetQuizError : QuizError("Ошибка получения квиза")
    object PostQuizError : QuizError("Ошибка завершения квиза")
}

class QuizApiService(
    private val client: HttpClient,
    private val baseUrl: String
) {

    suspend fun getTestQuizzes(quizType: String, token: String): Response<List<QuizOut>> {
        return kotlin.runCatching {
            Response.Success(client.get("$baseUrl/quizzes/test/$quizType") {
                header("Authorization", "Bearer $token")
                contentType(ContentType.Application.Json)
            }.body<List<QuizOut>>())
        }.getOrElse { Response.Error(QuizError.GetQuizError) }
    }

    suspend fun submitAnswer(answer: AnswerIn, token: String): Response<AnswerOut> {

        return kotlin.runCatching {
            Response.Success(client.post("$baseUrl/answer") {
                header("Authorization", "Bearer $token")
                contentType(ContentType.Application.Json)
                setBody(answer)
            }.body<AnswerOut>())
        }.getOrElse { Response.Error(QuizError.PostQuizError) }
    }
}
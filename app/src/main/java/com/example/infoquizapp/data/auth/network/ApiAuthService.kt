package com.example.infoquizapp.data.auth.network


import com.example.infoquizapp.data.auth.model.TokenResponse
import com.example.infoquizapp.data.auth.model.UserCreate
import com.example.infoquizapp.data.auth.model.UserLogin
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.http.*

sealed class Response<T>{
    data class Succes<T>(
        val result: T
    ): Response<T>()

    data class Error<T>(
        val error: ServerError
    ) : Response<T>()
}

sealed class ServerError (val message: String?) {
    data object ErrorRegister: ServerError("Ошибка регистрации")
    data object ErrorLogin: ServerError("Ошибка авторизации")
}



class ApiAuthService(private val client: HttpClient, private val baseUrl: String) {

    suspend fun register(userCreate: UserCreate): Response<TokenResponse> {
        return kotlin.runCatching {
            Response.Succes(client.post("$baseUrl/auth/register") {
                contentType(ContentType.Application.Json)
                setBody(userCreate)
            }.body<TokenResponse>())
        }.getOrDefault(Response.Error(ServerError.ErrorRegister))

    }

    suspend fun login(userLogin: UserLogin): Response<TokenResponse> {
        return kotlin.runCatching {
            Response.Succes(client.post("$baseUrl/auth/login") {
                contentType(ContentType.Application.Json)
                setBody(userLogin)
            }.body<TokenResponse>())
        }.getOrDefault(Response.Error(ServerError.ErrorLogin))
    }
}
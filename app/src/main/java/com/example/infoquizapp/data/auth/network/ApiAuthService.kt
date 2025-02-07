package com.example.infoquizapp.data.auth.network


import com.example.infoquizapp.data.auth.model.TokenResponse
import com.example.infoquizapp.data.auth.model.UserCreate
import com.example.infoquizapp.data.auth.model.UserLogin
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.http.*

class ApiAuthService(private val client: HttpClient, private val baseUrl: String) {

    suspend fun register(userCreate: UserCreate): TokenResponse {
        return client.post("$baseUrl/auth/register") {
            contentType(ContentType.Application.Json)
            setBody(userCreate)
        }.body()
    }

    suspend fun login(userLogin: UserLogin): TokenResponse {
        return client.post("$baseUrl/auth/login") {
            contentType(ContentType.Application.Json)
            setBody(userLogin)
        }.body()
    }
}
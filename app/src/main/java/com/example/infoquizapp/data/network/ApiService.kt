package com.example.infoquizapp.data.network

import com.example.infoquizapp.data.model.ApiResponse
import com.example.infoquizapp.data.model.AuthResponse
import com.example.infoquizapp.data.model.LoginRequest
import com.example.infoquizapp.data.model.RegisterRequest
import com.example.infoquizapp.data.model.ResetPasswordRequest
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

class ApiService(private val client: HttpClient) {

    suspend fun register(request: RegisterRequest): ApiResponse {
        val response: HttpResponse = client.post("/api/register") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        return response.body()
    }

    suspend fun login(request: LoginRequest): AuthResponse {
        val response: HttpResponse = client.post("/api/login") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        return response.body()
    }

    suspend fun resetPassword(request: ResetPasswordRequest): ApiResponse {
        val response: HttpResponse = client.post("/api/reset-password") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        return response.body()
    }
}
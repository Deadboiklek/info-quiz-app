package com.example.infoquizapp.data.teacher.network

import android.util.Log
import com.example.infoquizapp.data.quiz.model.QuizOut
import com.example.infoquizapp.data.teacher.model.StudentInfo
import com.example.infoquizapp.data.teacher.model.StudentStatistics
import com.example.infoquizapp.data.teacher.model.TeacherCreateQuiz
import com.example.infoquizapp.data.teacher.model.TeacherProfile
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
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
    data object GetTeacherStudentsError: TeacherError("Ошибка получения студентов")
    data object GetStudentsStatisticsError: TeacherError("Ошибка получения статистики студентов")
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

    suspend fun postQuiz(
        token: String,
        question: String,
        correctAnswer: String,
        experienceReward: Int,
        type: String,
        imageBytes: ByteArray?
    ): Response<QuizOut> = runCatching {
        // multipart запрос
        val httpResponse = client.submitFormWithBinaryData(
            url = "$baseUrl/teacher/postquizzes",
            formData = formData {
                append("question", question)
                append("correct_answer", correctAnswer)
                append("experience_reward", experienceReward.toString())
                append("type", type)
                imageBytes?.let { bytes ->
                    append(
                        key = "file",
                        value = bytes,
                        headers = Headers.build {
                            append(HttpHeaders.ContentDisposition, """filename="quiz_image.png"""")
                            append(HttpHeaders.ContentType, ContentType.Image.PNG.toString())
                        }
                    )
                }
            }
        ) {
            header("Authorization", "Bearer $token")
        }

        Response.Succes(httpResponse.body<QuizOut>())
    }.fold(
        onSuccess = { it },
        onFailure = {
            Log.e("TeacherApiService", "Ошибка создания квиза: ${it.localizedMessage}", it)
            Response.Error(TeacherError.PostTeacherQuizError)
        }
    )

    suspend fun updateQuiz(
        token: String,
        quizId: Int,
        question: String,
        correctAnswer: String,
        experienceReward: Int,
        type: String,
        imageBytes: ByteArray?
    ): Response<QuizOut> = runCatching {
        val response = client.submitFormWithBinaryData(
            url = "$baseUrl/teacher/quizzes/$quizId",
            formData = formData {
                append("question", question)
                append("correct_answer", correctAnswer)
                append("experience_reward", experienceReward.toString())
                append("type", type)
                imageBytes?.let { bytes ->
                    append(
                        key = "file",
                        value = bytes,
                        headers = Headers.build {
                            append(HttpHeaders.ContentDisposition, """filename="quiz.png"""")
                            append(HttpHeaders.ContentType, ContentType.Image.PNG.toString())
                        }
                    )
                }
            }
        ) {
            header("Authorization", "Bearer $token")
            method = HttpMethod.Put
        }

        Response.Succes(response.body<QuizOut>())
    }.getOrElse {
        Response.Error(TeacherError.PostTeacherQuizError)
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

    suspend fun getTeacherStudents(token: String): Response<List<StudentInfo>> {
        return runCatching {
            Response.Succes(
                client.get("$baseUrl/teacher/students") {
                    header("Authorization", "Bearer $token")
                }.body<List<StudentInfo>>()
            )
        }.fold(
            onSuccess = { it },
            onFailure = { ex ->
                Log.e("TeacherApiService", "Ошибка получения списка учеников: ${ex.localizedMessage}", ex)
                Response.Error(TeacherError.GetTeacherStudentsError)
            }
        )
    }

    suspend fun getStudentStatistics(token: String, studentId: Int): Response<StudentStatistics> {
        return runCatching {
            Response.Succes(
                client.get("$baseUrl/teacher/studentstats/$studentId") {
                    header("Authorization", "Bearer $token")
                }.body<StudentStatistics>()
            )
        }.fold(
            onSuccess = { it },
            onFailure = { ex ->
                Log.e("TeacherApiService", "Ошибка получения статистики ученика: ${ex.localizedMessage}", ex)
                Response.Error(TeacherError.GetStudentsStatisticsError)
            }
        )
    }

}
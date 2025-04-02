package com.example.infoquizapp.data.gamequiz.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CompleteGameQuizResponse(
    val message: String,
    @SerialName("new_experience") val newExperience: Int,
)
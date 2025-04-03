package com.example.infoquizapp.presentation.game.model

import androidx.compose.ui.geometry.Offset

data class Asteroid(
    val position: Offset,
    val size: Float = 40f,
    val speed: Float = 5f
)
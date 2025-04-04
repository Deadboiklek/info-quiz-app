package com.example.infoquizapp.presentation.game.model

import androidx.compose.ui.geometry.Offset

data class Monster(
    val position: Offset,
    val width: Float,
    val height: Float = 60f,
    val speed: Float = 3f
)
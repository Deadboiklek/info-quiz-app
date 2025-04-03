package com.example.infoquizapp.presentation.game.model

import androidx.compose.ui.geometry.Offset

data class Monster(
    val position: Offset,
    val size: Float = 60f,
    val speed: Float = 3f
)
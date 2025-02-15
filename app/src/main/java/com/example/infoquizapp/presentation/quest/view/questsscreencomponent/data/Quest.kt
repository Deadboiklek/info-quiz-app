package com.example.infoquizapp.presentation.quest.view.questsscreencomponent.data

import androidx.compose.ui.graphics.painter.Painter

data class Quest(
    val id: Int,
    val title: String,
    val description: String,
    val image: Painter, // Изображение задания
    val counter: Int? = null // счетчик
)

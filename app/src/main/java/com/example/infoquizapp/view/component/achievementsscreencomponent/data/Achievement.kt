package com.example.infoquizapp.view.component.achievementsscreencomponent.data

import androidx.compose.ui.graphics.vector.ImageVector

data class Achievement(
    val id: Int,
    val title: String,
    val description: String,
    val icon: ImageVector,
    val rarity: String,
    val completed: Boolean
)

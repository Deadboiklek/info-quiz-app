package com.example.infoquizapp.presentation.quiz.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.example.infoquizapp.R

@Composable
fun PlanetIcon(isActive: Boolean, modifier: Modifier = Modifier) {
    val imageRes = if (isActive) {
        R.drawable.planet_green // ресурс для активной планеты
    } else {
        R.drawable.planet_red // ресурс для неактивной планеты
    }
    Image(
        painter = painterResource(id = imageRes),
        contentDescription = "Планета",
        modifier = modifier
    )
}
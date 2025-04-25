package com.example.infoquizapp.presentation.quiz.view

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.infoquizapp.R
import com.example.infoquizapp.data.quiz.model.QuizOut

@Composable
fun PlanetScreen(
    quizzes: List<QuizOut>,
    currentPlanetIndex: Int
) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
    ) {
        val boxWidth = constraints.maxWidth.toFloat()
        val density = LocalDensity.current

        // Размещаем планеты равномерно по горизонтали
        quizzes.forEachIndexed { index, _ ->
            val xFraction = (index + 1) / (quizzes.size + 1).toFloat()
            val xOffset = with(density) { (boxWidth * xFraction).toDp() }
            val planetY = 50.dp  // можно настроить вертикальное положение
            Box(
                modifier = Modifier
                    .offset(x = xOffset - 40.dp, y = planetY)
                    .size(70.dp)
                    .clickable { /* Обработка нажатия, если требуется */ },
                contentAlignment = Alignment.Center
            ) {
                // Здесь отображается сама планета без отладочных элементов
                PlanetIcon(isActive = index <= currentPlanetIndex)
            }
        }

        // Анимированное перемещение космонавта к текущей планете
        val targetFraction = (currentPlanetIndex + 1) / (quizzes.size + 1).toFloat()
        val targetXOffset = with(density) { (boxWidth * targetFraction).toDp() }
        val animatedXOffset by animateDpAsState(
            targetValue = targetXOffset - 32.dp,
            animationSpec = tween(durationMillis = 500)
        )
        Image(
            painter = painterResource(id = R.drawable.quiz_astronaut), // убедитесь, что ресурс существует
            contentDescription = "Космонавт",
            modifier = Modifier
                .size(64.dp)
                .offset(x = animatedXOffset, y = 0.dp)
        )
    }
}
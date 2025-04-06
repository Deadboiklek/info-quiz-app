package com.example.infoquizapp.presentation.main.view.mainscreencomponent

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.infoquizapp.data.profile.model.UserOut
import kotlin.math.pow

@Composable
fun UserProgressBar(user: UserOut) {
    // Параметры системы уровней
    val baseExp = 100.0
    val multiplier = 1.2
    val currentLevel = user.level ?: 1
    val currentExp = user.experience ?: 0

    val requiredExp = baseExp * multiplier.pow(currentLevel.toDouble() - 1)
    val progress = (currentExp / requiredExp).toFloat().coerceIn(0f, 1f)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Уровень ${user.level}",
            style = TextStyle(fontSize = 16.sp),
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        LinearProgressIndicator(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp),
            color = MaterialTheme.colorScheme.primary,
            progress = progress
        )
    }
}
package com.example.infoquizapp.presentation.achievementnotifier

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.infoquizapp.data.achievement.model.AchievementOut
import com.example.infoquizapp.presentation.achievement.viewmodel.AchievementsViewModel
import kotlinx.coroutines.delay

@Composable
fun GlobalAchievementNotifier(
    viewModel: AchievementsViewModel,
    modifier: Modifier = Modifier
) {
    // Состояние для текущего уведомления (если не null, показываем его)
    var currentAchievement by remember { mutableStateOf<AchievementOut?>(null) }

    // Подписываемся на события получения новых достижений
    LaunchedEffect(viewModel.achievementEarnedEvent) {
        viewModel.achievementEarnedEvent.collect { achievement ->
            currentAchievement = achievement
            // Отображаем уведомление в течение 3 секунд
            delay(3000)
            currentAchievement = null
        }
    }

    // Если есть активное уведомление, выводим его поверх содержимого
    currentAchievement?.let { achievement ->
        // Пример: уведомление сверху по центру экрана
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(top = 16.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            AchievementBanner(
                achievement = achievement,
                onDismiss = { currentAchievement = null }
            )
        }
    }
}
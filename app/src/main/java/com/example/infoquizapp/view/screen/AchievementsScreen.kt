package com.example.infoquizapp.view.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.infoquizapp.view.component.achievementsscreencomponent.AchievementCard
import com.example.infoquizapp.view.component.achievementsscreencomponent.data.Achievement

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AchievementsScreen(
    achievements: List<Achievement>,
    counts: List<Int>, // cчетчики для значков
    onClose: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Достижения") },
                actions = {
                    TextButton(onClick = onClose) {
                        Text(text = "Закрыть", color = MaterialTheme.colorScheme.primary)
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            // Секция значков
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                counts.forEachIndexed { index, count ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = when (index) {
                                0 -> Icons.Default.Star // Platinum
                                1 -> Icons.Default.EmojiEvents // Gold
                                2 -> Icons.Default.StarBorder // Silver
                                else -> Icons.Default.MilitaryTech // Bronze
                            },
                            contentDescription = null,
                            tint = when (index) {
                                0 -> Color(0xFF0099FF) // Blue
                                1 -> Color(0xFFFFD700) // Gold
                                2 -> Color(0xFFC0C0C0) // Silver
                                else -> Color(0xFFCD7F32) // Bronze
                            },
                            modifier = Modifier.size(48.dp)
                        )
                        Text(
                            text = count.toString(),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Список достижений
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(achievements) { achievement ->
                    AchievementCard(achievement = achievement)
                }
            }
        }
    }
}
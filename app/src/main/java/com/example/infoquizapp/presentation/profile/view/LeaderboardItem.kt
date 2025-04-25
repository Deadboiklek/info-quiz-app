package com.example.infoquizapp.presentation.profile.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.infoquizapp.data.profile.model.LeaderboardEntry

@Composable
fun LeaderboardItem(
    position: Int,
    entry: LeaderboardEntry
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (position in 1..3) {
                val color = when (position) {
                    1 -> Color(0xFFFFD700) // золотой
                    2 -> Color(0xFFC0C0C0) // серебряный
                    3 -> Color(0xFFCD7F32) // бронзовый
                    else -> Color.Unspecified
                }
                Icon(
                    imageVector = Icons.Default.EmojiEvents,
                    contentDescription = "Трофей #$position",
                    tint = color,
                    modifier = Modifier.size(32.dp)
                )
            } else {
                Text(
                    text = "#$position",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.width(32.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = entry.username,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "Уровень: ${entry.level}  Опыт: ${entry.experience}",
                    fontSize = 14.sp
                )
            }
        }
    }
}

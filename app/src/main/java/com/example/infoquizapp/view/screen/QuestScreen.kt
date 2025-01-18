package com.example.infoquizapp.view.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.infoquizapp.view.component.questsscreencomponent.QuestionCard
import com.example.infoquizapp.view.component.questsscreencomponent.data.Quest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksScreen(
    quests: List<Quest>,
    onClose: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Задания") },
                actions = {
                    TextButton(onClick = onClose) {
                        Text(text = "Закрыть", color = MaterialTheme.colorScheme.primary)
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(quests) { quest ->
                QuestionCard(quest = quest)
            }
        }
    }
}
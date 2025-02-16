package com.example.infoquizapp.presentation.quest.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.infoquizapp.presentation.quest.view.questsscreencomponent.QuestionCard
import com.example.infoquizapp.presentation.quest.view.questsscreencomponent.data.Quest
import com.example.infoquizapp.presentation.quest.viewmodel.UserQuestsUiState
import com.example.infoquizapp.presentation.quest.viewmodel.UserQuestsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksScreen(
    viewModel: UserQuestsViewModel,
    token: String
) {
    LaunchedEffect(token) {
        viewModel.loadUserQuests(token)
    }

    val uiState by viewModel.userQuestsState.collectAsState()

    when (uiState) {
        is UserQuestsUiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is UserQuestsUiState.Success -> {
            val quests = (uiState as UserQuestsUiState.Success).quests

            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text(text = "Задания") },
                        actions = {
                            TextButton(onClick = TODO()) {
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
                        QuestionCard(quest = quest, token = token, viewModel = viewModel)
                    }
                }
            }


        }

        is UserQuestsUiState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = (uiState as UserQuestsUiState.Error).message,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        UserQuestsUiState.Idle -> Unit
    }
}

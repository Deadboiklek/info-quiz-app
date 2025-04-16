package com.example.infoquizapp.presentation.quest.view

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.infoquizapp.presentation.quest.view.questsscreencomponent.QuestCard
import com.example.infoquizapp.presentation.quest.viewmodel.QuestsViewModel
import com.example.infoquizapp.utils.TokenManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestScreen(
    viewModel: QuestsViewModel,
    onExit: () -> Unit
) {
    val context = LocalContext.current
    val token = TokenManager.getToken(context) ?: ""

    // Загружаем данные при изменении токена
    LaunchedEffect(token) {
        viewModel.loadUserQuests(token)
        viewModel.loadAllQuests()
    }

    val combinedState by viewModel.combinedQuestsState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Задания") },
                actions = {
                    TextButton(onClick = onExit) {
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
            items(combinedState.questsUiModels) { questUiModel ->
                QuestCard(questUiModel = questUiModel, token = token, viewModel = viewModel)
            }
        }
    }
}

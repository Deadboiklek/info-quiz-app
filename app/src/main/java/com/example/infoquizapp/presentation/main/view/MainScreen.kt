package com.example.infoquizapp.presentation.main.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.infoquizapp.presentation.main.view.mainscreencomponent.AchievementsCard
import com.example.infoquizapp.presentation.main.view.mainscreencomponent.AppBar
import com.example.infoquizapp.presentation.main.view.mainscreencomponent.QuestCard
import com.example.infoquizapp.presentation.main.view.mainscreencomponent.TabBar
import com.example.infoquizapp.presentation.main.view.mainscreencomponent.UserProgressBar
import com.example.infoquizapp.presentation.main.viewmodel.MainUiState
import com.example.infoquizapp.presentation.main.viewmodel.MainViewModel

@Composable
fun MainScreen(
    viewModel: MainViewModel,
    token: String,
    progress: Float = 0.7f,  //тут ебучая хуйня блять пиздос, чтобы её сделать надо пол сервака менять нахуй........
    onProfileClick : () -> Unit,
    onAchievementClick : () -> Unit,
    onQuestClick : () -> Unit
) {

    LaunchedEffect(token) {
        viewModel.loadProfile(token)
    }


    var selectedTab by remember { mutableStateOf(0) } // Для TabBar
    val uiState by viewModel.uiState.collectAsState()

    when(uiState) {
        is MainUiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        is MainUiState.Success -> {

            val user = (uiState as MainUiState.Success).user

            Scaffold(
                topBar = {
                    AppBar(user = user, onProfileClick = onProfileClick)
                },
                bottomBar = {
                    TabBar(
                        selectedTab = selectedTab,
                        onTabSelected = { selectedTab = it }
                    )
                }
            ) { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    // Прогресс пользователя
                    UserProgressBar(user = user, progressProvider =  { progress })

                    Spacer(modifier = Modifier.height(16.dp))

                    // Карточка заданий
                    QuestCard(onQuestClick = onQuestClick)

                    Spacer(modifier = Modifier.height(16.dp))

                    // Карточка достижений
                    AchievementsCard(onAchievementClick = onAchievementClick)
                }
            }
        }
        is MainUiState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = (uiState as MainUiState.Error).message,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
        MainUiState.Idle -> {}
    }
}
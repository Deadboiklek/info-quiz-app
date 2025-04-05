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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.infoquizapp.Routes
import com.example.infoquizapp.presentation.main.view.mainscreencomponent.AchievementsCard
import com.example.infoquizapp.presentation.main.view.mainscreencomponent.AppBar
import com.example.infoquizapp.presentation.main.view.mainscreencomponent.QuestCard
import com.example.infoquizapp.presentation.main.view.mainscreencomponent.TabBarComp
import com.example.infoquizapp.presentation.main.view.mainscreencomponent.UserProgressBar
import com.example.infoquizapp.presentation.main.viewmodel.MainUiState
import com.example.infoquizapp.presentation.main.viewmodel.MainViewModel
import com.example.infoquizapp.utils.TokenManager

@Composable
fun MainScreen(
    viewModel: MainViewModel,
    progress: Float = 0.7f,
    onProfileClick : (token: String) -> Unit,
    onAchievementClick : (token: String) -> Unit,
    navController: NavController
) {

    val context = LocalContext.current
    val token = TokenManager.getToken(context) ?: ""

    LaunchedEffect(token) {
        viewModel.loadProfile(token)
    }


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
                    AppBar(user = user, onProfileClick = onProfileClick, token = token)
                },
                bottomBar = {
                    TabBarComp(
                        navController = navController
                    )
                },
                contentColor = MaterialTheme.colorScheme.background
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
                    QuestCard(onQuestClick = { navController.navigate(Routes.Quest.createRoute(token)) },
                        token = token)

                    Spacer(modifier = Modifier.height(16.dp))

                    // Карточка достижений
                    AchievementsCard(onAchievementClick = onAchievementClick, token = token)
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
package com.example.infoquizapp.presentation.profile.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.infoquizapp.Routes
import com.example.infoquizapp.presentation.auth.viewmodel.AuthViewModel
import com.example.infoquizapp.presentation.profile.view.profilescreencomponent.UserProfileSection
import com.example.infoquizapp.presentation.profile.viewmodel.ProfileUiState
import com.example.infoquizapp.presentation.profile.viewmodel.ProfileViewModel
import com.example.infoquizapp.presentation.profile.viewmodel.StatisticsUiState
import com.example.infoquizapp.presentation.profile.viewmodel.StatisticsViewModel
import com.example.infoquizapp.utils.TokenManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    profileViewModel: ProfileViewModel,
    statisticsViewModel: StatisticsViewModel,
    authViewModel: AuthViewModel,
    onExit: () -> Unit,
    navController: NavController
) {
    val context = LocalContext.current
    val token = TokenManager.getToken(context) ?: ""

    // Загружаем профиль и статистику пользователя
    LaunchedEffect(token) {
        profileViewModel.loadProfile(token)
        statisticsViewModel.getStatistics(token)
    }

    val profileUiState by profileViewModel.uiState.collectAsState()
    val statsUiState by statisticsViewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Профиль") },
                navigationIcon = {
                    IconButton(onClick = { onExit() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                },
                actions = {
                    TextButton(onClick = {
                        authViewModel.logout()
                        TokenManager.clearToken(context)
                        navController.navigate(Routes.Login.route) {
                            popUpTo(Routes.Main.route) { inclusive = true }
                        }
                    }) {
                        Text("Выйти из аккаунта", color = MaterialTheme.colorScheme.error)
                    }
                },
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            // Обработка состояния профиля
            when (profileUiState) {
                is ProfileUiState.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is ProfileUiState.Success -> {
                    val user = (profileUiState as ProfileUiState.Success).user
                    UserProfileSection(user.username)

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            profileViewModel.resetEditState()
                            navController.navigate(Routes.ProfileEdit.route)
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Измненить профиль")
                    }
                }
                is ProfileUiState.Error -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = (profileUiState as ProfileUiState.Error).message,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
                ProfileUiState.Idle -> {}
            }

            // Добавляем секцию статистики
            when (statsUiState) {
                is StatisticsUiState.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is StatisticsUiState.Success -> {
                    val stats = (statsUiState as StatisticsUiState.Success).user
                    UserStatisticsSection(stats)
                }
                is StatisticsUiState.Error -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = (statsUiState as StatisticsUiState.Error).message,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
                StatisticsUiState.Idle -> { /* ничего не показываем */ }
            }
        }
    }
}
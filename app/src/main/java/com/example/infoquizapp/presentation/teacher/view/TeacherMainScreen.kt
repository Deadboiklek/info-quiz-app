package com.example.infoquizapp.presentation.teacher.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.infoquizapp.Routes
import com.example.infoquizapp.presentation.auth.viewmodel.AuthViewModel
import com.example.infoquizapp.presentation.teacher.viewmodel.TeacherProfileUiState
import com.example.infoquizapp.presentation.teacher.viewmodel.TeacherProfileViewModel
import com.example.infoquizapp.utils.TokenManager

@Composable
fun TeacherMainScreen(
    viewModel: TeacherProfileViewModel,
    authViewModel: AuthViewModel,
    navController: NavController
) {
    val context = LocalContext.current
    val token = TokenManager.getToken(context) ?: ""

    LaunchedEffect(token) {
        viewModel.load(token)
    }

    val uiState by viewModel.uiState.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    when (uiState) {
        is TeacherProfileUiState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = (uiState as TeacherProfileUiState.Error).message,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        TeacherProfileUiState.Idle -> {}
        TeacherProfileUiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is TeacherProfileUiState.Success -> {
            val teacher = (uiState as TeacherProfileUiState.Success).teacher

            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    confirmButton = {
                        TextButton(onClick = { showDialog = false }) {
                            Text("Закрыть")
                        }
                    },
                    title = { Text("Код учителя") },
                    text = { Text("Ваш код: ${teacher.teacherCode}") }
                )
            }

            Scaffold(
                contentColor = MaterialTheme.colorScheme.background
            ) { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Здравствуйте, ${teacher.firstname} ${teacher.patronymic}",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    // Сбрасываем editState перед навигацией
                    Button(
                        onClick = {
                            viewModel.resetEdit()
                            navController.navigate(Routes.TeacherProfileEdit.route)
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Изменить профиль")
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { navController.navigate(Routes.AddQuizScreen.route) },
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        Box(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Добавить задание",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { navController.navigate(Routes.GetAndDeleteQuizScreen.route) },
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                    ) {
                        Box(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Просмотреть и удалить задания",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { navController.navigate(Routes.StudentsListScreen.route) },
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE0FEBD))
                    ) {
                        Box(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Просмотреть статистику",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showDialog = true },
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFEBDE0))
                    ) {
                        Box(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Посмотреть код учителя",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }

                    TextButton(onClick = {
                        authViewModel.logout()
                        TokenManager.clearToken(context)
                        navController.navigate(Routes.Login.route) {
                            popUpTo(Routes.Main.route) { inclusive = true }
                        }
                    }
                    ) {
                        Text("Выйти из аккаунта", color = MaterialTheme.colorScheme.error)
                    }
                }
            }
        }
    }
}

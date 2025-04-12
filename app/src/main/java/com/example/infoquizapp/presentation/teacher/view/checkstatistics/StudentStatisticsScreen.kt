package com.example.infoquizapp.presentation.teacher.view.checkstatistics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import com.example.infoquizapp.presentation.teacher.viewmodel.StudentStatisticsUiState
import com.example.infoquizapp.presentation.teacher.viewmodel.StudentStatisticsViewModel
import com.example.infoquizapp.utils.TokenManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentStatisticsScreen(
    studentId: Int,
    navController: NavController,
    viewModel: StudentStatisticsViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val token = TokenManager.getToken(context) ?: ""

    LaunchedEffect(studentId) {
        viewModel.loadStudentStatistics(token, studentId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Статистика ученика") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when(uiState) {
                is StudentStatisticsUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is StudentStatisticsUiState.Error -> {
                    Text(
                        text = (uiState as StudentStatisticsUiState.Error).message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is StudentStatisticsUiState.Success -> {
                    val stat = (uiState as StudentStatisticsUiState.Success).statistics
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(text = "Имя: ${stat.username}", style = MaterialTheme.typography.titleMedium)
                        Text(text = "Email: ${stat.email}", style = MaterialTheme.typography.bodyMedium)
                        Text(text = "Выполнено заданий: ${stat.totalQuestsCompleted}", style = MaterialTheme.typography.bodyMedium)
                        Text(text = "Правильных ответов: ${stat.totalQuizCorrect}", style = MaterialTheme.typography.bodyMedium)
                        Text(text = "Опыт: ${stat.totalExperience}", style = MaterialTheme.typography.bodyMedium)
                        Text(text = "Уровень: ${stat.level}", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}
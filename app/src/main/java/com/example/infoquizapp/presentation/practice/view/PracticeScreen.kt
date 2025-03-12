package com.example.infoquizapp.presentation.practice.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.navigation.NavController
import com.example.infoquizapp.Routes
import com.example.infoquizapp.presentation.practice.viewmodel.AllPracticeUiState
import com.example.infoquizapp.presentation.practice.viewmodel.PracticeViewModel

@Composable
fun PracticeScreen(
    token: String,
    viewModel: PracticeViewModel,
    navController: NavController
) {

    val uiState by viewModel.combinedState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadAllPractices()
    }

    Scaffold { paddingValues ->
        when(uiState.allPracticeUiState) {
            AllPracticeUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is AllPracticeUiState.Success -> {
                val practices = (uiState.allPracticeUiState as AllPracticeUiState.Success).practices

                LazyColumn(
                    contentPadding = paddingValues,
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(practices) { practice ->
                        PracticeCard(
                            token = token,
                            practice = practice,
                            onPracticeCardClick = { quizType ->
                                navController.navigate(Routes.QuizTest.createRoute(quizType, token))
                            },
                        )
                    }
                }
            }
            is AllPracticeUiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = (uiState.allPracticeUiState as AllPracticeUiState.Error).message,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
            AllPracticeUiState.Idle -> { /* ничего не делаем */ }
        }
    }
}
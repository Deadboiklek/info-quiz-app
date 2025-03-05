package com.example.infoquizapp.presentation.trial.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.infoquizapp.presentation.main.view.mainscreencomponent.TabBar
import com.example.infoquizapp.presentation.quiz.view.QuizQuestionItem
import com.example.infoquizapp.presentation.trial.viewmodel.TrialUiState
import com.example.infoquizapp.presentation.trial.viewmodel.TrialViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrialTestScreen(viewModel: TrialViewModel, token: String) {

    //при старте загружаем пробник, состоит из заданий всех типов(1 тип = 1 задание)
    LaunchedEffect(token) {
        viewModel.loadTrial(token)
    }

    var selectedTab by remember { mutableIntStateOf(2) }
    val trialState by viewModel.trialState.collectAsState()
    // локальное состояние для выбранных ответов: Map(quiz id -> user answer)
    var userAnswers by remember { mutableStateOf(mutableMapOf<Int, String>()) }

    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Пробник",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(MaterialTheme.colorScheme.primary)
            )
        },
        bottomBar = {
            TabBar(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column (modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)) {
            when(trialState) {
                TrialUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is TrialUiState.Success -> {
                    val quizzes = (trialState as TrialUiState.Success).quizzes
                    LazyColumn(modifier = Modifier.weight(1f)) {
                        items(quizzes) { quiz ->
                            QuizQuestionItem(
                                quiz = quiz,
                                selectedAnswer = userAnswers[quiz.id] ?: "",
                                onAnswerSelected = { answer ->
                                    userAnswers[quiz.id] = answer
                                }
                            )
                            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                        }
                    }
                    Button(
                        onClick = {
                            userAnswers.forEach{ (quizId, answer) ->
                                viewModel.submitAnswer(quizId, answer, token) { result ->
                                    println("Quiz $quizId: ${if (result.response?.isCorrect == true) "Верно" 
                                    else "Неверно, правильный ответ: ${result.response?.correctAnswer}"}")
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Отправить тест")
                    }
                }
                is TrialUiState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = (trialState as TrialUiState.Error).message,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
                TrialUiState.Idle -> Unit
            }
        }
    }
}
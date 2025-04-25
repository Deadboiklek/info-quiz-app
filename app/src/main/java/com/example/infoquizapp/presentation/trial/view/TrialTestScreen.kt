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
import androidx.compose.material3.Divider
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
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.infoquizapp.presentation.trial.viewmodel.TrialUiState
import com.example.infoquizapp.presentation.trial.viewmodel.TrialViewModel
import com.example.infoquizapp.utils.TokenManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrialTestScreen(
    viewModel: TrialViewModel,
    onExit: () -> Unit,
    onShowResults: (correct: Int, total: Int) -> Unit
) {
    val context = LocalContext.current
    val token = TokenManager.getToken(context) ?: ""

    LaunchedEffect(token) {
        viewModel.resetTrial()
        viewModel.loadTrial(token)
    }

    val trialState by viewModel.trialState.collectAsState()
    val answerResults by viewModel.answerResults.collectAsState()

    // Флаг: ответы уже проверены
    val isChecked = answerResults.isNotEmpty()

    // Локально выбираемые ответы
    var userAnswers by remember { mutableStateOf(mutableStateMapOf<Int, String>()) }

    Scaffold(topBar = {
        TopAppBar(title = { Text("Пробник") }, actions = {
            TextButton(onClick = onExit) { Text("Закрыть") }
        })
    }) { paddingValues ->
        Column(Modifier.fillMaxSize().padding(paddingValues)) {
            when (trialState) {
                is TrialUiState.Loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
                is TrialUiState.Success -> {
                    val quizzes = (trialState as TrialUiState.Success).quizzes
                    LazyColumn(Modifier.weight(1f)) {
                        items(quizzes) { quiz ->
                            QuizQuestionItem(
                                quiz = quiz,
                                selectedAnswer = userAnswers[quiz.id] ?: "",
                                onAnswerSelected = { ans -> if (!isChecked) userAnswers[quiz.id] = ans },
                                feedback = answerResults[quiz.id]
                            )
                            Divider(Modifier.padding(vertical = 8.dp))
                        }
                    }
                    Button(
                        onClick = {
                            if (!isChecked) {
                                viewModel.submitTrial(userAnswers.toMap(), token)
                            } else {
                                // Подсчитанные правильные ответы
                                val correct = answerResults.count { it.value.isCorrect }
                                val total = quizzes.size
                                onShowResults(correct, total)
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(if (!isChecked) "Проверить ответы" else "Завершить пробник")
                    }
                }
                is TrialUiState.Error -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text((trialState as TrialUiState.Error).message, color = MaterialTheme.colorScheme.error)
                }
                TrialUiState.Idle -> Unit
            }
        }
    }
}
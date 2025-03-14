package com.example.infoquizapp.presentation.quiz.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.infoquizapp.presentation.quiz.viewmodel.QuizViewModel
import com.example.infoquizapp.presentation.quiz.viewmodel.TestQuizzesUiState
import com.example.infoquizapp.utils.TokenManager

@Composable
fun QuizTestScreen(viewModel: QuizViewModel, quizType: String) {

    val context = LocalContext.current
    val token = TokenManager.getToken(context) ?: ""

    // при старте экрана загружаем тест (10 случайных вопросов заданного типа)
    LaunchedEffect(quizType, token) {
        viewModel.loadTest(quizType, token)
    }

    // подписываемся на состояние теста
    val testState by viewModel.testQuizzesState.collectAsState()

    // локальное состояние для выбранных ответов: Map(quiz id -> user answer)
    var userAnswers by remember { mutableStateOf(mutableMapOf<Int, String>()) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        when (testState) {
            is TestQuizzesUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is TestQuizzesUiState.Success -> {
                val quizzes = (testState as TestQuizzesUiState.Success).quizzes
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
                        // отправляем ответы по каждому вопросу
                        userAnswers.forEach { (quizId, answer) ->
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
            is TestQuizzesUiState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = (testState as TestQuizzesUiState.Error).message,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
            TestQuizzesUiState.Idle -> Unit
        }
    }
}
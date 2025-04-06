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
import androidx.navigation.NavController
import com.example.infoquizapp.Routes
import com.example.infoquizapp.presentation.quiz.viewmodel.QuizViewModel
import com.example.infoquizapp.presentation.quiz.viewmodel.TestQuizzesUiState
import com.example.infoquizapp.presentation.quiz.viewmodel.TestResultUiState
import com.example.infoquizapp.utils.TokenManager

@Composable
fun QuizTestScreen(navController: NavController, viewModel: QuizViewModel, quizType: String) {
    val context = LocalContext.current
    val token = TokenManager.getToken(context) ?: ""

    // Загружаем тест при старте экрана
    LaunchedEffect(quizType, token) {
        viewModel.resetTest()
        viewModel.loadTest(quizType, token)
    }

    val testState by viewModel.testQuizzesState.collectAsState()
    val testResultState by viewModel.testResultState.collectAsState()

    // Локальное состояние для отслеживания текущего вопроса
    var currentQuestionIndex by remember { mutableStateOf(0) }
    // Локальное состояние для ответов пользователя (можно синхронизировать с ViewModel.userAnswers)
    var userAnswers by remember { mutableStateOf(mutableMapOf<Int, String>()) }

    // Если тест успешно отправлен, переходим к экрану результата
    LaunchedEffect(testResultState) {
        if (testResultState is TestResultUiState.Success) {
            navController.navigate(Routes.TestResult.route) {
                popUpTo(Routes.TestResult.route) { inclusive = true }
            }
        }
    }

    when (testState) {
        is TestQuizzesUiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is TestQuizzesUiState.Success -> {
            val quizzes = (testState as TestQuizzesUiState.Success).quizzes
            // Защита от некорректного индекса
            if (quizzes.isNotEmpty() && currentQuestionIndex in quizzes.indices) {
                QuizPlanetQuestionScreen(
                    quiz = quizzes[currentQuestionIndex],
                    totalQuestions = quizzes.size,
                    currentIndex = currentQuestionIndex,
                    userAnswer = userAnswers[quizzes[currentQuestionIndex].id] ?: "",
                    onAnswerChanged = { answer ->
                        userAnswers = userAnswers.toMutableMap().apply {
                            this[quizzes[currentQuestionIndex].id] = answer
                        }
                    },
                    onNext = {
                        // Переход к следующему вопросу
                        if (currentQuestionIndex < quizzes.lastIndex) {
                            currentQuestionIndex++
                        }
                    },
                    onBack = {
                        if (currentQuestionIndex > 0) {
                            currentQuestionIndex--
                        }
                    },
                    onSubmit = {
                        // Отправляем тест – перебираем все ответы и отправляем их на сервер
                        viewModel.submitTest(userAnswers, token)
                    }
                )
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
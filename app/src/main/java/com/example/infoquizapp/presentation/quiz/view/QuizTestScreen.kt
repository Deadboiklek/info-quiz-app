package com.example.infoquizapp.presentation.quiz.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
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
import androidx.navigation.NavController
import com.example.infoquizapp.Routes
import com.example.infoquizapp.presentation.quiz.viewmodel.QuizViewModel
import com.example.infoquizapp.presentation.quiz.viewmodel.TestQuizzesUiState
import com.example.infoquizapp.presentation.quiz.viewmodel.TestResultUiState
import com.example.infoquizapp.utils.TokenManager

@Composable
fun QuizTestScreen(
    navController: NavController,
    viewModel: QuizViewModel,
    quizType: String
) {
    val context = LocalContext.current
    val token = TokenManager.getToken(context) ?: ""

    LaunchedEffect(quizType, token) {
        viewModel.resetTest()
        viewModel.loadTest(quizType, token)
    }

    val testState by viewModel.testQuizzesState.collectAsState()
    val testResultState by viewModel.testResultState.collectAsState()
    val answerResult by viewModel.answerResult.collectAsState()

    // Локальное состояние ответов пользователя
    val userAnswers = remember { mutableStateMapOf<Int, String>() }
    // Текущий индекс вопроса
    var currentIndex by remember { mutableStateOf(0) }

    // Сбрасываем результат проверки при смене вопроса
    LaunchedEffect(currentIndex) {
        viewModel.resetAnswerResult()
    }

    // После завершения теста навигация к результатам
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
            if (quizzes.isNotEmpty() && currentIndex in quizzes.indices) {
                val quiz = quizzes[currentIndex]
                QuizPlanetQuestionScreen(
                    quiz = quiz,
                    totalQuestions = quizzes.size,
                    currentIndex = currentIndex,
                    userAnswer = userAnswers[quiz.id] ?: "",
                    answerResult = answerResult,
                    onAnswerChanged = { answer ->
                        userAnswers[quiz.id] = answer
                    },
                    onCheckAnswer = {
                        val answer = userAnswers[quiz.id] ?: ""
                        viewModel.checkAnswer(quiz.id, answer, token)
                        // сохраняем локально для подсчёта
                        if (answerResult?.isCorrect == true) {
                            // ничего не делаем — опыт начисляется в ViewModel
                        }
                    },
                    onNext = { if (currentIndex < quizzes.lastIndex) currentIndex++ },
                    onBack = { if (currentIndex > 0) currentIndex-- },
                    onSubmitTest = {
                        viewModel.submitTest(userAnswers.toMap(), token)
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

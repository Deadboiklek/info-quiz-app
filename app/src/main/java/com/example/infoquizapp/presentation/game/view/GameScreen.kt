package com.example.infoquizapp.presentation.game.view

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.infoquizapp.presentation.game.viewmodel.GameViewModel
import com.example.infoquizapp.utils.TokenManager

@Composable
fun GameScreen(
    viewModel: GameViewModel,
    onExit: () -> Unit // Callback для выхода на предыдущий экран
) {

    val context = LocalContext.current
    val token = TokenManager.getToken(context) ?: ""

    val gameState by viewModel.gameState.collectAsState()
    var screenWidth by remember { mutableStateOf(300f) }
    var screenHeight by remember { mutableStateOf(800f) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .onSizeChanged { size ->
                screenWidth = size.width.toFloat()
                screenHeight = size.height.toFloat()
            }
            .background(Color.Black)
    ) {
        // Игровой Canvas
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                color = Color.Blue,
                radius = 20f,
                center = gameState.spaceshipPosition
            )
            gameState.asteroids.forEach { asteroid ->
                drawCircle(
                    color = Color.Gray,
                    radius = asteroid.size / 2,
                    center = asteroid.position
                )
            }
            gameState.monster?.let { monster ->
                drawCircle(
                    color = Color.Red,
                    radius = monster.size / 2,
                    center = monster.position
                )
            }
        }

        // Слой управления кораблём
        SpaceshipControlArea(
            viewModel = viewModel,
            screenWidth = screenWidth,
            screenHeight = screenHeight
        )

        // Счет и жизни
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Score: ${gameState.score}",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )
            Text(
                text = "Lives: ${gameState.lives}",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )
        }

        // Диалог с вопросом (если игра приостановлена для вопроса)
        if (gameState.isPausedForQuestion && gameState.gameQuiz != null) {
            AlertDialog(
                onDismissRequest = { /* запрещаем закрытие вне кнопок */ },
                title = { Text("Вопрос") },
                text = { Text(gameState.gameQuiz!!.question) },
                confirmButton = {
                    Button(onClick = { viewModel.onAnswerSelected(1, token) }) {
                        Text(gameState.gameQuiz!!.option1)
                    }
                },
                dismissButton = {
                    Button(onClick = { viewModel.onAnswerSelected(2, token) }) {
                        Text(gameState.gameQuiz!!.option2)
                    }
                }
            )
        }

        // Диалог окончания игры
        if (gameState.isGameOver) {
            AlertDialog(
                onDismissRequest = { /* запрещаем закрытие вне кнопок */ },
                title = { Text("Игра завершена") },
                text = { Text("Ваш результат: ${gameState.score} очков") },
                confirmButton = {
                    Button(onClick = {
                        // При выборе "Начать заново" перезапускаем игру
                        viewModel.startGame(token, screenWidth, screenHeight)
                    }) {
                        Text("Начать заново")
                    }
                },
                dismissButton = {
                    Button(onClick = {
                        // При выборе "Выйти" останавливаем игру и вызываем onExit для навигации назад
                        onExit()
                        viewModel.resetGame()
                    }) {
                        Text("Выйти")
                    }
                }
            )
        }

        // Запуск игрового цикла (если игра еще не окончена)
        LaunchedEffect(screenWidth, screenHeight) {
            if (!gameState.isGameOver) {
                viewModel.startGame(token, screenWidth, screenHeight)
            }
        }
    }
}
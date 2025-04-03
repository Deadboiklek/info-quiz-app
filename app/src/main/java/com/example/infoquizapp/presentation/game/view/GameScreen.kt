package com.example.infoquizapp.presentation.game.view

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.infoquizapp.presentation.game.viewmodel.GameViewModel
import com.example.infoquizapp.utils.TokenManager

@Composable
fun GameScreen(viewModel: GameViewModel){

    val context = LocalContext.current
    val token = TokenManager.getToken(context) ?: ""

    val gameState by viewModel.gameState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
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

        // Диалог с вопросом
        if (gameState.isPausedForQuestion && gameState.gameQuiz != null) {
            AlertDialog(
                onDismissRequest = { /* Запрет закрытия вне диалога */ },
                title = { Text("Вопрос") },
                text = { Text(gameState.gameQuiz!!.question) },
                confirmButton = {
                    Button(onClick = { viewModel.onAnswerSelected(1) }) {
                        Text(gameState.gameQuiz!!.option1)
                    }
                },
                dismissButton = {
                    Button(onClick = { viewModel.onAnswerSelected(2) }) {
                        Text(gameState.gameQuiz!!.option2)
                    }
                }
            )
        }
    }
}
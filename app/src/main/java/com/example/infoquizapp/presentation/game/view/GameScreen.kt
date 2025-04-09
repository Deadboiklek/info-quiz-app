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
import androidx.compose.ui.draw.paint
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.example.infoquizapp.R
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

    val spaceshipImage = ImageBitmap.imageResource(id = R.drawable.tarelka)
    val meteorImage = ImageBitmap.imageResource(id = R.drawable.meteor)

    val normalMonsterImage = ImageBitmap.imageResource(id = R.drawable.monster_normal)
    val hardMonsterImage = ImageBitmap.imageResource(id = R.drawable.monster_hard)


    Box(
        modifier = Modifier
            .fillMaxSize()
            .onSizeChanged { size ->
                screenWidth = size.width.toFloat()
                screenHeight = size.height.toFloat()
            }
            .paint(
                painter = painterResource(id = R.drawable.game_background),
                contentScale = ContentScale.FillBounds
            )
    ) {
        // Игровой Canvas
        Canvas(modifier = Modifier.fillMaxSize()) {

            val shipOffset = gameState.spaceshipPosition - Offset(
                spaceshipImage.width / 2f,
                spaceshipImage.height / 2f
            )
            drawImage(spaceshipImage, topLeft = shipOffset)

            gameState.asteroids.forEach { asteroid ->
                val topLeft = asteroid.position - Offset(meteorImage.width / 2f, meteorImage.height / 2f)
                drawImage(meteorImage, topLeft = topLeft)
            }

            gameState.monster?.let { monster ->
                val monsterImage = if (monster.difficulty == "hard") hardMonsterImage else normalMonsterImage
                drawIntoCanvas { canvas ->
                    val scaleX = monster.width / monsterImage.width.toFloat()
                    val scaleY = monster.height / monsterImage.height.toFloat()
                    canvas.save()
                    canvas.translate(monster.position.x, monster.position.y)
                    canvas.scale(scaleX, scaleY)
                    canvas.drawImage(monsterImage, Offset.Zero, Paint())
                    canvas.restore()
                }
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
                        viewModel.startGame(token, screenWidth, screenHeight)
                    }) {
                        Text("Начать заново")
                    }
                },
                dismissButton = {
                    Button(onClick = {
                        viewModel.resetGame()
                        onExit()
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
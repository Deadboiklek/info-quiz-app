package com.example.infoquizapp.presentation.game.viewmodel

import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.infoquizapp.data.gamequiz.model.GameQuizOut
import com.example.infoquizapp.domain.gamequiz.usecases.CompleteGameQuizUseCase
import com.example.infoquizapp.domain.gamequiz.usecases.GetGameQuizUseCase
import com.example.infoquizapp.presentation.game.model.Asteroid
import com.example.infoquizapp.presentation.game.model.Monster
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.math.sqrt
import kotlin.random.Random

// Функция для вычисления расстояния
fun Offset.distance(): Float = sqrt(x * x + y * y)

// Состояние игры
data class GameState(
    val spaceshipPosition: Offset = Offset(150f, 600f),
    val asteroids: List<Asteroid> = emptyList(),
    val monster: Monster? = null,
    val isPausedForQuestion: Boolean = false,
    val gameQuiz: GameQuizOut? = null,
    val lives: Int = 3,
    val score: Int = 0,
    val isGameOver: Boolean = false
)

class GameViewModel(
    private val getGameQuizUseCase: GetGameQuizUseCase,
    private val completeGameQuizUseCase: CompleteGameQuizUseCase
) : ViewModel() {

    private val _gameState = MutableStateFlow(GameState())
    val gameState: StateFlow<GameState> = _gameState

    private var gameIsRunning = true
    private var quizRequested = false

    // Запуск игры с передачей token, ширины и высоты экрана
    fun startGame(token: String, screenWidth: Float, screenHeight: Float) {
        // Сбрасываем состояние, если игра была завершена ранее
        _gameState.value = GameState()
        gameIsRunning = true
        quizRequested = false
        startGameLoop(token, screenWidth, screenHeight)
    }

    private fun startGameLoop(token: String, screenWidth: Float, screenHeight: Float) {
        viewModelScope.launch {
            while (gameIsRunning) {
                delay(16L) // ~60 FPS
                // Если игра не приостановлена и не окончена
                if (!_gameState.value.isPausedForQuestion && !_gameState.value.isGameOver) {
                    updateGameState(token, screenWidth, screenHeight)
                }
            }
        }
    }

    private fun updateGameState(token: String, screenWidth: Float, screenHeight: Float) {
        val currentState = _gameState.value

        // Обновляем положение астероидов: сдвигаем вниз
        val movedAsteroids = currentState.asteroids.map { asteroid ->
            asteroid.copy(position = asteroid.position.copy(y = asteroid.position.y + asteroid.speed))
        }
        // Оставляем астероиды, которые еще на экране (y меньше screenHeight)
        val onScreenAsteroids = movedAsteroids.filter { it.position.y < screenHeight }

        val spaceshipPos = currentState.spaceshipPosition
        val spaceshipRadius = 20f

        // Если астероид сталкивается с кораблем, он уничтожается
        var lives = currentState.lives
        var score = currentState.score
        val remainingAsteroids = onScreenAsteroids.filter { asteroid ->
            val distance = (asteroid.position - spaceshipPos).distance()
            val collisionDistance = (asteroid.size / 2 + spaceshipRadius)
            if (distance < collisionDistance) {
                // Столкновение: уменьшаем жизни и очки, метеор уничтожается
                lives = (lives - 1).coerceAtLeast(0)
                score -= 10
                false
            } else {
                true
            }
        }

        // Если жизни достигли 0, завершаем игру
        if (lives == 0) {
            gameIsRunning = false
            _gameState.value = currentState.copy(
                asteroids = remainingAsteroids,
                lives = lives,
                score = score,
                isGameOver = true
            )
            return
        }

        // Добавляем новый астероид, если их меньше 5, при условии, что он не накладывается на существующие
        val newAsteroids = if (remainingAsteroids.size < 5) {
            val maxAttempts = 10
            var newAsteroid: Asteroid? = null
            for (i in 0 until maxAttempts) {
                val randomX = Random.nextFloat() * screenWidth
                val candidate = Asteroid(position = Offset(randomX, 0f))
                if (remainingAsteroids.all { existing ->
                        (existing.position - candidate.position).distance() > (existing.size / 2 + candidate.size / 2)
                    }) {
                    newAsteroid = candidate
                    break
                }
            }
            if (newAsteroid != null) remainingAsteroids + newAsteroid else remainingAsteroids
        } else {
            remainingAsteroids
        }

        // Логика появления и движения монстра
        var isPausedForQuestion = currentState.isPausedForQuestion
        var monster: Monster? = currentState.monster
        if (monster == null && Random.nextInt(1000) < 5) {
            monster = Monster(position = Offset(Random.nextFloat() * screenWidth, 0f))
        } else if (monster != null) {
            monster = monster.copy(position = monster.position.copy(y = monster.position.y + monster.speed))
            val dist = (monster.position - spaceshipPos).distance()
            if (dist < (monster.size / 2 + spaceshipRadius)) {
                if (!quizRequested) {
                    isPausedForQuestion = true
                    quizRequested = true
                    fetchGameQuiz(token)
                }
            }
        }

        _gameState.value = currentState.copy(
            asteroids = newAsteroids,
            monster = monster,
            lives = lives,
            score = score,
            isPausedForQuestion = isPausedForQuestion
        )
    }

    private fun fetchGameQuiz(token: String) {
        viewModelScope.launch {
            val result = getGameQuizUseCase("default", token)
            result.gameQuiz?.let { quiz ->
                _gameState.value = _gameState.value.copy(gameQuiz = quiz)
            }
        }
    }

    fun onAnswerSelected(option: Int, token: String) {
        val currentState = _gameState.value
        var lives = currentState.lives
        var score = currentState.score

        currentState.gameQuiz?.let { quiz ->
            val selectedAnswer = if (option == 1) quiz.option1 else quiz.option2
            if (selectedAnswer == quiz.correctAnswer) {
                score += quiz.experienceReward
                viewModelScope.launch {
                    completeGameQuizUseCase(quiz.experienceReward, token)
                }
            } else {
                score -= 10
                lives = (lives - 1).coerceAtLeast(0)
            }
        }

        _gameState.value = currentState.copy(
            score = score,
            lives = lives,
            isPausedForQuestion = false,
            gameQuiz = null,
            monster = null
        )
        quizRequested = false
    }

    fun moveSpaceship(offset: Offset, screenWidth: Float, screenHeight: Float) {
        val currentState = _gameState.value
        val spaceshipRadius = 20f
        val newX = (currentState.spaceshipPosition.x + offset.x)
            .coerceIn(spaceshipRadius, screenWidth - spaceshipRadius)
        val newY = (currentState.spaceshipPosition.y + offset.y)
            .coerceIn(spaceshipRadius, screenHeight - spaceshipRadius)
        _gameState.value = currentState.copy(spaceshipPosition = Offset(newX, newY))
    }

    fun resetGame() {
        _gameState.value = GameState()
        gameIsRunning = false
        quizRequested = false
    }
}
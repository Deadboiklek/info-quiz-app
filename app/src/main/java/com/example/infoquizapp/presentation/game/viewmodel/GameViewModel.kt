package com.example.infoquizapp.presentation.game.viewmodel

import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.infoquizapp.data.gamequiz.model.GameQuizOut
import com.example.infoquizapp.domain.gamequiz.usecases.CompleteGameQuizUseCase
import com.example.infoquizapp.domain.gamequiz.usecases.GetGameQuizUseCase
import com.example.infoquizapp.presentation.game.model.Asteroid
import com.example.infoquizapp.presentation.game.model.Monster
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.math.sqrt
import kotlin.random.Random

fun Offset.distance(): Float = sqrt(x * x + y * y)

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

    private var gameSpeedMultiplier = 1f

    fun startGame(token: String, screenWidth: Float, screenHeight: Float) {
        _gameState.value = GameState(
            spaceshipPosition = Offset(screenWidth / 2, screenHeight / 2 + 40f)
        )
        gameIsRunning = true
        quizRequested = false
        gameSpeedMultiplier = 1f
        startGameLoop(token, screenWidth, screenHeight)
    }

    private var gameJob: Job? = null

    private fun startGameLoop(token: String, screenWidth: Float, screenHeight: Float) {
        gameJob?.cancel()
        gameJob = viewModelScope.launch {
            while (gameIsRunning) {
                delay(16L) // ~60 FPS
                if (!_gameState.value.isPausedForQuestion && !_gameState.value.isGameOver) {
                    updateGameState(token, screenWidth, screenHeight)
                }
            }
        }
    }

    private fun updateGameState(token: String, screenWidth: Float, screenHeight: Float) {
        val currentState = _gameState.value

        val movedAsteroids = currentState.asteroids.map { asteroid ->
            asteroid.copy(position = asteroid.position.copy(y = asteroid.position.y + asteroid.speed * gameSpeedMultiplier))
        }
        val onScreenAsteroids = movedAsteroids.filter { it.position.y < screenHeight }

        val spaceshipPos = currentState.spaceshipPosition
        val spaceshipRadius = 20f

        var lives = currentState.lives
        var score = currentState.score

        val remainingAsteroids = onScreenAsteroids.filter { asteroid ->
            val distance = (asteroid.position - spaceshipPos).distance()
            val collisionDistance = (asteroid.size / 2 + spaceshipRadius)
            if (distance < collisionDistance) {
                lives = (lives - 1).coerceAtLeast(0)
                score -= 10
                false
            } else {
                true
            }
        }

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

        val newAsteroids = if (remainingAsteroids.size < 5) {
            val maxAttempts = 10
            var newAsteroid: Asteroid? = null
            for (i in 0 until maxAttempts) {
                val randomX = Random.nextFloat() * screenWidth
                val candidate = Asteroid(
                    position = Offset(randomX, -Random.nextFloat() * 50f),
                    size = (30f + Random.nextFloat() * 20f) * 2,
                    speed = (3f + Random.nextFloat() * 4f) * 4
                )
                if (remainingAsteroids.all { existing ->
                        (existing.position - candidate.position).distance() >
                                (existing.size / 2 + candidate.size / 2)
                    }) {
                    newAsteroid = candidate
                    break
                }
            }
            if (newAsteroid != null) remainingAsteroids + newAsteroid else remainingAsteroids
        } else {
            remainingAsteroids
        }

        var isPausedForQuestion = currentState.isPausedForQuestion
        var monster: Monster? = currentState.monster
        if (monster == null && Random.nextInt(1000) < 5) {
            val difficulty = if (Random.nextFloat() < 0.3f) "hard" else "normal"
            monster = Monster(
                position = Offset(0f, -60f),
                width = screenWidth,
                height = 540f,
                speed = 3f * gameSpeedMultiplier,
                difficulty = difficulty
            )
        } else if (monster != null) {
            monster = monster.copy(position = monster.position.copy(y = monster.position.y + monster.speed))
            if (spaceshipPos.y + spaceshipRadius > monster.position.y &&
                spaceshipPos.y - spaceshipRadius < monster.position.y + monster.height
            ) {
                if (!quizRequested) {
                    isPausedForQuestion = true
                    quizRequested = true
                    fetchGameQuiz(token, monster.difficulty)
                }
            }
            if (monster.position.y > screenHeight) {
                monster = null
            }
        }

        _gameState.value = currentState.copy(
            asteroids = newAsteroids,
            monster = monster,
            lives = lives,
            score = score,
            isGameOver = false,
            isPausedForQuestion = isPausedForQuestion
        )
    }

    private fun fetchGameQuiz(token: String, difficulty: String) {
        viewModelScope.launch {
            val result = getGameQuizUseCase(difficulty, token)
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
                gameSpeedMultiplier *= 1.1f
                viewModelScope.launch {
                    completeGameQuizUseCase(quiz.experienceReward, quiz.id, token)
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
        gameJob?.cancel()
        _gameState.value = GameState()
        gameIsRunning = false
        quizRequested = false
    }
}
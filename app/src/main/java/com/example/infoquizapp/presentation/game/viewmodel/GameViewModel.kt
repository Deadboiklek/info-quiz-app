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
    val score: Int = 0
)

class GameViewModel(
    private val getGameQuizUseCase: GetGameQuizUseCase,
    private val completeGameQuizUseCase: CompleteGameQuizUseCase,
    private val token: String
) : ViewModel() {

    private val _gameState = MutableStateFlow(GameState())
    val gameState: StateFlow<GameState> = _gameState

    private var gameIsRunning = true
    private var quizRequested = false

    init {
        startGameLoop()
    }

    private fun startGameLoop() {
        viewModelScope.launch {
            while (gameIsRunning) {
                delay(16L) // ~60 FPS
                if (!_gameState.value.isPausedForQuestion) {
                    updateGameState()
                }
            }
        }
    }

    private fun updateGameState() {
        val currentState = _gameState.value

        // Обновляем астероиды
        val updatedAsteroids = currentState.asteroids.map { asteroid ->
            asteroid.copy(position = asteroid.position.copy(y = asteroid.position.y + asteroid.speed))
        }.filter { it.position.y < 800f }

        val newAsteroids = if (updatedAsteroids.size < 5) {
            val randomX = Random.nextFloat() * 300f
            updatedAsteroids + Asteroid(position = Offset(randomX, 0f))
        } else {
            updatedAsteroids
        }

        // Проверка столкновения с астероидами
        val spaceshipPos = currentState.spaceshipPosition
        val asteroidCollision = newAsteroids.any { asteroid ->
            (asteroid.position - spaceshipPos).distance() < (asteroid.size / 2 + 20f)
        }

        var lives = currentState.lives
        var score = currentState.score
        var isPausedForQuestion = currentState.isPausedForQuestion
        var monster: Monster? = currentState.monster

        if (asteroidCollision) {
            lives = (lives - 1).coerceAtLeast(0)
            score -= 10
        }

        // Логика появления монстра
        if (monster == null && Random.nextInt(1000) < 5) {
            monster = Monster(position = Offset(Random.nextFloat() * 300f, 0f))
        } else if (monster != null) {
            monster = monster.copy(position = monster.position.copy(y = monster.position.y + monster.speed))
            val dist = (monster.position - spaceshipPos).distance()
            if (dist < (monster.size / 2 + 20f)) {
                if (!quizRequested) {
                    isPausedForQuestion = true
                    quizRequested = true
                    fetchGameQuiz()
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

    private fun fetchGameQuiz() {
        viewModelScope.launch {
            val result = getGameQuizUseCase("default", token)
            result.gameQuiz?.let { quiz ->
                _gameState.value = _gameState.value.copy(gameQuiz = quiz)
            }
        }
    }

    fun onAnswerSelected(option: Int) {
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

    fun moveSpaceship(offset: Offset) {
        val currentState = _gameState.value
        _gameState.value = currentState.copy(
            spaceshipPosition = currentState.spaceshipPosition + offset
        )
    }
}
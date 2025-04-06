package com.example.infoquizapp.presentation.quiz.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.infoquizapp.R
import com.example.infoquizapp.data.quiz.model.QuizOut

@Composable
fun QuizPlanetQuestionScreen(
    quiz: QuizOut,
    totalQuestions: Int,
    currentIndex: Int,
    userAnswer: String,
    onAnswerChanged: (String) -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit,
    onSubmit: () -> Unit
) {
    // Основной контейнер с космическим фоном
    Box(modifier = Modifier.fillMaxSize()) {
        // Космический фон (на весь экран)
        Image(
            painter = painterResource(id = R.drawable.game_background), // замените на свой ресурс
            contentDescription = "Космический фон",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Планеты и космонавт как декоративная панель в верхней части экрана
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .align(Alignment.TopCenter)
        ) {
            PlanetScreen(
                quizzes = List(totalQuestions) { quiz }, // Здесь можно передавать список вопросов для отрисовки планет
                currentPlanetIndex = currentIndex
            )
        }

        // Контейнер для вопроса и вариантов ответа
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .align(Alignment.Center)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = quiz.question, style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.height(16.dp))
                    // Если вариантов ответа нет – текстовое поле
                    if (quiz.options.isNullOrEmpty()) {
                        OutlinedTextField(
                            value = userAnswer,
                            onValueChange = { newValue ->
                                onAnswerChanged(newValue)
                            },
                            label = { Text("Введите ваш ответ") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {
                        // Если варианты есть – радио-кнопки
                        var currentSelection by remember { mutableStateOf(userAnswer) }
                        quiz.options?.forEach { option ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            ) {
                                RadioButton(
                                    selected = (currentSelection == option),
                                    onClick = {
                                        currentSelection = option
                                        onAnswerChanged(option)
                                    }
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = option)
                            }
                        }
                    }
                }
            }
        }

        // Панель навигации в нижней части экрана
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (currentIndex > 0) {
                Button(onClick = onBack) {
                    Text("Назад")
                }
            } else {
                Spacer(modifier = Modifier.width(80.dp))
            }

            if (currentIndex < totalQuestions - 1) {
                Button(onClick = onNext) {
                    Text("Далее")
                }
            } else {
                Button(
                    onClick = onSubmit,
                    modifier = Modifier.fillMaxWidth(0.5f)
                ) {
                    Text("Отправить тест")
                }
            }
        }
    }
}
package com.example.infoquizapp.presentation.quiz.view

import android.graphics.BitmapFactory
import android.util.Base64
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
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
                    // Отображаем текст вопроса
                    Text(
                        text = quiz.question,
                        style = MaterialTheme.typography.bodyLarge
                    )

                    // Если есть картинка, декодируем и отображаем её
                    if (!quiz.image.isNullOrEmpty()) {
                        // Декодируем Base64-строку в байты
                        val imageBytes = Base64.decode(quiz.image, Base64.DEFAULT)
                        // Декодируем байты в Bitmap
                        val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                        bitmap?.let {
                            Spacer(modifier = Modifier.height(16.dp))
                            Image(
                                bitmap = it.asImageBitmap(),
                                contentDescription = "Изображение вопроса",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp),
                                contentScale = ContentScale.Fit
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }

                    // Поле для ввода ответа
                    OutlinedTextField(
                        value = userAnswer,
                        onValueChange = { newValue -> onAnswerChanged(newValue) },
                        label = { Text("Введите ваш ответ") },
                        modifier = Modifier.fillMaxWidth()
                    )
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
package com.example.infoquizapp.presentation.quiz.view

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTransformGestures
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.infoquizapp.R
import com.example.infoquizapp.data.quiz.model.AnswerOut
import com.example.infoquizapp.data.quiz.model.QuizOut

@Composable
fun QuizPlanetQuestionScreen(
    quiz: QuizOut,
    totalQuestions: Int,
    currentIndex: Int,
    userAnswer: String,
    answerResult: AnswerOut?,
    onAnswerChanged: (String) -> Unit,
    onCheckAnswer: () -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit,
    onSubmitTest: () -> Unit
) {

    val isSubmitted = answerResult != null
    val isCorrect = answerResult?.isCorrect == true

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
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 2.dp,
                        color = when {
                            !isSubmitted -> Color.Transparent
                            isCorrect -> Color.Green
                            else -> Color.Red
                        },
                        shape = RoundedCornerShape(8.dp)
                    ),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),

            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Отображаем текст вопроса
                    Text(
                        text = quiz.question,
                        style = MaterialTheme.typography.bodyLarge
                    )

                    // Если есть картинка, декодируем и отображаем её
                    if (!quiz.image.isNullOrEmpty()) {
                        val imageBytes = Base64.decode(quiz.image, Base64.DEFAULT)
                        val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                        bitmap?.let {
                            Spacer(modifier = Modifier.height(16.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                            ) {
                                var scale by remember { mutableStateOf(1f) }
                                var offset by remember { mutableStateOf(Offset.Zero) }

                                Image(
                                    bitmap = it.asImageBitmap(),
                                    contentDescription = "Изображение вопроса",
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clipToBounds()
                                        .pointerInput(Unit) {
                                            detectTransformGestures(
                                                panZoomLock = false,
                                                onGesture = { centroid, pan, zoom, _ ->
                                                    scale = (scale * zoom).coerceIn(1f, 5f)

                                                    val extraWidth = (scale - 1) * size.width
                                                    val extraHeight = (scale - 1) * size.height

                                                    val maxX = extraWidth / 2
                                                    val minX = -maxX
                                                    val maxY = extraHeight / 2
                                                    val minY = -maxY

                                                    offset = Offset(
                                                        x = (offset.x + pan.x * scale)
                                                            .coerceIn(minX, maxX),
                                                        y = (offset.y + pan.y * scale)
                                                            .coerceIn(minY, maxY)
                                                    )
                                                }
                                            )
                                        }
                                        .graphicsLayer {
                                            scaleX = scale
                                            scaleY = scale
                                            translationX = offset.x
                                            translationY = offset.y
                                        },
                                    contentScale = ContentScale.Fit
                                )
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }

                    // Поле для ввода ответа
                    OutlinedTextField(
                        value = userAnswer,
                        onValueChange = { if (!isSubmitted) onAnswerChanged(it) },
                        label = { Text("Введите ваш ответ") },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isSubmitted
                    )

                    if (isSubmitted && !isCorrect) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Правильный ответ: ${answerResult?.correctAnswer}",
                            style = MaterialTheme.typography.bodyMedium
                        )
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
            if (!isSubmitted) {
                Button(onClick = onCheckAnswer, modifier = Modifier.fillMaxWidth(0.5f)) {
                    Text("Проверить ответ")
                }
            } else {
                if (currentIndex < totalQuestions - 1) {
                    Button(onClick = onNext) { Text("Далее") }
                } else {
                    Button(onClick = onSubmitTest, modifier = Modifier.fillMaxWidth(0.5f)) {
                        Text("Отправить тест")
                    }
                }
            }
        }
    }
}
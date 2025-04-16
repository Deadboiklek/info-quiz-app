package com.example.infoquizapp.presentation.quiz.view

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import com.example.infoquizapp.data.quiz.model.QuizOut

@Composable
fun QuizQuestionItem(
    quiz: QuizOut,
    selectedAnswer: String,
    onAnswerSelected: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = quiz.question,
            style = androidx.compose.material3.MaterialTheme.typography.bodyLarge
        )
        // Если есть изображение (поле image), декодируем его и отображаем
        if (!quiz.image.isNullOrEmpty()) {
            // Декодирование Base64-строки в ByteArray
            val imageBytes = Base64.decode(quiz.image, Base64.DEFAULT)
            // Декодирование байтов в Bitmap
            val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            // Преобразование Bitmap в ImageBitmap и отображение
            bitmap?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = "Изображение вопроса",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp) // задайте нужную высоту
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = selectedAnswer,
            onValueChange = { newValue -> onAnswerSelected(newValue) },
            label = { Text("Введите ваш ответ") },
            modifier = Modifier.fillMaxWidth()
        )
    }
}
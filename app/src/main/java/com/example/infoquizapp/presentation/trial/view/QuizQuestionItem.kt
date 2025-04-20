package com.example.infoquizapp.presentation.trial.view

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.example.infoquizapp.data.quiz.model.AnswerOut
import com.example.infoquizapp.data.quiz.model.QuizOut

@Composable
fun QuizQuestionItem(
    quiz: QuizOut,
    selectedAnswer: String,
    onAnswerSelected: (String) -> Unit,
    feedback: AnswerOut?
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                BorderStroke(
                    2.dp,
                    when {
                        feedback == null -> Color.Transparent
                        feedback.isCorrect -> Color.Green
                        else -> Color.Red
                    }
                ),
                RoundedCornerShape(8.dp)
            )
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(quiz.question)
            Spacer(modifier = Modifier.height(8.dp))
            // Показ изображения, если есть
            quiz.image?.let { imageStr ->
                if (imageStr.isNotEmpty()) {
                    val imageBytes = Base64.decode(imageStr, Base64.DEFAULT)
                    val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                    bitmap?.let {
                        Image(
                            bitmap = it.asImageBitmap(),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
            OutlinedTextField(
                value = selectedAnswer,
                onValueChange = { if (feedback == null) onAnswerSelected(it) },
                enabled = feedback == null,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            if (feedback != null && !feedback.isCorrect) {
                Text("Правильный ответ: ${feedback.correctAnswer}")
            }
        }
    }
}
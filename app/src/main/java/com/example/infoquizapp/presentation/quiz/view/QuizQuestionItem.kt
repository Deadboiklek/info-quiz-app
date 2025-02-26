package com.example.infoquizapp.presentation.quiz.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.unit.dp
import com.example.infoquizapp.data.quiz.model.QuizOut

@Composable
fun QuizQuestionItem(
    quiz: QuizOut,
    selectedAnswer: String,
    onAnswerSelected: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = quiz.question, style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(8.dp))
        //  если options пустой, отображаем текстовое поле
        if (quiz.options.isEmpty()) {
            var textFieldValue by remember { mutableStateOf(selectedAnswer) }
            OutlinedTextField(
                value = textFieldValue,
                onValueChange = { newValue ->
                    textFieldValue = newValue
                    onAnswerSelected(newValue)
                },
                label = { Text("Введите ваш ответ") },
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            // иначе отображаем варианты ответа как радио-кнопки
            var currentSelection by remember { mutableStateOf(selectedAnswer) }
            quiz.options.forEach { option ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                ) {
                    RadioButton(
                        selected = (currentSelection == option),
                        onClick = {
                            currentSelection = option
                            onAnswerSelected(option)
                        }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = option)
                }
            }
        }
    }
}
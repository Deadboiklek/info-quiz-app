package com.example.infoquizapp.presentation.teacher.view

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.infoquizapp.data.teacher.model.TeacherCreateQuiz
import com.example.infoquizapp.presentation.teacher.viewmodel.PostTeacherQuizUiState
import com.example.infoquizapp.presentation.teacher.viewmodel.PostTeacherQuizViewModel
import com.example.infoquizapp.utils.TokenManager

@Composable
fun AddQuizScreen(
    viewModel: PostTeacherQuizViewModel,
    onQuizAdded: () -> Unit
) {

    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val token = TokenManager.getToken(context) ?: ""

    var question by remember { mutableStateOf("") }
    var correctAnswer by remember { mutableStateOf("") }
    var experienceRewardText by remember { mutableStateOf("") }

    // Для выбора типа вопроса используем выпадающий список
    var expanded by remember { mutableStateOf(false) }
    var selectedType by remember { mutableStateOf("") }
    val types = listOf("type1", "type2", "type3")

    // Если добавление прошло успешно, показать сообщение и выполнить навигацию
    LaunchedEffect(uiState) {
        if (uiState is PostTeacherQuizUiState.Success) {
            Toast.makeText(context, "Квест добавлен", Toast.LENGTH_SHORT).show()
            onQuizAdded()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Добавление квиза",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )

        // Текстовое поле для ввода вопроса (многострочное)
        OutlinedTextField(
            value = question,
            onValueChange = { question = it },
            label = { Text("Вопрос") },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 100.dp),
            maxLines = Int.MAX_VALUE,
            singleLine = false
        )

        // Текстовое поле для ввода правильного ответа
        OutlinedTextField(
            value = correctAnswer,
            onValueChange = { correctAnswer = it },
            label = { Text("Правильный ответ") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        // Текстовое поле для ввода награды опыта
        OutlinedTextField(
            value = experienceRewardText,
            onValueChange = { experienceRewardText = it },
            label = { Text("Награда опыта") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true
        )

        // Выпадающий список для выбора типа вопроса
        QuizTypeDropdown(
            selectedType = selectedType,
            onTypeSelected = { selectedType = it }
        )

        Button(
            onClick = {
                // Парсим награду опыта, по умолчанию 0, если не удаётся преобразовать
                val experienceReward = experienceRewardText.toIntOrNull() ?: 0

                // Собираем данные квиза с удалённым полем options
                val quizData = TeacherCreateQuiz(
                    question = question,
                    correctAnswer = correctAnswer,
                    experienceReward = experienceReward,
                    type = selectedType
                )
                viewModel.createQuiz(token, quizData)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("Добавить квиз")
        }

        when (uiState) {
            is PostTeacherQuizUiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }
            is PostTeacherQuizUiState.Error -> {
                Text(
                    text = (uiState as PostTeacherQuizUiState.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
            else -> {}
        }
    }
}
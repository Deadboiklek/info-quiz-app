package com.example.infoquizapp.presentation.teacher.view.addquiz

import android.widget.Toast
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddQuizScreen(
    viewModel: PostTeacherQuizViewModel,
    onQuizAdded: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val token = TokenManager.getToken(context) ?: ""

    var question by remember { mutableStateOf("") }
    var correctAnswer by remember { mutableStateOf("") }
    var experienceRewardText by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf("") }

    // Если добавление прошло успешно, показать сообщение и выполнить навигацию
    LaunchedEffect(uiState) {
        if (uiState is PostTeacherQuizUiState.Success) {
            Toast.makeText(context, "Квест добавлен", Toast.LENGTH_SHORT).show()
            onQuizAdded()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Добавление квиза") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                }
            )
        },
        bottomBar = {
            // Кнопка всегда внизу
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Button(
                    onClick = {
                        val experienceReward = experienceRewardText.toIntOrNull() ?: 0
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
            }
        }
    ) { paddingValues ->
        // Основное содержимое с отступами (не растягивается до краев)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Многострочное поле для вопроса
            OutlinedTextField(
                value = question,
                onValueChange = { question = it },
                label = { Text("Вопрос") },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 100.dp), // Минимальная высота для многострочного ввода
                maxLines = Int.MAX_VALUE,
                singleLine = false
            )

            // Поле для ввода правильного ответа
            OutlinedTextField(
                value = correctAnswer,
                onValueChange = { correctAnswer = it },
                label = { Text("Правильный ответ") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Поле для ввода награды опыта
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
        }
    }
}
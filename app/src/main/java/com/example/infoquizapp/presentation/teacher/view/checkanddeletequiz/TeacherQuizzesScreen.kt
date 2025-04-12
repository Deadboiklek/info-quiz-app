package com.example.infoquizapp.presentation.teacher.view.checkanddeletequiz

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.infoquizapp.presentation.teacher.viewmodel.GetAndDeleteQuizViewModel
import com.example.infoquizapp.utils.TokenManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherQuizzesScreen(
    viewModel: GetAndDeleteQuizViewModel,
    onBack: () -> Unit              // Или кнопка "назад"
) {
    val quizzes by viewModel.quizzesState.collectAsState()
    val errorMessage by viewModel.errorState.collectAsState()
    val context = LocalContext.current
    val token = TokenManager.getToken(context) ?: ""

    // Загружаем квизы при старте экрана
    LaunchedEffect(token) {
        viewModel.loadTeacherQuizzes(token)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Мои квизы") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (errorMessage != null) {
                // Выводим сообщение об ошибке
                Text(
                    text = errorMessage ?: "",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(quizzes) { quiz ->
                        QuizListItem(
                            quiz = quiz,
                            onDeleteClick = {
                                viewModel.deleteQuiz(token, quiz.id)
                                Toast.makeText(context, "Квиз удалён", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                }
            }
        }
    }
}
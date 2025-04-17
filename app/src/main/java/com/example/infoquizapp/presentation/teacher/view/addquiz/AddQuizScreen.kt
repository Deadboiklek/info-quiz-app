package com.example.infoquizapp.presentation.teacher.view.addquiz

import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
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
    val token = TokenManager.getToken(context) ?: ""
    val uiState by viewModel.state.collectAsState()

    var question by remember { mutableStateOf("") }
    var correctAnswer by remember { mutableStateOf("") }
    var expText by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf("") }
    var imageBytes by remember { mutableStateOf<ByteArray?>(null) }
    var imagePreview by remember { mutableStateOf<ImageBitmap?>(null) }

    // Лаунчер для выбора файла из галереи
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            context.contentResolver.openInputStream(it)?.use { stream ->
                val bytes = stream.readBytes()
                if (bytes.size <= 2 * 1024 * 1024) {
                    imageBytes = bytes
                    val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                    imagePreview = bmp.asImageBitmap()
                } else {
                    Toast.makeText(context, "Картинка > 2 МБ", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    LaunchedEffect(uiState) {
        if (uiState is PostTeacherQuizUiState.Success) {
            Toast.makeText(context, "Квиз добавлен", Toast.LENGTH_SHORT).show()
            onQuizAdded()
            viewModel.resetState()
        }
        if (uiState is PostTeacherQuizUiState.Error) {
            Toast.makeText(context, (uiState as PostTeacherQuizUiState.Error).message, Toast.LENGTH_SHORT).show()
            viewModel.resetState()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Добавить квиз") }, navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                }
            })
        },
        bottomBar = {
            Button(
                onClick = {
                    val exp = expText.toIntOrNull() ?: 0
                    viewModel.createQuiz(
                        token, question, correctAnswer, exp, selectedType, imageBytes
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(16.dp)
            ) { Text("Сохранить") }
        }
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                question, { question = it },
                label = { Text("Вопрос") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                correctAnswer, { correctAnswer = it },
                label = { Text("Правильный ответ") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                expText, { expText = it },
                label = { Text("Награда опыта") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            QuizTypeDropdown(selectedType) { selectedType = it }
            Button(onClick = { launcher.launch("image/*") }) {
                Text("Выбрать картинку")
            }
            imagePreview?.let {
                Image(it, contentDescription = null, modifier = Modifier.fillMaxWidth().height(200.dp))
            }
        }
    }
}
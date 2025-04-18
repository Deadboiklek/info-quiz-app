package com.example.infoquizapp.presentation.teacher.view.checkanddeletequiz

import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.infoquizapp.presentation.teacher.view.addquiz.QuizTypeDropdown
import com.example.infoquizapp.presentation.teacher.viewmodel.EditQuizViewModel
import com.example.infoquizapp.utils.TokenManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditQuizScreen(vm: EditQuizViewModel, quizId: Int, onBack: ()->Unit) {


    LaunchedEffect(quizId) {
        vm.resetState()
    }

    val ui by vm.uiState.collectAsState()
    val context = LocalContext.current
    val token = TokenManager.getToken(context)!!
    var imageBytes by remember { mutableStateOf<ByteArray?>(null) }
    var imagePreview by remember { mutableStateOf<ImageBitmap?>(null) }

    LaunchedEffect(ui) {
        when (ui) {
            is EditQuizViewModel.UiState.Success -> {
                Toast.makeText(context, "Сохранено", Toast.LENGTH_SHORT).show()
                onBack()
            }
            is EditQuizViewModel.UiState.Error -> {
                val msg = (ui as EditQuizViewModel.UiState.Error).msg
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            }
            else -> { /* Idle и Loading — ничего не делаем */ }
        }
    }

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

    Scaffold(topBar = {
        TopAppBar(title={ Text("Редактировать квиз") }, navigationIcon = {
            IconButton(onClick=onBack){ Icon(Icons.AutoMirrored.Filled.ArrowBack,"") }
        })
    }, bottomBar = {
        Button(modifier= Modifier.fillMaxWidth().padding(16.dp),
            onClick={ vm.saveChanges(token, quizId) }) {
            Text("Сохранить")
        }
    }) { padding ->
        Column(Modifier.verticalScroll(rememberScrollState()).padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedTextField(vm.question, onValueChange={ vm.question = it }, label={ Text("Вопрос") })
            OutlinedTextField(vm.correctAnswer, onValueChange={ vm.correctAnswer = it }, label={ Text("Ответ") })
            OutlinedTextField(vm.expReward, onValueChange={ vm.expReward = it },
                label={ Text("Опыт") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            QuizTypeDropdown(vm.type){ vm.type = it }
            Button(onClick = { launcher.launch("image/*") }) {
                Text("Выбрать картинку")
            }
            imagePreview?.let {
                Image(it, contentDescription = null, modifier = Modifier.fillMaxWidth().height(200.dp))
            }
        }
    }
}
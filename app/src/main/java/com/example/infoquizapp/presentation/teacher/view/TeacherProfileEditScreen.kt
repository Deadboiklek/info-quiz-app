package com.example.infoquizapp.presentation.teacher.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.infoquizapp.Routes
import com.example.infoquizapp.data.teacher.model.TeacherUpdate
import com.example.infoquizapp.presentation.auth.viewmodel.AuthViewModel
import com.example.infoquizapp.presentation.teacher.viewmodel.TeacherEditUiState
import com.example.infoquizapp.presentation.teacher.viewmodel.TeacherProfileUiState
import com.example.infoquizapp.presentation.teacher.viewmodel.TeacherProfileViewModel
import com.example.infoquizapp.utils.TokenManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherProfileEditScreen(
    vm: TeacherProfileViewModel,
    authVm: AuthViewModel,
    nav: NavController
) {
    val ctx   = LocalContext.current
    val token = TokenManager.getToken(ctx) ?: ""
    val profState = vm.uiState.collectAsState().value
    val editState = vm.editState.collectAsState().value

    // полевые стейты
    var username  by remember { mutableStateOf("") }
    var email     by remember { mutableStateOf("") }
    var firstname by remember { mutableStateOf("") }
    var surname   by remember { mutableStateOf("") }
    var patronymic by remember { mutableStateOf("") }
    var password  by remember { mutableStateOf("") }
    var passChanged by remember { mutableStateOf(false) }

    val fields: List<Triple<String, String, (String) -> Unit>> = listOf(
        Triple("Username",  username)   { new -> username  = new },
        Triple("Email",     email)      { new -> email     = new },
        Triple("Имя",       firstname)  { new -> firstname = new },
        Triple("Фамилия",   surname)    { new -> surname   = new },
        Triple("Отчество",  patronymic) { new -> patronymic= new },
    )

    // заполняем поля раз
    LaunchedEffect(profState) {
        if (profState is TeacherProfileUiState.Success && editState is TeacherEditUiState.Idle) {
            val t = profState.teacher
            username = t.username
            email    = t.email
            firstname = t.firstname
            surname   = t.surname
            patronymic = t.patronymic
        }
    }

    // реакция на успешное сохранение
    LaunchedEffect(editState) {
        if (editState is TeacherEditUiState.Success) {
            if (passChanged) {
                authVm.logout()
                TokenManager.clearToken(ctx)
                nav.navigate(Routes.Login.route) {
                    popUpTo(Routes.Main.route) { inclusive = true }
                }
            } else {
                nav.popBackStack()
            }
        }
    }

    Scaffold(topBar = {
        TopAppBar(title = { Text("Редактировать профиль учителя") },
            navigationIcon = {
                IconButton({ nav.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                }
            }
        )
    }) { padding ->

        Column(
            Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            fields.forEach { (label, value, onChange) ->
                OutlinedTextField(
                    value = value,
                    onValueChange = onChange,
                    label = { Text(label) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    passChanged = it.isNotBlank()
                },
                label = { Text("Новый пароль") },
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                enabled = editState !is TeacherEditUiState.Loading,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            Button(onClick = {
                vm.save(token, TeacherUpdate(
                    username  = username.takeIf(String::isNotBlank),
                    email     = email.takeIf(String::isNotBlank),
                    firstname = firstname.takeIf(String::isNotBlank),
                    surname   = surname.takeIf(String::isNotBlank),
                    patronymic= patronymic.takeIf(String::isNotBlank),
                    password  = password.takeIf(String::isNotBlank)
                )
                )
            },
                enabled = editState !is TeacherEditUiState.Loading,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (editState is TeacherEditUiState.Loading) {
                    CircularProgressIndicator(Modifier.size(24.dp))
                } else Text("Сохранить")
            }

            if (editState is TeacherEditUiState.Error) {
                Text(
                    editState.message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}
package com.example.infoquizapp.presentation.profile.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.infoquizapp.Routes
import com.example.infoquizapp.data.profile.model.UserUpdate
import com.example.infoquizapp.presentation.auth.viewmodel.AuthViewModel
import com.example.infoquizapp.presentation.profile.viewmodel.ProfileEditUiState
import com.example.infoquizapp.presentation.profile.viewmodel.ProfileUiState
import com.example.infoquizapp.presentation.profile.viewmodel.ProfileViewModel
import com.example.infoquizapp.utils.TokenManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileEditScreen(
    profileViewModel: ProfileViewModel,
    authViewModel: AuthViewModel,
    navController: NavController
) {
    val context = LocalContext.current
    val token = TokenManager.getToken(context) ?: ""

    // Стейт для чтения текущего профиля
    val profileState by profileViewModel.uiState.collectAsState()
    // Отдельный стейт для операции редактирования
    val editState by profileViewModel.editState.collectAsState()

    // Локальные поля формы
    var username by remember { mutableStateOf("") }
    var email    by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var passwordChanged by remember { mutableStateOf(false) }
    var usernameChanged by remember { mutableStateOf(false) }
    var emailChanged by remember { mutableStateOf(false) }

    // Заполнить поля данными из profileState один раз перед началом редактирования
    LaunchedEffect(profileState) {
        if (profileState is ProfileUiState.Success && editState is ProfileEditUiState.Idle) {
            val user = (profileState as ProfileUiState.Success).user
            username = user.username
            email    = user.email
        }
    }

    // Реакция на результат редактирования
    LaunchedEffect(editState) {
        when (editState) {
            is ProfileEditUiState.Success -> {
                if (passwordChanged or usernameChanged or emailChanged) {
                    // При смене пароля — логаут и переход на Login
                    authViewModel.logout()
                    TokenManager.clearToken(context)
                    navController.navigate(Routes.Login.route) {
                        popUpTo(Routes.Main.route) { inclusive = true }
                    }
                } else {
                    // Иначе просто возвращаемся назад
                    navController.popBackStack()
                }
            }
            else -> {
                // никаких действий на Idle, Loading или Error
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Редактировать профиль") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = username,
                onValueChange = {
                    username = it
                    usernameChanged = it.isNotBlank()
                                },
                label = { Text("Username") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                enabled = editState !is ProfileEditUiState.Loading
            )
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    emailChanged = it.isNotBlank()
                                },
                label = { Text("Email") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                enabled = editState !is ProfileEditUiState.Loading
            )
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    passwordChanged = it.isNotBlank()
                },
                label = { Text("Новый пароль") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                enabled = editState !is ProfileEditUiState.Loading
            )

            Button(
                onClick = {
                    profileViewModel.saveChanges(
                        token,
                        UserUpdate(
                            username = username.takeIf { it.isNotBlank() },
                            email    = email.takeIf { it.isNotBlank() },
                            password = password.takeIf { it.isNotBlank() }
                        )
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = editState !is ProfileEditUiState.Loading
            ) {
                if (editState is ProfileEditUiState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(24.dp)
                            .align(Alignment.CenterVertically)
                    )
                } else {
                    Text("Сохранить")
                }
            }

            if (editState is ProfileEditUiState.Error) {
                Text(
                    text = (editState as ProfileEditUiState.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}
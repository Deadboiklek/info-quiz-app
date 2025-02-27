package com.example.infoquizapp.presentation.auth.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.infoquizapp.presentation.auth.viewmodel.AuthUiState
import com.example.infoquizapp.presentation.auth.viewmodel.AuthViewModel

@Composable
fun LoginScreen(

    viewModel: AuthViewModel,
    onLoginSuccess: (token: String) -> Unit,
    onSignUpClick: () -> Unit

){

    val uiState by viewModel.uiState.collectAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    LaunchedEffect(uiState) {
        if (uiState is AuthUiState.Success) {
            onLoginSuccess((uiState as AuthUiState.Success).token)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Авторизация",
            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 24.sp),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        //Email textfield
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            singleLine = true
        )

        //Password textfield
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        //Log in button
        Button(
            onClick = { viewModel.login(email, password) },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = "Войти", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Sign up Text
        Text(
            text = "Нет аккаунта? Зарегистрируйтесь!",
            modifier = Modifier
                .clickable { onSignUpClick() },
            style = TextStyle(
                color = MaterialTheme.colorScheme.secondary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        )
    }
}
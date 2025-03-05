package com.example.infoquizapp.presentation.profile.view

import androidx.compose.foundation.interaction.HoverInteraction
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.infoquizapp.presentation.profile.view.profilescreencomponent.TrophiesSection
import com.example.infoquizapp.presentation.profile.view.profilescreencomponent.UserProfileSection
import com.example.infoquizapp.presentation.profile.viewmodel.ProfileUiState
import com.example.infoquizapp.presentation.profile.viewmodel.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    token: String,
    onExit : () -> Unit
) {

    LaunchedEffect(token) {
        viewModel.loadProfile(token)
    }

    val uiState by viewModel.uiState.collectAsState()

    when (uiState) {
        is ProfileUiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        is ProfileUiState.Success -> {

            val user = (uiState as ProfileUiState.Success).user

            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("") },
                        navigationIcon = {
                            IconButton(onClick = { onExit() }) {
                                Icon(imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                                    contentDescription = "Back")
                            }
                        },
                        actions = {
                            TextButton(onClick = { /* Handle edit action */ }) {
                                Text("Изменить", color = MaterialTheme.colorScheme.primary)
                            }
                        },
                    )
                }
            ) { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(horizontal = 16.dp)
                ) {
                    UserProfileSection(user.username)

                    Spacer(modifier = Modifier.height(16.dp))

                    TrophiesSection()
                }
            }
        }

        is ProfileUiState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = (uiState as ProfileUiState.Error).message,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
        ProfileUiState.Idle -> {}
    }
}
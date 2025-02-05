package com.example.infoquizapp.presentation.view.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.infoquizapp.presentation.view.component.profilescreencomponent.TrophiesSection
import com.example.infoquizapp.presentation.view.component.profilescreencomponent.UserProfileSection

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("") },
                navigationIcon = {
                    IconButton(onClick = { /* тут сделать навигацию назад */ }) {
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
            UserProfileSection()

            Spacer(modifier = Modifier.height(16.dp))

            TrophiesSection()
        }
    }
}
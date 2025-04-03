package com.example.infoquizapp.presentation.game.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.infoquizapp.Routes
import com.example.infoquizapp.presentation.main.view.mainscreencomponent.TabBarComp
import com.example.infoquizapp.utils.TokenManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameMainScreen(
    navController: NavController,
) {
    val context = LocalContext.current
    val token = TokenManager.getToken(context) ?: ""

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Играть",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(MaterialTheme.colorScheme.primary)
            )
        },
        bottomBar = {
            TabBarComp(
                navController = navController
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            GameCard(
                token = token,
                onGameScreen = {
                    navController.navigate(Routes.Game.createRoute(token))
                }
            )
        }
    }
}
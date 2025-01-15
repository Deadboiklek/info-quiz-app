package com.example.infoquizapp.view.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.example.infoquizapp.view.component.mainscreencomponent.TabBar
import com.example.infoquizapp.view.component.testscreencomponent.TestCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestScreen() {

    var selectedTab by remember { mutableStateOf(2) } // для TabBarComp

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Пробник", fontWeight = FontWeight.Bold) },
            )
        },
        bottomBar = {
            TabBar(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )
        }
    ) { paddingValues ->
        Column (
            modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
        ) {
            TestCard()
        }

    }
}
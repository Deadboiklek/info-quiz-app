package com.example.infoquizapp.presentation.view.component.classscreencomponent

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.infoquizapp.presentation.view.component.classscreencomponent.data.PracticeCardData

val practicies = listOf(
    PracticeCardData("Практика 1", 5),
    PracticeCardData("Практика 2", 3),
    PracticeCardData("Практика 3", 10)
)

@Composable
fun PracticeScreen() {
    Scaffold { paddingValues ->
        LazyColumn(
            contentPadding = paddingValues,
            modifier = Modifier.fillMaxSize()
        ) {
            items(practicies) { practice ->
                PracticeCard(practice = practice)
            }
        }
    }
}
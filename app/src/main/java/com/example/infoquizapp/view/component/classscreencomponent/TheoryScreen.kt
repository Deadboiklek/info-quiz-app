package com.example.infoquizapp.view.component.classscreencomponent

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.infoquizapp.view.component.classscreencomponent.data.TheoryCardData
import com.example.infoquizapp.view.component.lessonscreencomponent.LessonCard

val theories = listOf(
    TheoryCardData("1", "Раздел 1", 4),
    TheoryCardData("2", "Раздел 2", 1),
    TheoryCardData("3", "Раздел 3", 2)
)

@Composable
fun TheoryScreen() {
    Scaffold { paddingValues ->
        LazyColumn(
            contentPadding = paddingValues,
            modifier = Modifier.fillMaxSize()
        ) {
            items(theories) { theory ->
                TheoryCard(theory = theory)
            }
        }
    }
}
package com.example.infoquizapp.presentation.view.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.example.infoquizapp.presentation.view.component.lessonscreencomponent.LessonCard
import com.example.infoquizapp.presentation.view.component.lessonscreencomponent.data.Lesson
import com.example.infoquizapp.presentation.main.view.mainscreencomponent.TabBar
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults

// Список уроков для визуализации
val lessons = listOf(
    Lesson("Урок 1", "Вот такое вот крутое описание1", 0, 3, android.R.drawable.ic_menu_gallery),
    Lesson("Урок 2", "Вот такое вот крутое описание2", 0, 1, android.R.drawable.ic_menu_gallery),
    Lesson("Урок 3", "Вот такое вот крутое описание3", 0, 6, android.R.drawable.ic_menu_gallery)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonScreen() {

    var selectedTab by remember { mutableStateOf(1) } // для TabBarComp

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Уроки", fontWeight = FontWeight.Bold) }
            )
        },
        bottomBar = {
            TabBar(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            contentPadding = paddingValues,
            modifier = Modifier.fillMaxSize()
        ) {
            items(lessons) { lesson ->
                LessonCard(lesson = lesson)
            }
        }
    }
}
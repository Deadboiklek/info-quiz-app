package com.example.infoquizapp.presentation.main.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.infoquizapp.presentation.main.view.mainscreencomponent.AchievementsCard
import com.example.infoquizapp.presentation.main.view.mainscreencomponent.ActivityCard
import com.example.infoquizapp.presentation.main.view.mainscreencomponent.AppBar
import com.example.infoquizapp.presentation.main.view.mainscreencomponent.TabBar
import com.example.infoquizapp.presentation.main.view.mainscreencomponent.TaskCard
import com.example.infoquizapp.presentation.main.view.mainscreencomponent.UserProgressBar

@Composable
fun MainScreen(
    userName: String = "Имя Пользователя",
    userLevel: Int = 5,
    progress: Float = 0.7f      //тут надо всё это сделать, а не статичные данные
) {
    var selectedTab by remember { mutableStateOf(0) } // Для TabBar


    Scaffold(
        topBar = {
            AppBar(userName = userName)
        },
        bottomBar = {
            TabBar(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Прогресс пользователя
            UserProgressBar(userLevel = userLevel, progressProvider =  { progress })

            Spacer(modifier = Modifier.height(16.dp))

            // Карточка заданий
            TaskCard()

            Spacer(modifier = Modifier.height(16.dp))

            // Карточка достижений
            AchievementsCard()

            Spacer(modifier = Modifier.height(16.dp))

            // Карточка активности
            ActivityCard()
        }
    }
}
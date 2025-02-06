package com.example.infoquizapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.example.compose.AppTheme
import com.example.infoquizapp.view.component.questsscreencomponent.data.Quest
import com.example.infoquizapp.view.screen.TasksScreen


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme() {
                TasksApp()
            }
        }
    }
}

@Composable
fun TasksApp() {
    val quests = listOf(
        Quest(
            id = 1,
            title = "Пройти тест",
            description = "Пройдите тест на тему 'Основы информатики'.",
            image = painterResource(id = R.drawable.ic_launcher_background), // Убедитесь, что ресурс существует
            counter = 5
        ),
        Quest(
            id = 2,
            title = "Решить задачу",
            description = "Решите задачу по алгоритмам.",
            image = painterResource(id = R.drawable.ic_launcher_background) // Убедитесь, что ресурс существует
        ),
        Quest(
            id = 3,
            title = "Прочитать материал",
            description = "Изучите материал по структурам данных.",
            image = painterResource(id = R.drawable.ic_launcher_background) // Убедитесь, что ресурс существует
        )
    )

    TasksScreen(
        quests = quests,
        onClose = { println("Закрыть экран") }
    )
}
package com.example.infoquizapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.compose.AppTheme
import com.example.infoquizapp.view.component.lessonscreencomponent.LessonCard
import com.example.infoquizapp.view.component.lessonscreencomponent.data.Lesson
import com.example.infoquizapp.view.screen.LessonsScreen

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme() {
                LessonsScreen()
            }
        }
    }
}

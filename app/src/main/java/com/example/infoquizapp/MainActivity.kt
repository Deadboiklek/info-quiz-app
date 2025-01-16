package com.example.infoquizapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.compose.AppTheme
import com.example.infoquizapp.view.screen.LessonScreen
import com.example.infoquizapp.view.screen.LoginScreen
import com.example.infoquizapp.view.screen.MainScreen
import com.example.infoquizapp.view.screen.ProfileScreen
import com.example.infoquizapp.view.screen.TestScreen

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme() {

            }
        }
    }
}

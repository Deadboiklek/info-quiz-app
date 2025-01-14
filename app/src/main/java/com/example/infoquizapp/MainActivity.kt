package com.example.infoquizapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.compose.AppTheme
import com.example.infoquizapp.view.component.mainscreencomponent.ActivityCard
import com.example.infoquizapp.view.component.mainscreencomponent.TabBar
import com.example.infoquizapp.view.component.profilescreencomponent.TrophiesSection
import com.example.infoquizapp.view.component.profilescreencomponent.UserProfileSection
import com.example.infoquizapp.view.screen.HomeScreen

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

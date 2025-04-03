package com.example.infoquizapp.presentation.main.view.mainscreencomponent

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Gamepad
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.infoquizapp.Routes

@Composable
fun TabBarComp(navController: NavController) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        tonalElevation = 8.dp,
    ) {


        val backStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = backStackEntry?.destination?.route
        val token = backStackEntry?.arguments?.getString("token") ?: ""

        NavBarItems.BarItems.forEach { navItem ->
            NavigationBarItem(
                selected = currentRoute?.startsWith(navItem.route.substringBefore("/{")) == true,
                onClick = {
                    val routeWithToken = when (navItem.route) {
                        Routes.Main.route -> Routes.Main.createRoute(token)
                        Routes.Lesson.route -> Routes.Lesson.createRoute(token)
                        Routes.Trial.route -> Routes.Trial.createRoute(token)
                        Routes.Game.route -> Routes.GameMainScreen.createRoute(token)
                        else -> navItem.route
                    }
                    navController.navigate(routeWithToken) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { Icon(imageVector = navItem.icon, contentDescription = navItem.title) },
                label = { Text(text = navItem.title) },
                alwaysShowLabel = true
            )
        }
    }
}

data class BarItem(val title: String, val icon: ImageVector, val route: String)

object NavBarItems {
    val BarItems = listOf(
        BarItem(
            title = "Главная",
            icon = Icons.Outlined.Home,
            route = Routes.Main.route
        ),
        BarItem(
            title = "Уроки",
            icon = Icons.Outlined.CheckCircle,
            route = Routes.Lesson.route
        ),
        BarItem(
            title = "Пробник",
            icon = Icons.Outlined.DateRange,
            route = Routes.Trial.route
        ),
        BarItem(
            title = "Играть",
            icon = Icons.Outlined.Gamepad,
            route = Routes.GameMainScreen.route
        )
    )
}

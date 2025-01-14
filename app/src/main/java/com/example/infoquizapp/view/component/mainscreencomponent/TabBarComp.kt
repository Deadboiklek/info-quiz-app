package com.example.infoquizapp.view.component.mainscreencomponent

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun TabBar(selectedTab: Int, onTabSelected: (Int) -> Unit) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        tonalElevation = 8.dp
    ) {
        val tabs = listOf(
            TabItem("Главная", Icons.Outlined.Home),
            TabItem("Уроки", Icons.Outlined.CheckCircle),
            TabItem("Разделы", Icons.Outlined.DateRange)
        )

        tabs.forEachIndexed { index, tab ->
            NavigationBarItem(
                selected = selectedTab == index,
                onClick = { onTabSelected(index) },
                icon = {
                    Icon(
                        imageVector = tab.icon,
                        contentDescription = tab.title,
                        tint = if (selectedTab == index) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                    )
                },
                label = {
                    Text(
                        text = tab.title,
                        color = if (selectedTab == index) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                    )
                },
                alwaysShowLabel = true
            )
        }
    }
}

data class TabItem(val title: String, val icon: ImageVector)

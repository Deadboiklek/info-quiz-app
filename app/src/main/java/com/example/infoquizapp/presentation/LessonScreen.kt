package com.example.infoquizapp.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.infoquizapp.presentation.main.view.mainscreencomponent.TabBarComp
import com.example.infoquizapp.presentation.practice.view.PracticeScreen
import com.example.infoquizapp.presentation.practice.viewmodel.PracticeViewModel
import com.example.infoquizapp.presentation.theory.view.TheoryScreen
import com.example.infoquizapp.presentation.theory.viewmodel.TheoryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonScreen(
    theoryViewModel: TheoryViewModel,
    practiceViewModel: PracticeViewModel,
    onTheoryCardClick: (Int) -> Unit,
    onPracticeCardClick: (String) -> Unit,
    navController: NavController
) {

    var selectedTabIndex by remember { mutableIntStateOf(0) } // для TabRow

    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Уроки",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(MaterialTheme.colorScheme.primary)
            )
        },
        bottomBar = {
            TabBarComp(
                navController = navController
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            TabRow(selectedTabIndex = selectedTabIndex) {
                Tab(selected = selectedTabIndex == 0, onClick = { selectedTabIndex = 0 }) {
                    Text("Теория", modifier = Modifier.padding(16.dp))
                }
                Tab(selected = selectedTabIndex == 1, onClick = { selectedTabIndex = 1 }) {
                    Text("Практика", modifier = Modifier.padding(16.dp))
                }
            }

            when (selectedTabIndex) {
                0 -> TheoryScreen(
                    viewModel = theoryViewModel,
                    onTheoryCardClick = onTheoryCardClick
                )
                1 -> PracticeScreen(
                    viewModel = practiceViewModel,
                    onPracticeCardClick = onPracticeCardClick
                )
            }
        }
    }
}
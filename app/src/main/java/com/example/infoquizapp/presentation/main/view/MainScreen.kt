package com.example.infoquizapp.presentation.main.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.infoquizapp.R
import com.example.infoquizapp.Routes
import com.example.infoquizapp.presentation.achievement.viewmodel.AchievementsViewModel
import com.example.infoquizapp.presentation.achievementnotifier.GlobalAchievementNotifier
import com.example.infoquizapp.presentation.main.view.mainscreencomponent.AchievementsCard
import com.example.infoquizapp.presentation.main.view.mainscreencomponent.AppBar
import com.example.infoquizapp.presentation.main.view.mainscreencomponent.LeaderboardCard
import com.example.infoquizapp.presentation.main.view.mainscreencomponent.QuestCard
import com.example.infoquizapp.presentation.main.view.mainscreencomponent.TabBarComp
import com.example.infoquizapp.presentation.main.view.mainscreencomponent.UserProgressBar
import com.example.infoquizapp.presentation.main.view.mainscreencomponent.tutorialpager.TutorialPage
import com.example.infoquizapp.presentation.main.view.mainscreencomponent.tutorialpager.TutorialPager
import com.example.infoquizapp.presentation.main.viewmodel.MainUiState
import com.example.infoquizapp.presentation.main.viewmodel.MainViewModel
import com.example.infoquizapp.utils.TokenManager

@Composable
fun MainScreen(
    mainViewModel: MainViewModel,
    achievementsViewModel: AchievementsViewModel,
    onProfileClick : (token: String) -> Unit,
    onAchievementClick : (token: String) -> Unit,
    navController: NavController
) {

    val context = LocalContext.current
    val token = TokenManager.getToken(context) ?: ""

    val showTutorial by mainViewModel.showTutorialFlow.collectAsState()

    LaunchedEffect(token) {
        mainViewModel.loadProfile(token)
        achievementsViewModel.loadAllAchievements()
        achievementsViewModel.loadUserAchievements(token)
    }

    if (showTutorial) {
        val pages = listOf(
            TutorialPage(R.drawable.image_pager1, "Добро пожаловать!\n\nЗдесь вы можете обучаться информатике в игровом формате!"),
            TutorialPage(R.drawable.image_page2, "Решайте задачи, получайте опыт и увеличивайте уровень!"),
            TutorialPage(R.drawable.image_page3, "Открывайте достижения, выполняйте увлекательные задания!"),
            TutorialPage(R.drawable.image_page4, "Играйте в захватывающую мини-игру, отражайте нападение космических монстров с помощью знаний по информатике!\nУдачи!")
        )

        TutorialPager(pages) {
            mainViewModel.markTutorialShown()
        }

    } else {

        val uiState by mainViewModel.uiState.collectAsState()

        when(uiState) {
            is MainUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is MainUiState.Success -> {

                val user = (uiState as MainUiState.Success).user

                Scaffold(
                    topBar = {
                        AppBar(user = user, onProfileClick = onProfileClick, token = token)
                    },
                    bottomBar = {
                        TabBarComp(navController = navController)
                    },
                    contentColor = MaterialTheme.colorScheme.background
                ) { paddingValues ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    ) {

                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            UserProgressBar(user = user)

                            Spacer(modifier = Modifier.height(16.dp))

                            LeaderboardCard(
                                onLeaderboardCardClick = { navController.navigate(Routes.LeaderBoardScreen.route) }
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            QuestCard(
                                onQuestClick = {
                                    navController.navigate(Routes.Quest.createRoute(token))
                                }
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            AchievementsCard(
                                onAchievementClick = onAchievementClick,
                                token = token
                            )
                        }

                        GlobalAchievementNotifier(
                            viewModel = achievementsViewModel,
                            modifier = Modifier
                                .fillMaxWidth()
                                .zIndex(1f)
                        )
                    }
                }
            }
            is MainUiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = (uiState as MainUiState.Error).message,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
            MainUiState.Idle -> {}
        }
    }
}
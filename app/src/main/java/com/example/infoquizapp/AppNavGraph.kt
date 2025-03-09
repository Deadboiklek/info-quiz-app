package com.example.infoquizapp

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.infoquizapp.presentation.achievement.view.AchievementsScreen
import com.example.infoquizapp.presentation.achievement.viewmodel.AchievementsViewModel
import com.example.infoquizapp.presentation.auth.view.LoginScreen
import com.example.infoquizapp.presentation.auth.view.SignUpScreen
import com.example.infoquizapp.presentation.auth.viewmodel.AuthViewModel
import com.example.infoquizapp.presentation.main.view.MainScreen
import com.example.infoquizapp.presentation.main.viewmodel.MainViewModel
import com.example.infoquizapp.presentation.profile.view.ProfileScreen
import com.example.infoquizapp.presentation.profile.viewmodel.ProfileViewModel
import com.example.infoquizapp.presentation.quest.view.QuestScreen
import com.example.infoquizapp.presentation.quest.viewmodel.UserQuestsViewModel
import org.kodein.di.DI
import org.kodein.di.instance

sealed class Routes(val route: String) {
    object Login : Routes("login")
    object SignUp : Routes("signup")
    object Main : Routes("main/{token}") {
        fun createRoute(token: String): String = "main/$token"
    }
    object Profile : Routes("profile/{token}") {
        fun createRoute(token: String): String = "profile/$token"
    }
    object Achievements : Routes("achievements/{token}") {
        fun createRoute(token: String): String = "achievements/$token"
    }
    object Quest : Routes("quest/{token}") {
        fun createRoute(token: String): String = "quest/$token"
    }
}

@Composable
fun AppNavGraph(
    di: DI
) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.Login.route) {
        composable(Routes.Login.route) {
            val authViewModel: AuthViewModel by di.instance()
            LoginScreen(
                viewModel = authViewModel,
                onLoginSuccess = { token ->
                    navController.navigate(Routes.Main.createRoute(token)) {
                        popUpTo(Routes.Login.route) { inclusive = true }
                    }
                },
                onSignUpClick = { navController.navigate(Routes.SignUp.route) }
            )
        }

        composable(Routes.SignUp.route) {
            val authViewModel: AuthViewModel by di.instance()
            SignUpScreen(
                viewModel = authViewModel,
                onRegisterSuccess = { token ->
                    navController.navigate(Routes.Main.createRoute(token)) {
                        popUpTo(Routes.SignUp.route) { inclusive = true }
                    }
                },
                onLoginClick = { navController.navigate(Routes.Login.route) }
            )
        }

        composable(
            route = Routes.Main.route,
            arguments = listOf(navArgument("token") { type = NavType.StringType })
        ) { backStackEntry ->
            val token = backStackEntry.arguments?.getString("token") ?: ""
            val mainViewModel: MainViewModel by di.instance()
            MainScreen(
                viewModel = mainViewModel,
                token = token,
                progress = 0.7f,
                onProfileClick = { navController.navigate(Routes.Profile.createRoute(token)) },
                onAchievementClick = { navController.navigate(Routes.Achievements.createRoute(token)) },
                onQuestClick = { navController.navigate(Routes.Quest.createRoute(token)) },
                navController = navController
            )
        }

        composable(
            route = Routes.Profile.route,
            arguments = listOf(navArgument("token") { type = NavType.StringType })
        ) { backStackEntry ->
            val token = backStackEntry.arguments?.getString("token") ?: ""
            val profileViewModel: ProfileViewModel by di.instance()
            ProfileScreen(
                viewModel = profileViewModel,
                token = token,
                onExit = { navController.navigateUp() }
            )
        }

        composable(
            route = Routes.Achievements.route,
            arguments = listOf(navArgument("token") { type = NavType.StringType })
        ) { backStackEntry ->
            val token = backStackEntry.arguments?.getString("token") ?: ""
            val achievementsViewModel: AchievementsViewModel by di.instance()
            AchievementsScreen(
                viewModel = achievementsViewModel,
                token = token,
                onExit = { navController.navigateUp() }
            )
        }

        composable(
            route = Routes.Quest.route,
            arguments = listOf(navArgument("token") { type = NavType.StringType })
        ) { backStackEntry ->
            val token = backStackEntry.arguments?.getString("token") ?: ""
            val questViewModel : UserQuestsViewModel by di.instance()
            QuestScreen(
                viewModel = questViewModel,
                token = token,
                onExit = { navController.navigateUp() }
            )
        }
    }
}
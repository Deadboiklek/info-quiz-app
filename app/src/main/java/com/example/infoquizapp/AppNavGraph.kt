package com.example.infoquizapp

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.infoquizapp.presentation.LessonScreen
import com.example.infoquizapp.presentation.achievement.view.AchievementsScreen
import com.example.infoquizapp.presentation.achievement.viewmodel.AchievementsViewModel
import com.example.infoquizapp.presentation.auth.view.LoginScreen
import com.example.infoquizapp.presentation.auth.view.SignUpScreen
import com.example.infoquizapp.presentation.auth.view.TeacherLoginScreen
import com.example.infoquizapp.presentation.auth.viewmodel.AuthViewModel
import com.example.infoquizapp.presentation.game.view.GameMainScreen
import com.example.infoquizapp.presentation.game.view.GameScreen
import com.example.infoquizapp.presentation.game.viewmodel.GameViewModel
import com.example.infoquizapp.presentation.main.view.MainScreen
import com.example.infoquizapp.presentation.main.viewmodel.MainViewModel
import com.example.infoquizapp.presentation.practice.viewmodel.PracticeViewModel
import com.example.infoquizapp.presentation.profile.view.ProfileScreen
import com.example.infoquizapp.presentation.profile.viewmodel.ProfileViewModel
import com.example.infoquizapp.presentation.quest.view.QuestScreen
import com.example.infoquizapp.presentation.quest.viewmodel.UserQuestsViewModel
import com.example.infoquizapp.presentation.quiz.view.QuizTestScreen
import com.example.infoquizapp.presentation.quiz.view.TestResultScreen
import com.example.infoquizapp.presentation.quiz.viewmodel.QuizViewModel
import com.example.infoquizapp.presentation.teacher.view.addquiz.AddQuizScreen
import com.example.infoquizapp.presentation.teacher.view.TeacherMainScreen
import com.example.infoquizapp.presentation.teacher.view.checkanddeletequiz.GetAndDeleteQuizzesScreen
import com.example.infoquizapp.presentation.teacher.view.checkstatistics.StudentListScreen
import com.example.infoquizapp.presentation.teacher.view.checkstatistics.StudentStatisticsScreen
import com.example.infoquizapp.presentation.teacher.viewmodel.GetAndDeleteQuizViewModel
import com.example.infoquizapp.presentation.teacher.viewmodel.PostTeacherQuizViewModel
import com.example.infoquizapp.presentation.teacher.viewmodel.StudentListViewModel
import com.example.infoquizapp.presentation.teacher.viewmodel.StudentStatisticsViewModel
import com.example.infoquizapp.presentation.teacher.viewmodel.TeacherProfileViewModel
import com.example.infoquizapp.presentation.theory.view.TheoryContentScreen
import com.example.infoquizapp.presentation.theory.viewmodel.TheoryViewModel
import com.example.infoquizapp.presentation.trial.view.TrialScreen
import com.example.infoquizapp.presentation.trial.view.TrialTestScreen
import com.example.infoquizapp.presentation.trial.viewmodel.TrialViewModel
import org.kodein.di.DI
import org.kodein.di.instance

sealed class Routes(val route: String) {
    object Login : Routes("login")
    object SignUp : Routes("signup")
    object TeacherLogin : Routes("teacherlogin")

    object Main : Routes("main/{token}") {
        fun createRoute(token: String): String = "main/$token"
    }

    object TeacherMain : Routes("teachermain")
    object AddQuizScreen : Routes("addquizscreen")
    object GetAndDeleteQuizScreen : Routes("getanddeletequizscreen")
    object StudentsListScreen : Routes("studentslistscreen")
    object StudentStatisticsScreen : Routes("studentstatistics/{studentId}")

    object Profile : Routes("profile/{token}") {
        fun createRoute(token: String): String = "profile/$token"
    }
    object Achievements : Routes("achievements/{token}") {
        fun createRoute(token: String): String = "achievements/$token"
    }
    object Quest : Routes("quest/{token}") {
        fun createRoute(token: String): String = "quest/$token"
    }
    object Lesson : Routes("lesson/{token}") {
        fun createRoute(token: String): String = "lesson/$token"
    }
    object TheoryContent : Routes("theorycontent/{theoryId}/{token}") {
        fun createRoute(theoryId: Int, token: String): String = "theorycontent/$theoryId/$token"
    }
    object QuizTest : Routes("quiztest/{quizType}/{token}") {
        fun createRoute(quizType: String, token: String): String = "quiztest/$quizType/$token"
    }
    object Trial : Routes("trial/{token}") {
        fun createRoute(token: String): String = "trial/$token"
    }
    object TrialTest : Routes("trialtest/{token}") {
        fun createRoute(token: String): String = "trialtest/$token"
    }
    object TestResult : Routes("testresult/{token}") {
        fun createRoute(token: String): String = "testresult/$token"
    }
    object GameMainScreen : Routes("GameMainScreen/{token}") {
        fun createRoute(token: String): String = "GameMainScreen/$token"
    }
    object Game : Routes("game/{token}") {
        fun createRoute(token: String): String = "game/$token"
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
                onLoginSuccess = {
                    navController.navigate(Routes.Main.route) {
                        popUpTo(Routes.Login.route) { inclusive = true }
                    }
                },
                navController = navController
            )
        }

        composable(Routes.SignUp.route) {
            val authViewModel: AuthViewModel by di.instance()
            SignUpScreen(
                viewModel = authViewModel,
                onRegisterSuccess = {
                    navController.navigate(Routes.Main.route) {
                        popUpTo(Routes.SignUp.route) { inclusive = true }
                    }
                },
                onLoginClick = { navController.navigate(Routes.Login.route) }
            )
        }

        composable(Routes.TeacherLogin.route) {
            val authViewModel: AuthViewModel by di.instance()
            TeacherLoginScreen(
                viewModel = authViewModel,
                navController = navController,
                onLoginSuccess = {
                    navController.navigate(Routes.TeacherMain.route) {
                        popUpTo(Routes.TeacherLogin.route) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = Routes.Main.route,
        ) {
            val mainViewModel: MainViewModel by di.instance()
            MainScreen(
                viewModel = mainViewModel,
                progress = 0.7f,
                onProfileClick = { navController.navigate(Routes.Profile.route) },
                onAchievementClick = { navController.navigate(Routes.Achievements.route) },
                navController = navController
            )
        }

        composable(
            route = Routes.Profile.route,
        ) {
            val profileViewModel: ProfileViewModel by di.instance()
            val authViewModel:AuthViewModel by di.instance()
            ProfileScreen(
                viewModel = profileViewModel,
                onExit = { navController.navigateUp() },
                navController = navController,
                authViewModel = authViewModel
            )
        }

        composable(
            route = Routes.Achievements.route,
        ) {
            val achievementsViewModel: AchievementsViewModel by di.instance()
            AchievementsScreen(
                viewModel = achievementsViewModel,
                onExit = { navController.navigateUp() }
            )
        }

        composable(
            route = Routes.Quest.route,
        ) {
            val questViewModel : UserQuestsViewModel by di.instance()
            QuestScreen(
                viewModel = questViewModel,
                onExit = { navController.navigateUp() }
            )
        }

        composable(
            route = Routes.Lesson.route,
        ) {
            val theoryViewModel : TheoryViewModel by di.instance()
            val practiceViewModel: PracticeViewModel by di.instance()
            LessonScreen(
                theoryViewModel = theoryViewModel,
                practiceViewModel = practiceViewModel,
                navController = navController,
            )
        }

        composable(
            route = Routes.TheoryContent.route,
            arguments = listOf(
                navArgument("theoryId") { type = NavType.IntType },
            )
        ) { backStackEntry ->
            val theoryId = backStackEntry.arguments?.getInt("theoryId") ?: 0
            val theoryViewModel : TheoryViewModel by di.instance()
            TheoryContentScreen(
                viewModel = theoryViewModel,
                theoryId = theoryId,
                onBackClick = { navController.navigateUp() }
            )
        }

        composable(
            route = Routes.QuizTest.route,
            arguments = listOf(
                navArgument("quizType") { type = NavType.StringType },
            )
        ) { backStackEntry ->
            val quizType = backStackEntry.arguments?.getString("quizType") ?: ""
            val quizViewModel : QuizViewModel by di.instance()
            QuizTestScreen(
                viewModel = quizViewModel,
                quizType = quizType,
                navController = navController
            )
        }

        composable(
            route = Routes.Trial.route,
        ) {
            TrialScreen(
                navController = navController,
            )
        }

        composable(
            route = Routes.TrialTest.route,
        ) {
            val trialViewModel: TrialViewModel by di.instance()
            TrialTestScreen(
                viewModel = trialViewModel,
                onExit = { navController.navigateUp() }
            )
        }

        composable(
            route = Routes.TestResult.route
        ) {
            val quizViewModel: QuizViewModel by di.instance()
            TestResultScreen(
                navController = navController,
                viewModel = quizViewModel
            )
        }

        composable(
            route = Routes.GameMainScreen.route,
        ) {
            GameMainScreen(
                navController = navController,
            )
        }

        composable(
            route = Routes.Game.route
        ) {
            val gameViewModel: GameViewModel by di.instance()
            GameScreen(
                viewModel = gameViewModel,
                onExit = { navController.navigateUp() },
            )
        }

        composable(
            route = Routes.TeacherMain.route
        ) {
            val teacherProfileViewModel: TeacherProfileViewModel by di.instance()
            TeacherMainScreen(
                viewModel = teacherProfileViewModel,
                navController = navController
            )
        }

        composable(
            route = Routes.AddQuizScreen.route
        ) {
            val postTeacherQuizViewModel: PostTeacherQuizViewModel by di.instance()
            AddQuizScreen(
                viewModel = postTeacherQuizViewModel,
                onQuizAdded = { navController.navigateUp() },
                onBack = { navController.navigateUp() }
            )
        }

        composable(
            route = Routes.GetAndDeleteQuizScreen.route
        ) {
            val getAndDeleteQuizViewModel : GetAndDeleteQuizViewModel by di.instance()
            GetAndDeleteQuizzesScreen(
                viewModel = getAndDeleteQuizViewModel,
                onBack = { navController.navigateUp() }
            )
        }

        composable(
            route = "studentstatistics/{studentId}",
            arguments = listOf(navArgument("studentId") {
                type = NavType.IntType
            })
        ) { backStackEntry ->
            val studentId = backStackEntry.arguments?.getInt("studentId") ?: 0

            val studentStatisticsViewModel: StudentStatisticsViewModel by di.instance()

            StudentStatisticsScreen(
                studentId = studentId,
                navController = navController,
                viewModel = studentStatisticsViewModel
            )
        }

        composable(
            route = Routes.StudentsListScreen.route
        ) {
            val studentListViewModel : StudentListViewModel by di.instance()

            StudentListScreen(
                viewModel = studentListViewModel,
                navController = navController,
                onBack = { navController.navigateUp() }
            )
        }
    }
}
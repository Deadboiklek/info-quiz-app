package com.example.infoquizapp.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.room.Room
import com.example.infoquizapp.data.AppDatabase
import com.example.infoquizapp.data.achievement.network.ApiAchievementsService
import com.example.infoquizapp.data.achievement.repository.AchievementsRepositoryImpl
import com.example.infoquizapp.data.auth.network.ApiAuthService
import com.example.infoquizapp.data.auth.repository.AuthRepositoryImpl
import com.example.infoquizapp.data.PracticeDao
import com.example.infoquizapp.data.profile.network.ApiProfileService
import com.example.infoquizapp.data.profile.repository.ProfileRepositoryImpl
import com.example.infoquizapp.data.quest.repository.QuestRepositoryImpl
import com.example.infoquizapp.data.quiz.network.QuizApiService
import com.example.infoquizapp.data.quiz.repository.QuizRepositoryImpl
import com.example.infoquizapp.data.TheoryDao
import com.example.infoquizapp.data.gamequiz.network.ApiGameQuizService
import com.example.infoquizapp.data.gamequiz.repository.GameQuizRepositoryImpl
import com.example.infoquizapp.data.practice.repository.PracticeRepositoryImpl
import com.example.infoquizapp.data.quest.network.ApiQuestsService
import com.example.infoquizapp.data.teacher.network.TeacherApiService
import com.example.infoquizapp.data.teacher.repository.TeacherRepositoryImpl
import com.example.infoquizapp.data.theory.repository.TheoryRepositoryImpl
import com.example.infoquizapp.domain.achievement.repository.AchievementRepository
import com.example.infoquizapp.domain.achievement.usecases.GetAllAchievementsUseCase
import com.example.infoquizapp.domain.achievement.usecases.GetUserAchievementsUseCase
import com.example.infoquizapp.domain.auth.repository.AuthRepository
import com.example.infoquizapp.domain.auth.usecases.LoginUseCase
import com.example.infoquizapp.domain.auth.usecases.RegisterUseCase
import com.example.infoquizapp.domain.auth.usecases.TeacherLoginUseCase
import com.example.infoquizapp.domain.gamequiz.repository.GameQuizRepository
import com.example.infoquizapp.domain.gamequiz.usecases.CompleteGameQuizUseCase
import com.example.infoquizapp.domain.gamequiz.usecases.GetGameQuizUseCase
import com.example.infoquizapp.domain.practice.repository.PracticeRepository
import com.example.infoquizapp.domain.practice.usecases.GetAllPracticeUseCase
import com.example.infoquizapp.domain.practice.usecases.GetPracticeUseCase
import com.example.infoquizapp.domain.practice.usecases.MarkPracticeAsDoneUseCase
import com.example.infoquizapp.domain.profile.repository.ProfileRepository
import com.example.infoquizapp.domain.profile.usecases.GetProfileUseCase
import com.example.infoquizapp.domain.profile.usecases.GetStatisticsUseCase
import com.example.infoquizapp.domain.profile.usecases.UpdateProfileUseCase
import com.example.infoquizapp.domain.quest.repository.QuestRepository
import com.example.infoquizapp.domain.quest.usecases.CompleteQuestResult
import com.example.infoquizapp.domain.quest.usecases.CompleteQuestUseCase
import com.example.infoquizapp.domain.quest.usecases.GetAllQuestsUseCase
import com.example.infoquizapp.domain.quest.usecases.GetUserQuestsUseCase
import com.example.infoquizapp.domain.quiz.repository.QuizRepository
import com.example.infoquizapp.domain.quiz.usecases.GetTestQuizzesUseCase
import com.example.infoquizapp.domain.quiz.usecases.GetTrialTestUseCase
import com.example.infoquizapp.domain.quiz.usecases.SubmitAnswerUseCase
import com.example.infoquizapp.domain.teacher.repository.TeacherRepository
import com.example.infoquizapp.domain.teacher.usecases.DeleteTeacherQuizUseCase
import com.example.infoquizapp.domain.teacher.usecases.GetStudentStatisticsUseCase
import com.example.infoquizapp.domain.teacher.usecases.GetTeacherProfileUseCase
import com.example.infoquizapp.domain.teacher.usecases.GetTeacherQuizzesUseCase
import com.example.infoquizapp.domain.teacher.usecases.GetTeacherStudentsUseCase
import com.example.infoquizapp.domain.teacher.usecases.PostTeacherQuizUseCase
import com.example.infoquizapp.domain.teacher.usecases.UpdateTeacherQuizUseCase
import com.example.infoquizapp.domain.theory.repository.TheoryRepository
import com.example.infoquizapp.domain.theory.usecases.GetAllTheoryUseCase
import com.example.infoquizapp.domain.theory.usecases.GetTheoryUseCase
import com.example.infoquizapp.domain.theory.usecases.MarkTheoryAsReadUseCase
import com.example.infoquizapp.presentation.achievement.viewmodel.AchievementsViewModel
import com.example.infoquizapp.presentation.auth.viewmodel.AuthViewModel
import com.example.infoquizapp.presentation.game.viewmodel.GameViewModel
import com.example.infoquizapp.presentation.main.viewmodel.MainViewModel
import com.example.infoquizapp.presentation.practice.viewmodel.PracticeViewModel
import com.example.infoquizapp.presentation.profile.viewmodel.ProfileViewModel
import com.example.infoquizapp.presentation.profile.viewmodel.StatisticsViewModel
import com.example.infoquizapp.presentation.quest.viewmodel.QuestsViewModel
import com.example.infoquizapp.presentation.quiz.viewmodel.QuizViewModel
import com.example.infoquizapp.presentation.teacher.viewmodel.EditQuizViewModel
import com.example.infoquizapp.presentation.teacher.viewmodel.GetAndDeleteQuizViewModel
import com.example.infoquizapp.presentation.teacher.viewmodel.PostTeacherQuizViewModel
import com.example.infoquizapp.presentation.teacher.viewmodel.StudentListViewModel
import com.example.infoquizapp.presentation.teacher.viewmodel.StudentStatisticsViewModel
import com.example.infoquizapp.presentation.teacher.viewmodel.TeacherProfileViewModel
import com.example.infoquizapp.presentation.theory.viewmodel.TheoryViewModel
import com.example.infoquizapp.presentation.trial.viewmodel.TrialViewModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider
import org.kodein.di.singleton

val appModule = DI.Module("appModule") {

    //ktor client
    bind<HttpClient>() with singleton {
        HttpClient(CIO) {
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.ALL
            }
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                })
            }
        }
    }

    bind<AppDatabase>() with singleton {
        Room.databaseBuilder(instance(), AppDatabase::class.java, "app_database")
            .fallbackToDestructiveMigration()
            .build()
    }

    

    bind<DataStore<Preferences>>() with singleton {
        val ctx: Context = instance()
        ctx.dataStore
    }

    bind<TheoryDao>() with singleton { instance<AppDatabase>().theoryDao() }
    bind<PracticeDao>() with singleton { instance<AppDatabase>().practiceDao() }

    //базовый юрл
    bind<String>("baseUrl") with singleton { "http://10.0.2.2:8000" }

    //http://10.0.2.2:8000

    //работа с сетью
    bind<ApiAuthService>() with singleton {
        ApiAuthService(instance(), instance("baseUrl"))
    }
    bind<ApiProfileService>() with singleton {
        ApiProfileService(instance(), instance("baseUrl"))
    }
    bind<ApiAchievementsService>() with singleton {
        ApiAchievementsService(instance(), instance("baseUrl"))
    }
    bind<ApiQuestsService>() with singleton {
        ApiQuestsService(instance(), instance("baseUrl"))
    }
    bind<QuizApiService>() with singleton {
        QuizApiService(instance(), instance("baseUrl"))
    }
    bind<ApiGameQuizService>() with singleton {
        ApiGameQuizService(instance(), instance("baseUrl"))
    }
    bind<TeacherApiService>() with singleton {
        TeacherApiService(instance(), instance("baseUrl"))
    }

    // репозитории
    // auth
    bind<AuthRepository>() with singleton { AuthRepositoryImpl(instance()) }
    //profile
    bind<ProfileRepository>() with singleton { ProfileRepositoryImpl(instance()) }
    //achievement
    bind<AchievementRepository>() with singleton { AchievementsRepositoryImpl(instance()) }
    //quest
    bind<QuestRepository>() with singleton { QuestRepositoryImpl(instance()) }
    //theory
    bind<TheoryRepository>() with singleton { TheoryRepositoryImpl(instance()) }
    //quiz
    bind<QuizRepository>() with singleton { QuizRepositoryImpl(instance()) }
    //practice
    bind<PracticeRepository>() with singleton { PracticeRepositoryImpl(instance()) }
    //game
    bind<GameQuizRepository>() with singleton { GameQuizRepositoryImpl(instance()) }
    //teacher
    bind<TeacherRepository>() with singleton { TeacherRepositoryImpl(instance()) }

    //usecases
    // auth
    bind<RegisterUseCase>() with singleton { RegisterUseCase(instance()) }
    bind<LoginUseCase>() with singleton { LoginUseCase(instance()) }
    bind<TeacherLoginUseCase>() with singleton { TeacherLoginUseCase(instance()) }
    //profile
    bind<GetProfileUseCase>() with singleton { GetProfileUseCase(instance()) }
    bind<GetStatisticsUseCase>() with singleton { GetStatisticsUseCase(instance()) }
    bind<UpdateProfileUseCase>() with singleton { UpdateProfileUseCase(instance()) }
    //achievement
    bind<GetAllAchievementsUseCase>() with singleton { GetAllAchievementsUseCase(instance()) }
    bind<GetUserAchievementsUseCase>() with singleton { GetUserAchievementsUseCase(instance()) }
    //quest
    bind<GetUserQuestsUseCase>() with singleton { GetUserQuestsUseCase(instance()) }
    bind<CompleteQuestUseCase>() with singleton { CompleteQuestUseCase(instance()) }
    bind<GetAllQuestsUseCase>() with singleton { GetAllQuestsUseCase(instance()) }
    //theory
    bind<GetTheoryUseCase>() with singleton { GetTheoryUseCase(instance()) }
    bind<MarkTheoryAsReadUseCase>() with singleton { MarkTheoryAsReadUseCase(instance()) }
    bind<GetAllTheoryUseCase>() with singleton { GetAllTheoryUseCase(instance()) }
    //quiz
    bind<GetTestQuizzesUseCase>() with singleton { GetTestQuizzesUseCase(instance()) }
    bind<SubmitAnswerUseCase>() with singleton { SubmitAnswerUseCase(instance()) }
    bind<GetTrialTestUseCase>() with singleton { GetTrialTestUseCase(instance()) }
    //practice
    bind<GetAllPracticeUseCase>() with singleton { GetAllPracticeUseCase(instance()) }
    bind<GetPracticeUseCase>() with singleton { GetPracticeUseCase(instance()) }
    bind<MarkPracticeAsDoneUseCase>() with singleton { MarkPracticeAsDoneUseCase(instance()) }
    //game
    bind<GetGameQuizUseCase>() with singleton { GetGameQuizUseCase(instance()) }
    bind<CompleteGameQuizUseCase>() with singleton { CompleteGameQuizUseCase(instance()) }
    //teacher
    bind<GetTeacherProfileUseCase>() with singleton{ GetTeacherProfileUseCase(instance()) }
    bind<PostTeacherQuizUseCase>() with singleton { PostTeacherQuizUseCase(instance()) }
    bind<GetTeacherQuizzesUseCase>() with singleton { GetTeacherQuizzesUseCase(instance()) }
    bind<DeleteTeacherQuizUseCase>() with singleton { DeleteTeacherQuizUseCase(instance()) }
    bind<GetTeacherStudentsUseCase>() with singleton { GetTeacherStudentsUseCase(instance()) }
    bind<GetStudentStatisticsUseCase>() with singleton { GetStudentStatisticsUseCase(instance()) }
    bind<UpdateTeacherQuizUseCase>() with singleton { UpdateTeacherQuizUseCase(instance()) }

    //viewmodels
    // auth
    bind<AuthViewModel>() with singleton { AuthViewModel(instance(), instance(), instance()) }
    //profile
    bind<ProfileViewModel>() with singleton { ProfileViewModel(instance(), instance()) }
    bind<StatisticsViewModel>() with singleton { StatisticsViewModel(instance()) }
    //achievement
    bind<AchievementsViewModel>() with singleton { AchievementsViewModel(instance(), instance()) }
    //quest
    bind<QuestsViewModel>() with singleton { QuestsViewModel(instance(), instance(), instance()) }
    //theory
    bind<TheoryViewModel>() with singleton { TheoryViewModel(instance(), instance(), instance()) }
    //quiz
    bind<QuizViewModel>() with singleton { QuizViewModel(instance(), instance()) }
    //main
    bind<MainViewModel>() with singleton { MainViewModel(instance(), instance()) }
    //trial
    bind<TrialViewModel>() with singleton { TrialViewModel(instance(), instance()) }
    //practice
    bind<PracticeViewModel>() with singleton { PracticeViewModel(instance(), instance(), instance()) }
    //game
    bind<GameViewModel>() with singleton { GameViewModel(instance(), instance()) }
    //teacherprofile
    bind<TeacherProfileViewModel>() with singleton { TeacherProfileViewModel(instance()) }
    //postteqcherquest
    bind<PostTeacherQuizViewModel>() with singleton { PostTeacherQuizViewModel(instance()) }
    //getanddeletequiz
    bind<GetAndDeleteQuizViewModel>() with singleton { GetAndDeleteQuizViewModel(instance(), instance()) }
    //getteacherstudents
    bind<StudentListViewModel>() with singleton { StudentListViewModel(instance()) }
    //getstudentstatistics
    bind<StudentStatisticsViewModel>() with singleton { StudentStatisticsViewModel(instance()) }
    //updateteacherquiz
    bind<EditQuizViewModel>() with singleton { EditQuizViewModel(instance(), instance()) }
}
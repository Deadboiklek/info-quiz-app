package com.example.infoquizapp.di

import com.example.infoquizapp.data.achievement.network.ApiAchievementsService
import com.example.infoquizapp.data.achievement.repository.AchievementsRepositoryImpl
import com.example.infoquizapp.data.auth.network.ApiAuthService
import com.example.infoquizapp.data.auth.repository.AuthRepositoryImpl
import com.example.infoquizapp.data.profile.network.ApiProfileService
import com.example.infoquizapp.data.profile.repository.ProfileRepositoryImpl
import com.example.infoquizapp.data.quest.network.ApiQuestService
import com.example.infoquizapp.data.quest.repository.QuestRepositoryImpl
import com.example.infoquizapp.data.quiz.network.QuizApiService
import com.example.infoquizapp.data.quiz.repository.QuizRepositoryImpl
import com.example.infoquizapp.data.theory.repository.TheoryRepositoryImpl
import com.example.infoquizapp.domain.achievement.repository.AchievementRepository
import com.example.infoquizapp.domain.achievement.usecases.GetAllAchievementsUseCase
import com.example.infoquizapp.domain.achievement.usecases.GetUserAchievementsUseCase
import com.example.infoquizapp.domain.auth.repository.AuthRepository
import com.example.infoquizapp.domain.auth.usecases.LoginUseCase
import com.example.infoquizapp.domain.auth.usecases.RegisterUseCase
import com.example.infoquizapp.domain.profile.repository.ProfileRepository
import com.example.infoquizapp.domain.profile.usecases.GetProfileUseCase
import com.example.infoquizapp.domain.quest.repository.QuestRepository
import com.example.infoquizapp.domain.quest.usecases.CompleteQuestResult
import com.example.infoquizapp.domain.quest.usecases.GetUserQuestsUseCase
import com.example.infoquizapp.domain.quiz.repository.QuizRepository
import com.example.infoquizapp.domain.quiz.usecases.GetTestQuizzesResult
import com.example.infoquizapp.domain.quiz.usecases.GetTestQuizzesUseCase
import com.example.infoquizapp.domain.quiz.usecases.GetTrialTestUseCase
import com.example.infoquizapp.domain.quiz.usecases.SubmitAnswerUseCase
import com.example.infoquizapp.domain.theory.repository.TheoryRepository
import com.example.infoquizapp.domain.theory.usecases.GetTheoryUseCase
import com.example.infoquizapp.domain.theory.usecases.MarkTheoryAsReadUseCase
import com.example.infoquizapp.presentation.achievement.viewmodel.AchievementsViewModel
import com.example.infoquizapp.presentation.auth.viewmodel.AuthViewModel
import com.example.infoquizapp.presentation.main.viewmodel.MainViewModel
import com.example.infoquizapp.presentation.profile.viewmodel.ProfileViewModel
import com.example.infoquizapp.presentation.quest.viewmodel.UserQuestsViewModel
import com.example.infoquizapp.presentation.quiz.viewmodel.QuizViewModel
import com.example.infoquizapp.presentation.theory.viewmodel.TheoryViewModel
import com.example.infoquizapp.presentation.trial.viewmodel.TrialViewModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

val appModule = DI.Module("appModule") {

    //ktor client
    bind<HttpClient>() with singleton { HttpClient(CIO) }

    //базовый юрл
    bind<String>("baseUrl") with singleton { "http://10.0.2.2:8000" }

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
    bind<ApiQuestService>() with singleton {
        ApiQuestService(instance(), instance("baseUrl"))
    }
    bind<QuizApiService>() with singleton {
        QuizApiService(instance(), instance("baseUrl"))
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


    //usecases
    // auth
    bind<RegisterUseCase>() with singleton { RegisterUseCase(instance()) }
    bind<LoginUseCase>() with singleton { LoginUseCase(instance()) }
    //profile
    bind<GetProfileUseCase>() with singleton { GetProfileUseCase(instance()) }
    //achievement
    bind<GetAllAchievementsUseCase>() with singleton { GetAllAchievementsUseCase(instance()) }
    bind<GetUserAchievementsUseCase>() with singleton { GetUserAchievementsUseCase(instance()) }
    //quest
    bind<GetUserQuestsUseCase>() with singleton { GetUserQuestsUseCase(instance()) }
    bind<CompleteQuestResult>() with singleton { CompleteQuestResult(instance(), instance()) }
    //theory
    bind<GetTheoryUseCase>() with singleton { GetTheoryUseCase(instance()) }
    bind<MarkTheoryAsReadUseCase>() with singleton { MarkTheoryAsReadUseCase(instance()) }
    //quiz
    bind<GetTestQuizzesUseCase>() with singleton { GetTestQuizzesUseCase(instance()) }
    bind<SubmitAnswerUseCase>() with singleton { SubmitAnswerUseCase(instance()) }
    bind<GetTrialTestUseCase>() with singleton { GetTrialTestUseCase(instance()) }

    //viewmodels
    // auth
    bind<AuthViewModel>() with singleton { AuthViewModel(instance(), instance()) }
    //profile
    bind<ProfileViewModel>() with singleton { ProfileViewModel(instance()) }
    //achievement
    bind<AchievementsViewModel>() with singleton { AchievementsViewModel(instance(), instance()) }
    //quest
    bind<UserQuestsViewModel>() with singleton { UserQuestsViewModel(instance(), instance()) }
    //theory
    bind<TheoryViewModel>() with singleton { TheoryViewModel(instance(), instance(), instance()) }
    //quiz
    bind<QuizViewModel>() with singleton { QuizViewModel(instance(), instance()) }
    //main
    bind<MainViewModel>() with singleton { MainViewModel(instance()) }
    //trial
    bind<TrialViewModel>() with singleton { TrialViewModel(instance(), instance()) }
}
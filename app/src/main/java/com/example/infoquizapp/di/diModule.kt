package com.example.infoquizapp.di

import com.example.infoquizapp.data.achievement.network.ApiAchievementsService
import com.example.infoquizapp.data.achievement.repository.AchievementsRepositoryImpl
import com.example.infoquizapp.data.auth.network.ApiAuthService
import com.example.infoquizapp.data.auth.repository.AuthRepositoryImpl
import com.example.infoquizapp.data.profile.network.ApiProfileService
import com.example.infoquizapp.data.profile.repository.ProfileRepositoryImpl
import com.example.infoquizapp.domain.achievement.repository.AchievementRepository
import com.example.infoquizapp.domain.achievement.usecases.GetAllAchievementsUseCase
import com.example.infoquizapp.domain.achievement.usecases.GetUserAchievementsUseCase
import com.example.infoquizapp.domain.auth.repository.AuthRepository
import com.example.infoquizapp.domain.auth.usecases.LoginUseCase
import com.example.infoquizapp.domain.auth.usecases.RegisterUseCase
import com.example.infoquizapp.domain.profile.repository.ProfileRepository
import com.example.infoquizapp.domain.profile.usecases.GetProfileUseCase
import com.example.infoquizapp.presentation.achievement.viewmodel.AchievementsViewModel
import com.example.infoquizapp.presentation.auth.viewmodel.AuthViewModel
import com.example.infoquizapp.presentation.profile.viewmodel.ProfileViewModel
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
    bind<String>("baseUrl") with singleton { "тут юрл" }

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

    // репозитории
    bind<AuthRepository>() with singleton { AuthRepositoryImpl(instance()) }
    bind<ProfileRepository>() with singleton { ProfileRepositoryImpl(instance()) }
    bind<AchievementRepository>() with singleton { AchievementsRepositoryImpl(instance()) }

    //usecases
    bind<RegisterUseCase>() with singleton { RegisterUseCase(instance()) }
    bind<LoginUseCase>() with singleton { LoginUseCase(instance()) }
    bind<GetProfileUseCase>() with singleton { GetProfileUseCase(instance()) }
    bind<GetAllAchievementsUseCase>() with singleton { GetAllAchievementsUseCase(instance()) }
    bind<GetUserAchievementsUseCase>() with singleton { GetUserAchievementsUseCase(instance()) }


    //viewmodels
    bind<AuthViewModel>() with singleton { AuthViewModel(instance(), instance()) }
    bind<ProfileViewModel>() with singleton { ProfileViewModel(instance()) }
    bind<AchievementsViewModel>() with singleton { AchievementsViewModel(instance(), instance()) }

}
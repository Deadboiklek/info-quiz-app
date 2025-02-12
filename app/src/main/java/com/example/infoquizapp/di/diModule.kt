package com.example.infoquizapp.di

import com.example.infoquizapp.data.auth.network.ApiAuthService
import com.example.infoquizapp.data.auth.repository.AuthRepositoryImpl
import com.example.infoquizapp.domain.auth.repository.AuthRepository
import com.example.infoquizapp.domain.auth.usecases.LoginUseCase
import com.example.infoquizapp.domain.auth.usecases.RegisterUseCase
import com.example.infoquizapp.presentation.auth.viewmodel.AuthViewModel
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

    bind<ApiAuthService>() with singleton {
        ApiAuthService(instance(), instance("baseUrl"))
    }

    bind<AuthRepository>() with singleton { AuthRepositoryImpl(instance()) }

    bind<RegisterUseCase>() with singleton { RegisterUseCase(instance()) }
    bind<LoginUseCase>() with singleton { LoginUseCase(instance()) }

    bind<AuthViewModel>() with singleton { AuthViewModel(instance(), instance()) }
}
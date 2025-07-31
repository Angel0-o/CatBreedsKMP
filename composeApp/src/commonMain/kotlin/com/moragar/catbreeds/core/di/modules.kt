package com.moragar.catbreeds.core.di

import com.moragar.catbreeds.catbreed.data.remote.CatApi
import com.moragar.catbreeds.catbreed.data.remote.CatApiImpl
import com.moragar.catbreeds.catbreed.data.remote.KtorClientFactory.createApiClient
import com.moragar.catbreeds.catbreed.data.repository.CatRepositoryImpl
import com.moragar.catbreeds.catbreed.data.usecase.GetCatBreedsUseCaseImpl
import com.moragar.catbreeds.catbreed.data.usecase.ReadFavoriteBreedsUseCaseImpl
import com.moragar.catbreeds.catbreed.data.usecase.ToggleFavoriteUseCaseImpl
import com.moragar.catbreeds.catbreed.domain.repository.CatRepository
import com.moragar.catbreeds.catbreed.domain.usecase.GetCatBreedsUseCase
import com.moragar.catbreeds.catbreed.domain.usecase.ReadFavoriteBreedsUseCase
import com.moragar.catbreeds.catbreed.domain.usecase.ToggleFavoriteUseCase
import com.moragar.catbreeds.catbreed.presentation.catBreed.CatBreedsViewModel
import com.moragar.catbreeds.core.data.database.CatDatabaseFactoryImpl
import com.moragar.catbreeds.core.data.usecase.ClearCachedBreedsUseCaseImpl
import com.moragar.catbreeds.core.data.usecase.IsOnlineUseCaseImpl
import com.moragar.catbreeds.core.data.usecase.ReadAllCachedBreedsUseCaseImpl
import com.moragar.catbreeds.core.data.usecase.SaveCachedBreedUseCaseImpl
import com.moragar.catbreeds.core.domain.usecase.ClearCachedBreedsUseCase
import com.moragar.catbreeds.core.domain.usecase.IsOnlineUseCase
import com.moragar.catbreeds.core.domain.usecase.ReadAllCachedBreedsUseCase
import com.moragar.catbreeds.core.domain.usecase.SaveCachedBreedUseCase
import com.moragar.catbreeds.login.data.auth.CatTokenManager
import com.moragar.catbreeds.login.data.repository.AuthRepositoryImpl
import com.moragar.catbreeds.login.data.usecase.CheckAuthStatusUseCaseImpl
import com.moragar.catbreeds.login.data.usecase.LoginUseCaseImpl
import com.moragar.catbreeds.login.data.usecase.LogoutUseCaseImpl
import com.moragar.catbreeds.login.domain.repository.AuthRepository
import com.moragar.catbreeds.login.domain.usecase.CheckAuthStatusUseCase
import com.moragar.catbreeds.login.domain.usecase.LoginUseCase
import com.moragar.catbreeds.login.domain.usecase.LogoutUseCase
import com.moragar.catbreeds.login.presentation.login.LoginViewModel
import com.russhwolf.settings.Settings
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

fun initKoin(appModule: Module = module {}) {
    startKoin {
        modules(
            appModule +
                    platformModule +
                    databaseModule +
                    networkModule +
                    repositoryModule +
                    useCaseModule +
                    viewModelModule +
                    settingsModule +
                    networkMonitorModule +
                    authModule
        )
    }
}

val networkModule = module {
    single { createApiClient(get()) }
    single<CatApi> { CatApiImpl(get()) }
}

val repositoryModule = module {
    single<CatRepository> { CatRepositoryImpl(get(), get(), get()) }
}

val useCaseModule = module {
    factory<GetCatBreedsUseCase> { GetCatBreedsUseCaseImpl(get()) }
    factory<ToggleFavoriteUseCase> { ToggleFavoriteUseCaseImpl(get()) }
    factory<ReadFavoriteBreedsUseCase> { ReadFavoriteBreedsUseCaseImpl(get()) }
    factory<LoginUseCase> { LoginUseCaseImpl(get()) }
    factory<LogoutUseCase> { LogoutUseCaseImpl(get()) }
    factory<CheckAuthStatusUseCase> { CheckAuthStatusUseCaseImpl(get()) }
    factory<IsOnlineUseCase> { IsOnlineUseCaseImpl(get()) }
    factory<SaveCachedBreedUseCase> { SaveCachedBreedUseCaseImpl(get()) }
    factory<ClearCachedBreedsUseCase> { ClearCachedBreedsUseCaseImpl(get()) }
    factory<ReadAllCachedBreedsUseCase> { ReadAllCachedBreedsUseCaseImpl(get()) }
}

val settingsModule = module {
    single { Settings() }
}

val authModule = module {
    single { CatTokenManager(get()) }
    single<AuthRepository> { AuthRepositoryImpl(get()) }
}

val viewModelModule = module {
    viewModelOf(::CatBreedsViewModel)
    viewModelOf(::LoginViewModel)
}

val databaseModule = module {
    single<CatDatabaseFactoryImpl> { CatDatabaseFactoryImpl(get()) }
}

expect object ApiKeyProvider {
    val apiKey: String
}
expect val networkMonitorModule: Module
expect val platformModule: Module
package com.moragar.catbreeds.core.di

import com.moragar.catbreeds.BuildConfig
import com.moragar.catbreeds.core.data.database.AndroidDatabaseFactory
import com.moragar.catbreeds.core.data.network.NetworkMonitorImpl
import com.moragar.catbreeds.core.domain.database.CatDatabaseFactory
import com.moragar.catbreeds.core.domain.network.NetworkMonitor
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module = module {
    includes(viewModelModule)
    single<HttpClientEngine> { OkHttp.create() }
    single<CatDatabaseFactory> { AndroidDatabaseFactory(androidContext()) }
    single<CoroutineDispatcher> { Dispatchers.IO }
}

actual val networkMonitorModule = module {
    single<NetworkMonitor> { NetworkMonitorImpl(androidContext()) }
}

actual object ApiKeyProvider {
    actual val apiKey: String = BuildConfig.KEY
}
package com.moragar.catbreeds.core.di

import com.moragar.catbreeds.core.data.database.DesktopDatabaseFactory
import com.moragar.catbreeds.core.data.network.NetworkMonitorImpl
import com.moragar.catbreeds.core.domain.database.CatDatabaseFactory
import com.moragar.catbreeds.core.domain.network.NetworkMonitor
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.java.Java
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module = module {
    single<HttpClientEngine> { Java.create() }
    single<CatDatabaseFactory> { DesktopDatabaseFactory() }
    single<CoroutineDispatcher> { Dispatchers.Default }
    includes(viewModelModule)
}

actual val networkMonitorModule = module {
    single<NetworkMonitor> { NetworkMonitorImpl() }
}

actual object ApiKeyProvider {
    actual val apiKey: String =
        System.getProperty("KEY")
}
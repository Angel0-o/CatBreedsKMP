package com.moragar.catbreeds.core.di

import com.moragar.catbreeds.core.data.database.IosDatabaseFactory
import com.moragar.catbreeds.core.data.network.NetworkMonitorImpl
import com.moragar.catbreeds.core.domain.database.CatDatabaseFactory
import com.moragar.catbreeds.core.domain.network.NetworkMonitor
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.Module
import org.koin.dsl.module
import platform.Foundation.NSBundle

actual val platformModule: Module = module {
    single<HttpClientEngine> { Darwin.create() }
    single<CatDatabaseFactory> { IosDatabaseFactory() }
    single<CoroutineDispatcher> { Dispatchers.Default }
    includes(viewModelModule)
}

actual val networkMonitorModule = module {
    single<NetworkMonitor> { NetworkMonitorImpl() }
}

actual object ApiKeyProvider {
    actual val apiKey: String =
        NSBundle.mainBundle.objectForInfoDictionaryKey("CAT_API_KEY") as? String
            ?: error("CAT_API_KEY not found in Info.plist")
}
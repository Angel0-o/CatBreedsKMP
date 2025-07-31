package com.moragar.catbreeds

import android.app.Application
import com.moragar.catbreeds.core.di.authModule
import com.moragar.catbreeds.core.di.databaseModule
import com.moragar.catbreeds.core.di.networkModule
import com.moragar.catbreeds.core.di.networkMonitorModule
import com.moragar.catbreeds.core.di.platformModule
import com.moragar.catbreeds.core.di.repositoryModule
import com.moragar.catbreeds.core.di.settingsModule
import com.moragar.catbreeds.core.di.useCaseModule
import com.moragar.catbreeds.core.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import kotlin.collections.plus

class CatBreedsApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@CatBreedsApp)
            modules(
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
}
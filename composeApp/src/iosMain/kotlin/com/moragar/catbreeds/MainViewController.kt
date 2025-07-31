package com.moragar.catbreeds

import androidx.compose.ui.window.ComposeUIViewController
import com.moragar.catbreeds.core.di.initKoin
import com.moragar.catbreeds.core.presentation.navigation.NavigationManager

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) { NavigationManager() }
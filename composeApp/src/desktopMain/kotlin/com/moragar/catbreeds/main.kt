package com.moragar.catbreeds

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.moragar.catbreeds.core.di.initKoin
import com.moragar.catbreeds.core.presentation.navigation.NavigationManager

fun main() = application {
    initKoin()
    Window(
        onCloseRequest = ::exitApplication,
        title = "CatBreeds",
    ) {
        NavigationManager()
    }
}
package com.moragar.catbreeds.core.presentation.navigation

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition

@Composable
fun NavigationManager() {
    MaterialTheme {
        Navigator(
            screen = CatLoginScreen,
        ) { navigator ->
            SlideTransition(navigator) { screen ->
                screen.Content()
            }
        }
    }
}
package com.moragar.catbreeds.core.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.moragar.catbreeds.catbreed.domain.model.CatBreed
import com.moragar.catbreeds.catbreed.presentation.catBreed.CatBreedAction
import com.moragar.catbreeds.catbreed.presentation.catBreed.CatBreedDetailScreen
import com.moragar.catbreeds.catbreed.presentation.catBreed.CatBreedListScreen
import com.moragar.catbreeds.catbreed.presentation.catBreed.CatBreedsState
import com.moragar.catbreeds.catbreed.presentation.catBreed.CatBreedsViewModel
import com.moragar.catbreeds.catbreed.presentation.catBreed.ConnectivityState
import com.moragar.catbreeds.login.presentation.login.CatLoginScreen
import com.moragar.catbreeds.login.presentation.login.LoginAction
import com.moragar.catbreeds.login.presentation.login.LoginState
import com.moragar.catbreeds.login.presentation.login.LoginViewModel
import org.koin.compose.koinInject

object CatLoginScreen : Screen {
    @Composable
    override fun Content() = LoginScreenWrapper(
        content = { state, onAction, navigator ->
            CatLoginScreen(
                state = state,
                onAction = onAction,
                navigateToHome = {
                    navigator.push(CatBreedListScreen)
                }
            )
        }
    )
}

object CatBreedListScreen : Screen {
    @Composable
    override fun Content() =
        CatScreenWrapper(
            content = { state, networkState, onAction, navigator ->
                CatBreedListScreen(
                    uiState = state,
                    networkState = networkState,
                    onAction = onAction,
                    navigateToDetail = { breed ->
                        navigator.push(CatBreedDetailScreen(breed = breed))
                    }
                )
            }
        )
}

class CatBreedDetailScreen(private val breed: CatBreed) : Screen {
    @Composable
    override fun Content() = CatScreenWrapper(
        content = { state, networkState, onAction, navigator ->
            CatBreedDetailScreen(
                breed = breed,
                onAction = onAction,
                onBackClick = {
                    navigator.pop()
                }
            )
        }
    )
}

@Composable
fun CatScreenWrapper(content: @Composable (CatBreedsState, ConnectivityState, (CatBreedAction) -> Unit, Navigator) -> Unit) {
    val viewModel: CatBreedsViewModel = koinInject()
    val state by viewModel.state.collectAsState()
    val networkState by viewModel.connectivityState.collectAsState()
    val navigator = LocalNavigator.currentOrThrow
    content(state, networkState, viewModel::onAction, navigator)
}

@Composable
fun LoginScreenWrapper(content: @Composable (LoginState, (LoginAction) -> Unit, Navigator) -> Unit) {
    val viewModel: LoginViewModel = koinInject()
    val state by viewModel.state.collectAsState()
    val navigator = LocalNavigator.currentOrThrow
    content(state, viewModel::onAction, navigator)
}
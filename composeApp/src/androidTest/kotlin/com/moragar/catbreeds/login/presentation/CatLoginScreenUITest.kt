package com.moragar.catbreeds.login.presentation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.moragar.catbreeds.login.presentation.login.CatLoginScreen
import com.moragar.catbreeds.login.presentation.login.LoginAction
import com.moragar.catbreeds.login.presentation.login.LoginState
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertTrue

class CatLoginScreenUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun login_withValidCredentials_triggersAction() {
        var loginCalled = false

        composeTestRule.setContent {
            CatLoginScreen(
                state = LoginState(),
                onAction = {
                    if (it is LoginAction.ValidateForm) loginCalled = true
                },
                navigateToHome = {}
            )
        }

        composeTestRule.onNodeWithTag("usernameField").performTextInput("user@gmail.com")
        composeTestRule.onNodeWithTag("passwordField").performTextInput("Password123!")
        composeTestRule.onNodeWithTag("loginButton").performClick()

        assertTrue(loginCalled)
    }

    @Test
    fun login_withError_showsErrorDialog() {
        composeTestRule.setContent {
            CatLoginScreen(
                state = LoginState(error = "Invalid credentials"),
                onAction = {},
                navigateToHome = {}
            )
        }
        composeTestRule.onNodeWithTag("errorDialog").assertIsDisplayed()
    }

    @Test
    fun login_withInvalidEmail_showsEmailError() {
        composeTestRule.setContent {
            CatLoginScreen(
                state = LoginState(usernameError = "Invalid email format"),
                onAction = {},
                navigateToHome = {}
            )
        }

        composeTestRule.onNodeWithTag("usernameError").assertIsDisplayed()
    }

    @Test
    fun showsLoadingIndicator_whenStateIsLoading() {
        composeTestRule.setContent {
            CatLoginScreen(
                state = LoginState(isLoading = true),
                onAction = {},
                navigateToHome = {}
            )
        }

        composeTestRule.onNodeWithTag("loadingIndicator").assertIsDisplayed()
    }
}
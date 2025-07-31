package com.moragar.catbreeds.login.presentation.login

import com.moragar.catbreeds.login.data.usecase.FakeCheckAuthStatusUseCase
import com.moragar.catbreeds.login.data.usecase.FakeLoginUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: LoginViewModel
    private lateinit var fakeLoginUseCase: FakeLoginUseCase
    private lateinit var fakeCheckAuthStatusUseCase: FakeCheckAuthStatusUseCase

    @BeforeTest
    fun setup() {
        fakeLoginUseCase = FakeLoginUseCase()
        fakeCheckAuthStatusUseCase = FakeCheckAuthStatusUseCase(isLoggedIn = false)
        viewModel = LoginViewModel(
            loginUseCase = fakeLoginUseCase,
            checkAuthStatus = fakeCheckAuthStatusUseCase,
            dispatcher = testDispatcher
        )
    }

    @Test
    fun `initial state should reflect auth status`() {
        assertFalse(viewModel.state.value.isLoggedIn)

        // Try when already logged in
        fakeCheckAuthStatusUseCase = FakeCheckAuthStatusUseCase(isLoggedIn = true)
        val newVm = LoginViewModel(fakeLoginUseCase, fakeCheckAuthStatusUseCase)
        assertTrue(newVm.state.value.isLoggedIn)
    }

    @Test
    fun `set invalid email should set usernameError`() {
        viewModel.onAction(LoginAction.SetUsernameChanged("invalidEmail"))
        assertEquals("Invalid email format", viewModel.state.value.usernameError)
    }

    @Test
    fun `set invalid password should set passwordError`() {
        viewModel.onAction(LoginAction.SetPasswordChanged("123"))
        assertEquals("Password must be at least 8 characters with uppercase, number and symbol", viewModel.state.value.passwordError)
    }

    @Test
    fun `validate form with valid credentials should trigger login`() = runTest(testDispatcher) {
        // SetUp
        viewModel.onAction(LoginAction.SetUsernameChanged("user@gmail.com"))
        viewModel.onAction(LoginAction.SetPasswordChanged("Password123!"))
        fakeLoginUseCase.shouldSucceed = true

        // Actions
        viewModel.onAction(LoginAction.ValidateForm)
        testScheduler.advanceUntilIdle()

        // Assert
        assertTrue(viewModel.state.value.isLoggedIn)
        assertFalse(viewModel.state.value.isLoading)
        assertEquals("", viewModel.state.value.error)
    }

    @Test
    fun `login failure should update state with error`() = runTest(testDispatcher) {
        // SetUp
        viewModel.onAction(LoginAction.SetUsernameChanged("user@gmail.com"))
        viewModel.onAction(LoginAction.SetPasswordChanged("Password123!"))
        fakeLoginUseCase.shouldSucceed = false

        // Actions
        viewModel.onAction(LoginAction.ValidateForm)
        testScheduler.advanceUntilIdle()

        // Assert
        assertFalse(viewModel.state.value.isLoggedIn)
        assertFalse(viewModel.state.value.isLoading)
        assertEquals("Invalid credentials", viewModel.state.value.error)
    }
}

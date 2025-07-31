package com.moragar.catbreeds.login.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moragar.catbreeds.core.util.Constants.INVALID_EMAIL_FORMAT
import com.moragar.catbreeds.core.util.Constants.INVALID_PASSWORD_FORMAT
import com.moragar.catbreeds.login.data.auth.ValidationUtils
import com.moragar.catbreeds.login.domain.model.UserCredentials
import com.moragar.catbreeds.login.domain.usecase.CheckAuthStatusUseCase
import com.moragar.catbreeds.login.domain.usecase.LoginUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val checkAuthStatus: CheckAuthStatusUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {
    val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    init {
        _state.value = _state.value.copy(isLoggedIn = checkAuthStatus())
    }

    fun onAction(action: LoginAction) {
        when (action) {
            is LoginAction.SetUsernameChanged -> setUserName(action.username)
            is LoginAction.SetPasswordChanged -> setPassword(action.password)
            is LoginAction.SetIsValidForm -> setIsValidForm(action.isValidForm)
            is LoginAction.ValidateForm -> validateAndSubmitForm()
            is LoginAction.Submit -> login()
        }
    }

    private fun login() {
        _state.value = _state.value.copy(
            isLoading = true,
            error = ""
        )
        viewModelScope.launch(context = dispatcher) {
            val result = loginUseCase(
                UserCredentials(
                    username = _state.value.username,
                    password = _state.value.password
                )
            )
            result.fold(
                onSuccess = { token ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        isLoggedIn = true
                    )
                },
                onFailure = { error ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = error.message ?: ""
                    )
                }
            )
        }
    }

    private fun setUserName(username: String) {
        _state.value = _state.value.copy(
            username = username,
            usernameError = if (username.isNotEmpty() && !ValidationUtils.isValidEmail(username))
                INVALID_EMAIL_FORMAT else ""
        )
    }

    private fun setPassword(password: String) {
        _state.value = _state.value.copy(
            password = password,
            passwordError = if (password.isNotEmpty() && !ValidationUtils.isValidPassword(password))
                INVALID_PASSWORD_FORMAT else ""
        )
    }

    private fun setIsValidForm(isValidForm: Boolean) {
        _state.value = _state.value.copy(
            isValidForm = isValidForm
        )
    }

    private fun validateAndSubmitForm() {
        val isEmailValid = ValidationUtils.isValidEmail(state.value.username)
        val isPasswordValid = ValidationUtils.isValidPassword(state.value.password)
        val newIsValidForm = isEmailValid && isPasswordValid
        if (newIsValidForm)
            onAction(LoginAction.Submit)
        _state.value = _state.value.copy(
            isValidForm = newIsValidForm
        )
    }
}

data class LoginState(
    val username: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val isValidForm: Boolean = false,
    val error: String = "",
    val usernameError: String = "",
    val passwordError: String = ""
)

sealed interface LoginAction {
    data class SetUsernameChanged(val username: String) : LoginAction
    data class SetPasswordChanged(val password: String) : LoginAction
    data class SetIsValidForm(val isValidForm: Boolean) : LoginAction
    object ValidateForm : LoginAction
    object Submit : LoginAction
}
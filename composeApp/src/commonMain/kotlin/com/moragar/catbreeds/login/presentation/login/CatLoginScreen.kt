package com.moragar.catbreeds.login.presentation.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import catbreeds.composeapp.generated.resources.Res
import catbreeds.composeapp.generated.resources.cat_login
import catbreeds.composeapp.generated.resources.email
import catbreeds.composeapp.generated.resources.ic_moracat
import catbreeds.composeapp.generated.resources.invalid_credentials
import catbreeds.composeapp.generated.resources.login
import catbreeds.composeapp.generated.resources.login_icon
import catbreeds.composeapp.generated.resources.password
import catbreeds.composeapp.generated.resources.password_must_contain
import catbreeds.composeapp.generated.resources.sign_in
import com.moragar.catbreeds.core.presentation.styles.Black
import com.moragar.catbreeds.core.presentation.styles.ErrorRed
import com.moragar.catbreeds.core.presentation.uicomponents.CustomAlert
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun CatLoginScreen(
    state: LoginState,
    onAction: (LoginAction) -> Unit,
    navigateToHome: () -> Unit,
) {
    val openDialog = remember { mutableStateOf(false) }
    val username = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }

    LaunchedEffect(state.isLoggedIn) {
        if (state.isLoggedIn) {
            navigateToHome()
        }
    }
    LaunchedEffect(state.error) {
        if (state.error.isNotEmpty()) {
            openDialog.value = true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .testTag("catLoginScreen"),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (openDialog.value) {
            CustomAlert(
                title = stringResource(Res.string.cat_login),
                message = stringResource(Res.string.invalid_credentials),
                openDialog = openDialog.value,
                onDismissDialog = {
                    openDialog.value = false
                },
                modifier = Modifier.testTag("errorDialog")
            )
        }
        Image(
            painter = painterResource(resource = Res.drawable.ic_moracat),
            contentDescription = stringResource(Res.string.login_icon),
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
                .testTag("loginIcon")
        )
        Text(
            text = stringResource(Res.string.sign_in),
            fontWeight = FontWeight.ExtraBold,
            fontSize = 50.sp,
            color = Black,
        )

        Spacer(modifier = Modifier.height(40.dp))

        OutlinedTextField(
            value = username.value,
            onValueChange = { username.value = it },
            label = { Text(stringResource(Res.string.email)) },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("usernameField"),
            shape = RoundedCornerShape(25.dp),
            singleLine = true,
            isError = state.usernameError.isNotEmpty(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )
        if (state.usernameError.isNotEmpty()) {
            Text(
                text = state.usernameError,
                color = ErrorRed,
                modifier = Modifier
                    .padding(start = 16.dp, top = 4.dp)
                    .testTag("usernameError")
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password.value,
            onValueChange = { password.value = it },
            label = { Text(stringResource(Res.string.password)) },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("passwordField"),
            visualTransformation = PasswordVisualTransformation(),
            shape = RoundedCornerShape(25.dp),
            singleLine = true,
            isError = state.passwordError.isNotEmpty(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
        if (state.passwordError.isNotEmpty()) {
            Text(
                text = state.passwordError,
                color = ErrorRed,
                modifier = Modifier
                    .padding(start = 16.dp, top = 4.dp)
                    .testTag("passwordError")
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                onAction(LoginAction.SetUsernameChanged(username.value))
                onAction(LoginAction.SetPasswordChanged(password.value))
                onAction(LoginAction.ValidateForm)
                      },
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .testTag("loginButton"),
            enabled = !state.isLoading
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(Modifier.testTag("loadingIndicator"))
            } else {
                Text(stringResource(Res.string.login))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(Res.string.password_must_contain),
            fontWeight = FontWeight.Light,
            modifier = Modifier.testTag("passwordHint")
        )
    }
}
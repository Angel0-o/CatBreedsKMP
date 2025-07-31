package com.moragar.catbreeds.login.data.usecase

import com.moragar.catbreeds.login.domain.model.AuthToken
import com.moragar.catbreeds.login.domain.model.UserCredentials
import com.moragar.catbreeds.login.domain.repository.AuthRepository
import com.moragar.catbreeds.login.domain.usecase.LoginUseCase

class LoginUseCaseImpl(
    private val repository: AuthRepository
) : LoginUseCase {
    override suspend operator fun invoke(credentials: UserCredentials): Result<AuthToken> =
        repository.login(credentials)
}
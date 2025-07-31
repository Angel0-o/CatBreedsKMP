package com.moragar.catbreeds.login.domain.usecase

import com.moragar.catbreeds.login.domain.model.AuthToken
import com.moragar.catbreeds.login.domain.model.UserCredentials

interface LoginUseCase {
    suspend operator fun invoke(credentials: UserCredentials): Result<AuthToken>
}
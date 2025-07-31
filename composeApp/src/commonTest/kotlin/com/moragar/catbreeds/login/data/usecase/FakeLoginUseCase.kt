package com.moragar.catbreeds.login.data.usecase

import com.moragar.catbreeds.login.domain.model.AuthToken
import com.moragar.catbreeds.login.domain.model.UserCredentials
import com.moragar.catbreeds.login.domain.usecase.LoginUseCase

class FakeLoginUseCase : LoginUseCase {
    var shouldSucceed = true

    override suspend fun invoke(credentials: UserCredentials): Result<AuthToken> {
        return if (shouldSucceed) {
            Result.success(AuthToken("fakeToken"))
        } else {
            Result.failure(Exception("Invalid credentials"))
        }
    }
}
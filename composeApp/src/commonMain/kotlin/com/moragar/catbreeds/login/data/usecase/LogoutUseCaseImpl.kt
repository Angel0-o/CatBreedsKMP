package com.moragar.catbreeds.login.data.usecase

import com.moragar.catbreeds.login.domain.repository.AuthRepository
import com.moragar.catbreeds.login.domain.usecase.LogoutUseCase

class LogoutUseCaseImpl(
    private val repository: AuthRepository
) : LogoutUseCase {
    override operator fun invoke() =
        repository.logout()
}
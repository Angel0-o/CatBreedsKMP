package com.moragar.catbreeds.login.data.usecase

import com.moragar.catbreeds.login.domain.repository.AuthRepository
import com.moragar.catbreeds.login.domain.usecase.CheckAuthStatusUseCase

class CheckAuthStatusUseCaseImpl(
    private val repository: AuthRepository
) : CheckAuthStatusUseCase  {
    override operator fun invoke(): Boolean {
        return repository.isAuthenticated()
    }
}
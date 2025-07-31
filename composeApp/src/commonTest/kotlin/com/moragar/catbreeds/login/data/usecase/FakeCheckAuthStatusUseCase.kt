package com.moragar.catbreeds.login.data.usecase

import com.moragar.catbreeds.login.domain.usecase.CheckAuthStatusUseCase

class FakeCheckAuthStatusUseCase(private val isLoggedIn: Boolean) : CheckAuthStatusUseCase {
    override operator fun invoke(): Boolean = isLoggedIn
}
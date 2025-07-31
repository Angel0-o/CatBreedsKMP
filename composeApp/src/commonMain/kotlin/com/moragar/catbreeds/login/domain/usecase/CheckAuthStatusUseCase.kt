package com.moragar.catbreeds.login.domain.usecase

interface CheckAuthStatusUseCase {
    operator fun invoke(): Boolean
}
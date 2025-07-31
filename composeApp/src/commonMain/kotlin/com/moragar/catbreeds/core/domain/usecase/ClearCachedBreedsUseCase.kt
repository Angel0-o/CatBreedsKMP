package com.moragar.catbreeds.core.domain.usecase

interface ClearCachedBreedsUseCase {
    suspend operator fun invoke()
}
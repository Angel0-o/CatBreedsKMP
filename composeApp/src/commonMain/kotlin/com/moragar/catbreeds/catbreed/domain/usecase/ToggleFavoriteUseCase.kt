package com.moragar.catbreeds.catbreed.domain.usecase

interface ToggleFavoriteUseCase {
    suspend operator fun invoke(breedId: String)
}
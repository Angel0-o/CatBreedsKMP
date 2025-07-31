package com.moragar.catbreeds.catbreed.data.usecase

import com.moragar.catbreeds.catbreed.domain.repository.CatRepository
import com.moragar.catbreeds.catbreed.domain.usecase.ToggleFavoriteUseCase

class ToggleFavoriteUseCaseImpl(
    private val repository: CatRepository
) : ToggleFavoriteUseCase {
    override suspend operator fun invoke(breedId: String) {
        repository.toggleFavorite(breedId)
    }
}
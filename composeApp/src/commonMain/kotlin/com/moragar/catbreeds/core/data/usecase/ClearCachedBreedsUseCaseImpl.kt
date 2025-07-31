package com.moragar.catbreeds.core.data.usecase

import com.moragar.catbreeds.catbreed.domain.repository.CatRepository
import com.moragar.catbreeds.core.domain.usecase.ClearCachedBreedsUseCase

class ClearCachedBreedsUseCaseImpl(
    private val repository: CatRepository
) : ClearCachedBreedsUseCase {
    override suspend operator fun invoke() =
        repository.clearCachedBreeds()
}
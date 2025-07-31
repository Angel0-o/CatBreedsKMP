package com.moragar.catbreeds.core.data.usecase

import com.moragar.catbreeds.catbreed.domain.model.CatBreed
import com.moragar.catbreeds.catbreed.domain.repository.CatRepository
import com.moragar.catbreeds.core.domain.usecase.SaveCachedBreedUseCase

class SaveCachedBreedUseCaseImpl(
    private val repository: CatRepository
) : SaveCachedBreedUseCase {
    override suspend operator fun invoke(breed: CatBreed) =
        repository.insertCachedBreed(breed)
}
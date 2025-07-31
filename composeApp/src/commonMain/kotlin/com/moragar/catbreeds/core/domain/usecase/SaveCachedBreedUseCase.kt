package com.moragar.catbreeds.core.domain.usecase

import com.moragar.catbreeds.catbreed.domain.model.CatBreed

interface SaveCachedBreedUseCase {
    suspend operator fun invoke(breed: CatBreed)
}
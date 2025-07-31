package com.moragar.catbreeds.core.domain.usecase

import com.moragar.catbreeds.catbreed.domain.model.CatBreed
import kotlinx.coroutines.flow.Flow

interface ReadAllCachedBreedsUseCase {
    suspend operator fun invoke(): Flow<List<CatBreed>>
}
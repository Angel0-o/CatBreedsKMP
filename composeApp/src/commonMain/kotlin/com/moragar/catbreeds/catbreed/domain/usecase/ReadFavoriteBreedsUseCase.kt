package com.moragar.catbreeds.catbreed.domain.usecase

import kotlinx.coroutines.flow.Flow

interface ReadFavoriteBreedsUseCase {
    suspend operator fun invoke() : Flow<List<String>>
}
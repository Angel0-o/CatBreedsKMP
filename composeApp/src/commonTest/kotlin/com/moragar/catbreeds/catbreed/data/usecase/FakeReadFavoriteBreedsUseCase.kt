package com.moragar.catbreeds.catbreed.data.usecase

import com.moragar.catbreeds.catbreed.domain.repository.CatRepository
import com.moragar.catbreeds.catbreed.domain.usecase.ReadFavoriteBreedsUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeReadFavoriteBreedsUseCase(
    private val repository: CatRepository
) : ReadFavoriteBreedsUseCase {
    override suspend fun invoke(): Flow<List<String>> =
        repository.readFavoriteBreeds()
}
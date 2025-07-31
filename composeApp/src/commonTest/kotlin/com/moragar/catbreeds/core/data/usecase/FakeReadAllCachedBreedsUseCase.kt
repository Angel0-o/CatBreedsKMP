package com.moragar.catbreeds.core.data.usecase

import com.moragar.catbreeds.catbreed.domain.model.CatBreed
import com.moragar.catbreeds.catbreed.domain.repository.CatRepository
import com.moragar.catbreeds.core.domain.usecase.ReadAllCachedBreedsUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeReadAllCachedBreedsUseCase(
    private val repository: CatRepository
) : ReadAllCachedBreedsUseCase {
    override suspend fun invoke(): Flow<List<CatBreed>> =
        repository.readAllCachedBreeds()
}
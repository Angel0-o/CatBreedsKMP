package com.moragar.catbreeds.catbreed.domain.repository

import com.moragar.catbreeds.catbreed.domain.model.CatBreed
import kotlinx.coroutines.flow.Flow

interface CatRepository {
    suspend fun getCatBreeds(limit: Int = 10, page: Int = 0): Flow<Result<List<CatBreed>>>
    suspend fun toggleFavorite(breedId: String)
    suspend fun readFavoriteBreeds(): Flow<List<String>>
    suspend fun isOnline(): Flow<Boolean>
    suspend fun clearCachedBreeds()
    suspend fun insertCachedBreed(breed: CatBreed)
    suspend fun readAllCachedBreeds(): Flow<List<CatBreed>>
}
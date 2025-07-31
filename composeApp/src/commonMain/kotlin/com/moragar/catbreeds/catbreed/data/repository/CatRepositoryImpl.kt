package com.moragar.catbreeds.catbreed.data.repository

import com.moragar.catbreeds.catbreed.data.remote.CatApi
import com.moragar.catbreeds.catbreed.domain.model.CatBreed
import com.moragar.catbreeds.catbreed.domain.repository.CatRepository
import com.moragar.catbreeds.core.data.database.CatDatabaseFactoryImpl
import com.moragar.catbreeds.core.domain.network.NetworkMonitor
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow

class CatRepositoryImpl(
    private val api: CatApi,
    private val database: CatDatabaseFactoryImpl,
    private val networkMonitor: NetworkMonitor
) : CatRepository {
    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getCatBreeds(limit: Int, page: Int): Flow<Result<List<CatBreed>>> =
        networkMonitor.isOnline().flatMapLatest { isOnline ->
            flow {
                try {
                    val breeds = if (isOnline) {
                        api.getBreeds(limit, page)
                    } else {
                        database.readAllCachedBreeds()
                    }
                    emit(Result.success(breeds))
                } catch (e: Exception) {
                    emit(Result.failure(e))
                }
            }
        }

    override suspend fun toggleFavorite(breedId: String) {
        if (database.isFavorite(breedId)) {
            database.removeFavoriteBreed(breedId)
        } else {
            database.addFavoriteBreed(breedId)
        }
    }

    override suspend fun readFavoriteBreeds(): Flow<List<String>> = flow {
        try {
            val favorites = database.readAllFavoriteBreeds()
            emit(favorites)
        } catch (e: Exception) {
            emit(emptyList())
        }
    }

    override suspend fun isOnline(): Flow<Boolean> = networkMonitor.isOnline()

    override suspend fun clearCachedBreeds() {
        database.clearCachedBreeds()
    }

    override suspend fun insertCachedBreed(breed: CatBreed) {
        database.insertCachedBreed(breed)
    }

    override suspend fun readAllCachedBreeds(): Flow<List<CatBreed>> = flow {
        try {
            val breeds = database.readAllCachedBreeds()
            emit(breeds)
        } catch (e: Exception) {
            emit(emptyList())
        }
    }
}
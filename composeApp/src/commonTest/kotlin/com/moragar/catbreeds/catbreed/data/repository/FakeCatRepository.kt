package com.moragar.catbreeds.catbreed.data.repository

import com.moragar.catbreeds.catbreed.domain.model.CatBreed
import com.moragar.catbreeds.catbreed.domain.repository.CatRepository
import com.moragar.catbreeds.core.data.database.FakeCatDatabaseFactoryImpl
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow

class FakeCatRepository(
    private val fakeDatabase: FakeCatDatabaseFactoryImpl = FakeCatDatabaseFactoryImpl(),
    private var online: Boolean = true
) : CatRepository {
    private val onlineFlow = MutableStateFlow(online)
    private val apiBreeds: MutableList<CatBreed> = mutableListOf()

    private var shouldThrow: Exception? = null

    fun setOnline(value: Boolean) { onlineFlow.value = value}

    fun setApiBreeds(breeds: List<CatBreed>) {
        apiBreeds.clear()
        apiBreeds.addAll(breeds)
    }

    fun simulateError(error: Exception) {
        shouldThrow = error
    }

    override suspend fun getCatBreeds(
        limit: Int,
        page: Int
    ): Flow<Result<List<CatBreed>>> = flow {
        shouldThrow?.let {
            emit(Result.failure(it))
            return@flow
        }
        try {
            val breeds = if (online) apiBreeds else fakeDatabase.readAllCachedBreeds()
            emit(Result.success(breeds))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override suspend fun toggleFavorite(breedId: String) {
        if (fakeDatabase.isFavorite(breedId)) {
            fakeDatabase.removeFavoriteBreed(breedId)
        } else {
            fakeDatabase.addFavoriteBreed(breedId)
        }
    }

    override suspend fun readFavoriteBreeds(): Flow<List<String>> = flow {
        emit(fakeDatabase.readAllFavoriteBreeds())
    }

    override suspend fun isOnline(): Flow<Boolean> = onlineFlow

    override suspend fun clearCachedBreeds() {
        fakeDatabase.clearCachedBreeds()
    }

    override suspend fun insertCachedBreed(breed: CatBreed) {
        fakeDatabase.insertCachedBreed(breed)
    }

    override suspend fun readAllCachedBreeds(): Flow<List<CatBreed>> = flow {
        emit(fakeDatabase.readAllCachedBreeds())
    }
}
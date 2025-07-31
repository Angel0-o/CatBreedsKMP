package com.moragar.catbreeds.core.data.database

import com.moragar.catbreeds.catbreed.domain.model.CatBreed

class FakeCatDatabaseFactoryImpl() {
    private val favoriteIds = mutableListOf<String>()
    private val cachedBreeds = mutableListOf<CatBreed>()

    fun readAllFavoriteBreeds(): List<String> = favoriteIds.toList()

    fun addFavoriteBreed(id: String) {
        if (!favoriteIds.contains(id)) favoriteIds.add(id)
    }

    fun removeFavoriteBreed(id: String) {
        favoriteIds.remove(id)
    }

    fun isFavorite(id: String): Boolean = favoriteIds.contains(id)

    fun clearCachedBreeds() {
        cachedBreeds.clear()
    }

    fun insertCachedBreed(breed: CatBreed) {
        cachedBreeds.removeAll { it.id == breed.id }
        cachedBreeds.add(breed)
    }

    fun readAllCachedBreeds(): List<CatBreed> = cachedBreeds.toList()
}
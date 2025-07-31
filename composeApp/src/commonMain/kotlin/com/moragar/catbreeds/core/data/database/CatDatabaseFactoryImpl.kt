package com.moragar.catbreeds.core.data.database

import com.moragar.catbreeds.catbreed.domain.model.CatBreed
import com.moragar.catbreeds.catbreed.domain.model.CatImage
import com.moragar.catbreeds.core.domain.database.CatDatabaseFactory
import com.moragar.db.CatDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext


class CatDatabaseFactoryImpl(
    private val catDatabaseFactory: CatDatabaseFactory
) {
    private val database: CatDatabase by lazy {
        CatDatabase(catDatabaseFactory.createDriver())
    }
    private val catQueries = database.catDatabaseQueries

    suspend fun readAllFavoriteBreeds(): List<String> = withContext(Dispatchers.IO) {
        try {
            println("INFO: Reading all favorite breeds")
            catQueries.selectAllFavorites().executeAsList()
        } catch (e: Exception) {
            println("ERROR: ${e.message}")
            emptyList()
        }
    }

    suspend fun addFavoriteBreed(id: String) = withContext(Dispatchers.IO) {
        try {
            println("INFO: Adding favorite breed with id $id")
            catQueries.insertFavorite(id)
        } catch (e: Exception) {
            println("ERROR: ${e.message}")
        }
    }

    suspend fun removeFavoriteBreed(id: String) = withContext(Dispatchers.IO) {
        try {
            println("INFO: Removing favorite breed with id $id")
            catQueries.deleteFavorite(id)
        } catch (e: Exception) {
            println("ERROR: ${e.message}")
        }
    }

    suspend fun isFavorite(id: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val result = catQueries.isFavorite(id).executeAsOneOrNull()
            println("INFO: CatBreed with id $id is favorite: $result")
            result != null
        } catch (e: Exception) {
            println("ERROR: ${e.message}")
            false
        }
    }

    suspend fun clearCachedBreeds() = withContext(Dispatchers.IO) {
        try {
            println("INFO: Clearing cached breeds")
            catQueries.clearCachedBreeds()
        } catch (e: Exception) {
            println("ERROR: ${e.message}")
        }
    }

    suspend fun insertCachedBreed(breed: CatBreed) = withContext(Dispatchers.IO) {
        try {
            println("INFO: Inserting cached breed with id ${breed.id}")
            catQueries.insertCachedBreed(
                id = breed.id,
                name = breed.name,
                description = breed.description,
                origin = breed.origin,
                temperament = breed.temperament,
                lifeSpan = breed.lifeSpan,
                imageUrl = breed.image?.url,
                isFavorite = if (breed.isFavorite) "1" else "0"
            )
        } catch (e: Exception) {
            println("ERROR: ${e.message}")
        }
    }

    suspend fun readAllCachedBreeds(): List<CatBreed> = withContext(Dispatchers.IO) {
        try {
            println("INFO: Reading all cached breeds")
            catQueries.selectAllCachedBreeds().executeAsList().map { breed ->
                CatBreed(
                    id = breed.id,
                    name = breed.name,
                    description = breed.description,
                    origin = breed.origin,
                    temperament = breed.temperament,
                    lifeSpan = breed.lifeSpan,
                    image = breed.imageUrl?.let { CatImage(it) },
                    isFavorite = breed.isFavorite == "1"
                )
            }
        } catch (e: Exception) {
            println("ERROR: ${e.message}")
            emptyList()
        }
    }
}
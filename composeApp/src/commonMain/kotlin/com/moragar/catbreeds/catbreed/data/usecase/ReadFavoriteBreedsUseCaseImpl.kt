package com.moragar.catbreeds.catbreed.data.usecase

import com.moragar.catbreeds.catbreed.domain.repository.CatRepository
import com.moragar.catbreeds.catbreed.domain.usecase.ReadFavoriteBreedsUseCase
import kotlinx.coroutines.flow.Flow

class ReadFavoriteBreedsUseCaseImpl(
    private val repository: CatRepository
) : ReadFavoriteBreedsUseCase {
    override suspend operator fun invoke() : Flow<List<String>> =
        repository.readFavoriteBreeds()
}
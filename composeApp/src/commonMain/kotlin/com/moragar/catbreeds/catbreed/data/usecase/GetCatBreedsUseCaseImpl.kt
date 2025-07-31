package com.moragar.catbreeds.catbreed.data.usecase

import com.moragar.catbreeds.catbreed.domain.model.CatBreed
import com.moragar.catbreeds.catbreed.domain.repository.CatRepository
import com.moragar.catbreeds.catbreed.domain.usecase.GetCatBreedsUseCase
import kotlinx.coroutines.flow.Flow

class GetCatBreedsUseCaseImpl(
    private val repository: CatRepository
): GetCatBreedsUseCase {
    override suspend operator fun invoke(limit: Int, page: Int): Flow<Result<List<CatBreed>>> =
        repository.getCatBreeds(limit, page)
}
package com.moragar.catbreeds.catbreed.domain.usecase

import com.moragar.catbreeds.catbreed.domain.model.CatBreed
import kotlinx.coroutines.flow.Flow

interface GetCatBreedsUseCase {
    suspend operator fun invoke(limit: Int, page: Int): Flow<Result<List<CatBreed>>>
}
package com.moragar.catbreeds.core.data.usecase

import com.moragar.catbreeds.catbreed.domain.repository.CatRepository
import com.moragar.catbreeds.core.domain.usecase.IsOnlineUseCase
import kotlinx.coroutines.flow.Flow

class IsOnlineUseCaseImpl(
    private val repository: CatRepository
) : IsOnlineUseCase {
    override suspend operator fun invoke(): Flow<Boolean> = repository.isOnline()
}
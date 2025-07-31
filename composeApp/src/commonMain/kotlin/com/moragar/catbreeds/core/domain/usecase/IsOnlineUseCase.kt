package com.moragar.catbreeds.core.domain.usecase

import kotlinx.coroutines.flow.Flow

interface IsOnlineUseCase {
    suspend operator fun invoke(): Flow<Boolean>
}
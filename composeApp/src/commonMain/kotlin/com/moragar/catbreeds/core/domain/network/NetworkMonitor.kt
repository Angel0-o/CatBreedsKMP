package com.moragar.catbreeds.core.domain.network

import kotlinx.coroutines.flow.Flow

interface NetworkMonitor {
    suspend fun isOnline(): Flow<Boolean>
}
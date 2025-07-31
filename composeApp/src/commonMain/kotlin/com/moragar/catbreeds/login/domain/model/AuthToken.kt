package com.moragar.catbreeds.login.domain.model

import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime

@Serializable
data class AuthToken @OptIn(ExperimentalTime::class) constructor(
    val token: String,
    val expiresAt: Long = kotlin.time.Clock.System.now().epochSeconds + (1 * 60 * 1000) // 10 minutes
)

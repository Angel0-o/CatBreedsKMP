package com.moragar.catbreeds.login.data.repository

import com.moragar.catbreeds.login.data.auth.CatTokenManager
import com.moragar.catbreeds.login.data.auth.HashingUtils
import com.moragar.catbreeds.login.domain.model.AuthToken
import com.moragar.catbreeds.login.domain.model.UserCredentials
import com.moragar.catbreeds.login.domain.repository.AuthRepository
import kotlinx.coroutines.delay
import kotlin.time.ExperimentalTime

class AuthRepositoryImpl(
    private val catTokenManager: CatTokenManager
) : AuthRepository {
    // Mock user database (in real app, use real database)
    private val mockUsers = mapOf(
        "user@gmail.com" to HashingUtils.hashPassword("Password123!"),
        "admin@gmail.com" to HashingUtils.hashPassword("Admin123!"),
        "guest@gmail.com" to HashingUtils.hashPassword("Guest123!")
    )

    @OptIn(ExperimentalTime::class)
    override suspend fun login(credentials: UserCredentials): Result<AuthToken> {
        // Simulate network delay
        delay(1000)

        // Check credentials
        val storedHash = mockUsers[credentials.username]
        if (storedHash == null || !HashingUtils.verifyPassword(credentials.password, storedHash)) {
            return Result.failure(Exception("Invalid credentials"))
        }

        // Create token
        val token = AuthToken(
            token = "mock_token_${kotlin.time.Clock.System.now().epochSeconds}"
        )

        // Save token
        catTokenManager.saveToken(token)
        return Result.success(token)
    }

    override fun logout() {
        catTokenManager.clearToken()
    }

    override fun isAuthenticated(): Boolean {
        return catTokenManager.isTokenValid()
    }
}
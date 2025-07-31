package com.moragar.catbreeds.login.domain.repository

import com.moragar.catbreeds.login.domain.model.AuthToken
import com.moragar.catbreeds.login.domain.model.UserCredentials

interface AuthRepository {
    suspend fun login(credentials: UserCredentials): Result<AuthToken>
    fun logout()
    fun isAuthenticated(): Boolean
}
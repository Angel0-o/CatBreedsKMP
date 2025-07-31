package com.moragar.catbreeds.login.data.auth

import com.moragar.catbreeds.login.domain.model.AuthToken
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.Settings
import com.russhwolf.settings.serialization.decodeValueOrNull
import com.russhwolf.settings.serialization.encodeValue
import kotlinx.serialization.ExperimentalSerializationApi
import kotlin.time.ExperimentalTime


const val TOKEN_KEY = "auth_token"
class CatTokenManager(private val settings: Settings) {

    @OptIn(ExperimentalSerializationApi::class, ExperimentalSettingsApi::class)
    fun saveToken(token: AuthToken) {
        settings.encodeValue(TOKEN_KEY, token)
    }

    @OptIn(ExperimentalSerializationApi::class, ExperimentalSettingsApi::class)
    fun getToken(): AuthToken? {
        return settings.decodeValueOrNull( AuthToken.serializer(), TOKEN_KEY)
    }

    fun clearToken() {
        settings.remove(TOKEN_KEY)
    }

    @OptIn(ExperimentalTime::class)
    fun isTokenValid(): Boolean {
        val token = getToken()
        return token?.let { it.expiresAt > kotlin.time.Clock.System.now().epochSeconds } ?: false
    }
}
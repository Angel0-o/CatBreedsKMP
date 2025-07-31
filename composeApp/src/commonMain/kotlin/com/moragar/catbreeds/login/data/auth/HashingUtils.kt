package com.moragar.catbreeds.login.data.auth

import korlibs.crypto.Hash
import korlibs.crypto.SHA256

object HashingUtils {
    fun hashPassword(password: String): String {
        return password.encodeToByteArray().sha256().hex
    }

    fun verifyPassword(password: String, hashedPassword: String): Boolean {
        return hashPassword(password) == hashedPassword
    }

    private fun ByteArray.sha256(): Hash {
        return SHA256.digest(this)
    }
}
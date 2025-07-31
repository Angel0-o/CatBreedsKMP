package com.moragar.catbreeds.core.domain.database

import app.cash.sqldelight.db.SqlDriver

interface CatDatabaseFactory {
    fun createDriver(): SqlDriver
}
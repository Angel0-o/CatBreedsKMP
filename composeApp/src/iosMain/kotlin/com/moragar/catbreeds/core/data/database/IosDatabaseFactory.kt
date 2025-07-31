package com.moragar.catbreeds.core.data.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.moragar.catbreeds.core.domain.database.CatDatabaseFactory
import com.moragar.db.CatDatabase

class IosDatabaseFactory : CatDatabaseFactory {
    override fun createDriver(): SqlDriver {
        return NativeSqliteDriver(
            schema = CatDatabase.Schema,
            name = "CatDatabase.db"
        )
    }
}
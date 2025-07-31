package com.moragar.catbreeds.core.data.database

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.moragar.catbreeds.core.domain.database.CatDatabaseFactory
import com.moragar.db.CatDatabase

class AndroidDatabaseFactory(private val context: Context) : CatDatabaseFactory {
    override fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(
            schema = CatDatabase.Schema,
            context = context,
            name = "CatDatabase.db"
        )
    }
}
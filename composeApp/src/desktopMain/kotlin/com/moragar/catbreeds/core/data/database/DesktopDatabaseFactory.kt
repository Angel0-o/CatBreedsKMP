package com.moragar.catbreeds.core.data.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.moragar.catbreeds.core.domain.database.CatDatabaseFactory

class DesktopDatabaseFactory : CatDatabaseFactory {
    override fun createDriver(): SqlDriver {
         return JdbcSqliteDriver("jdbc:sqlite:CatDatabase.db")
    }
}
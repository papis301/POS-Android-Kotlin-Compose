package com.pisco.stockmanager.shared.data

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers

@Database(entities = [ProductEntity::class], version = 1, exportSchema = true)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
}

/**
 * Room génère l'implémentation "actual" de cet objet à la compilation
 * (obligatoire depuis Kotlin 2.0 pour les bases de données KMP).
 * Ne rien mettre dedans, ne pas le supprimer.
 */
@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}

/**
 * Chaque plateforme fournit son propre Builder (chemin de fichier différent).
 * Voir DatabaseFactory.android.kt et DatabaseFactory.desktop.kt
 */
expect class DatabaseFactory {
    fun create(): RoomDatabase.Builder<AppDatabase>
}

const val DATABASE_NAME = "stock_manager.db"

fun buildDatabase(factory: DatabaseFactory): AppDatabase {
    return factory.create()
        .fallbackToDestructiveMigration(dropAllTables = true)
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.Default)
        .build()
}